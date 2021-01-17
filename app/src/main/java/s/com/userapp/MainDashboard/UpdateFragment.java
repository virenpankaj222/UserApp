package s.com.userapp.MainDashboard;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import s.com.userapp.MainDashboard.Model.PostModel;
import s.com.userapp.R;
import s.com.userapp.Registration.Login;
import s.com.userapp.databinding.FragmentUpdateBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentUpdateBinding binding;
    String callTime="";
    ProgressDialog progressDialog;
    ListenerRegistration registration;


    public UpdateFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(String id) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
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
        // Inflate the layout for this fragment
        binding=FragmentUpdateBinding.inflate(inflater,container,false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Update Post");
        progressDialog.setMessage("Post Updating...");
        binding.tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postTitle,email,phone,skypeId,telegram,costRange,discription,name,callDate;
                postTitle=binding.etPostName.getText().toString().trim();
                email=binding.etEmail.getText().toString().trim();
                phone=binding.etPhone.getText().toString().trim();
                skypeId=binding.etSkype.getText().toString().trim();
                telegram=binding.etTelegram.getText().toString().trim();
                costRange=binding.etCost.getText().toString().trim();
                discription=binding.etDiscription.getText().toString().trim();
                callDate=binding.etCallDate.getText().toString().trim();
                name=binding.etName.getText().toString().trim();
                validate(postTitle,name,email,phone,skypeId,telegram,callDate,costRange,discription);
            }
        });

        binding.etCallTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseTimeForNext();
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
             if (error!=null)
             {
                 return;
             }

                PostModel model=value.toObject(PostModel.class);
             model.setPostId(value.getId());

             binding.etPostName.setText(model.getPostTitle());
             binding.etName.setText(model.getName());
                binding.etEmail.setText(model.getEmail());
                binding.etPhone.setText(model.getPhone());
                binding.etSkype.setText(model.getSkypeId());
                binding.etTelegram.setText(model.getTelegram());
                binding.etCost.setText(model.getCostRange());
                binding.etCallDate.setText(model.getCallDate());
                binding.etCallTime.setText(model.getCallTime());
                callTime=model.getCallTime();
                binding.etDiscription.setText(model.getDiscription());
            }
        });
    }

    private void validate(String postTitle, String name, String email, String phone, String skypeId, String telegram, String callDate, String costRange, String discription) {
        if(postTitle.isEmpty())
        {
            binding.tlPostName.setError("Enter Title");
            return;
        }
        else if(callDate.isEmpty())
        {
            binding.tlCallDate.setError("Enter Date");
            return;
        }
        else if(callTime.isEmpty())
        {
            binding.tlCallRange.setError("Select Call Time");
            return;
        }
        else if (costRange.isEmpty())
        {
            binding.tlCostrange.setError("Enter Cost Range");
            return;
        }
        else if(discription.isEmpty())
        {
            binding.tlDiscription.setError("Enter Discription");
            return;
        }
        else
        {
            progressDialog.show();
            updatePost(postTitle,name,email,phone,skypeId,telegram,callDate,callTime,costRange,discription);
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (registration!=null)
        {
            registration.remove();
        }
    }

    private void updatePost(String postTitle, String name, String email, String phone, String skypeId, String telegram, String callDate, String callTime, String costRange, String discription) {

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            startActivity(new Intent(getContext(), Login.class));
            return;
        }

        Map<String, String> map=new HashMap<>();
        map.put("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("postTitle",postTitle);
        map.put("name",name);
        map.put("email",email);
        map.put("phone",phone);
        map.put("skypeId",skypeId);
        map.put("telegram",telegram);
        map.put("callDate",callDate);
        map.put("callTime",callTime);
        map.put("costRange",costRange);
        map.put("discription",discription);
        FirebaseFirestore.getInstance().collection("posts").document(mParam1).set(map,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                NavHostFragment.findNavController(UpdateFragment.this)
                        .navigate(R.id.action_updatetoHome);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ChooseTimeForNext()
    {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                callTime=showTime(selectedHour,selectedMinute);
                binding.etCallTime.setText(showTime(selectedHour,selectedMinute));
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Next Avalable");
        mTimePicker.show();

    }

    public String showTime(int hour, int min) {
        String tourStartTime="",mint,hours;
        String format="";

        Log.d("hasbdsdsasaa",""+hour+"  "+min);
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        if(hour<10)
        {
            hours="0"+String.valueOf(hour);
        }
        else
        {
            hours=String.valueOf(hour);
        }
        if(min<10)
        {
            mint="0"+String.valueOf(min);
        }
        else {
            mint=String.valueOf(min);
        }
        tourStartTime=tourStartTime+""+hours+":"+mint+format;
        return tourStartTime;
    }
}