package s.com.userapp.AdminModule;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import s.com.userapp.MainDashboard.Full_Details.CommentModel;
import s.com.userapp.MainDashboard.Full_Details.CommentsAdapter;
import s.com.userapp.MainDashboard.Model.PostModel;
import s.com.userapp.R;
import s.com.userapp.Utils.Constants;
import s.com.userapp.databinding.FragmentAdminDetailsBinding;
import s.com.userapp.databinding.FragmentAdminHomeBinding;
import s.com.userapp.notification.NotificationController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminDetails extends Fragment implements NotificationController.NotificationView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentAdminDetailsBinding binding;
    ListenerRegistration registration;
    NotificationController controller;
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    PostModel model;

    public AdminDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminDetails newInstance(String param1, String param2) {
        AdminDetails fragment = new AdminDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("postId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentAdminDetailsBinding.inflate(inflater,container,false);
        controller=new NotificationController(getContext(),this);
        binding.ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.etComment.getText().toString().trim().isEmpty())
                {
                    return;
                }

                CommentModel modelComment=new CommentModel(getSaltString(),"OFJsDD2O9jZDriY3GY7a",binding.etComment.getText().toString().trim(),"Admin");
                FirebaseFirestore.getInstance().collection("posts").document(mParam1).update("comments", FieldValue.arrayUnion(modelComment));
                TOPIC = "/topics/"+model.getPostId(); //topic has to match what the receiver subscribed to
                NOTIFICATION_TITLE = model.getPostTitle();
                NOTIFICATION_MESSAGE = binding.etComment.getText().toString().trim();
                binding.etComment.setText("");

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);
                    notification.put("to", TOPIC);
                    notification.put("data", notifcationBody);
                } catch (JSONException e) {
                    Log.e("notificationjsonksddnfi", "onCreate: " + e.getMessage() );
                }

                Log.d("notificationjsonksddnfi",""+notification);
                controller.sendNotification(notification);

            }
        });

        return binding.getRoot();
    }
    @Override
    public void onStart() {
        super.onStart();

        registration=FirebaseFirestore.getInstance().collection("posts").document(mParam1).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    return;
                }

                 model=value.toObject(PostModel.class);
                model.setPostId(value.getId());

                binding.tvPostName.setText(model.getPostTitle());
                binding.tvName.setText("Name : "+model.getName());
                binding.tvEmail.setText("Email : "+model.getEmail());
                binding.tvphone.setText("Mobile : "+model.getPhone());
                binding.tvSkypeId.setText("Skype Id : "+model.getSkypeId());
                binding.tvTelegram.setText("Telegram : "+model.getTelegram());
                binding.tvCast.setText("Cost : "+model.getCostRange());
                binding.callDate.setText("Date : "+model.getCallDate()+" at "+model.getCallTime());
                binding.tvDiscription.setText(model.getDiscription());
                binding.tvStatus.setText(model.getStatus());

                if(model.getStatus().equals(Constants.converted))
                {
                    binding.tvStatus.setBackgroundColor(Color.parseColor("#067841"));
                }
                else {
                    binding.tvStatus.setBackgroundColor(Color.parseColor("#ff6516"));

                }

                binding.rvComment.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                binding.rvComment.setAdapter(new CommentsAdapter(getReverseComment(model.getComments())));
            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
        if(registration!=null)
        {
            registration.remove();
        }
    }

    private List<CommentModel> getReverseComment(List<CommentModel> commentModels)
    {
        List<CommentModel> comments=new ArrayList<>();
        for (int i=commentModels.size()-1;i>=0;i--)
        {
            comments.add(commentModels.get(i));
        }

        return comments;
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @Override
    public void onNotificationSend(String msg) {

    }

    @Override
    public void onNotificationFail(String msg) {

    }
}