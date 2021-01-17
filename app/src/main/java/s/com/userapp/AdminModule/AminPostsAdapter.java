package s.com.userapp.AdminModule;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;

import s.com.userapp.MainDashboard.Model.PostModel;
import s.com.userapp.MainDashboard.Model.PostsAdapter;
import s.com.userapp.MainDashboard.PostListener;
import s.com.userapp.Utils.Constants;
import s.com.userapp.Utils.FirestoreAdapter;
import s.com.userapp.databinding.PostViewDetailsLayoutBinding;

public class AminPostsAdapter extends FirestoreAdapter<AminPostsAdapter.MyViewHolder> {

    PostListener listener;
    public AminPostsAdapter(Query query, PostListener listener) {
        super(query);
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(PostViewDetailsLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.dataBind(getSnapshot(position));

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        PostViewDetailsLayoutBinding binding;

        public MyViewHolder(PostViewDetailsLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void dataBind(DocumentSnapshot snapshot)
        {
            PostModel model=snapshot.toObject(PostModel.class);
            model.setPostId(snapshot.getId());

            FirebaseMessaging.getInstance().subscribeToTopic(snapshot.getId())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "subs";
                            if (!task.isSuccessful()) {
                                msg = "Fail to subbs";
                            }
//                        Log.d(TAG, msg);
                        }
                    });
            binding.tvPostName.setText(model.getPostTitle());
            binding.tvName.setText("Name : "+model.getName());
            binding.tvEmail.setText("Email : "+model.getEmail());
            binding.tvphone.setText("Mobile : "+model.getPhone());
            binding.tvSkypeId.setText("Skype Id : "+model.getSkypeId());
            binding.tvTelegram.setText("Telegram : "+model.getTelegram());
            binding.tvCast.setText("Cost : "+model.getCostRange());
            binding.callDate.setText("Date : "+model.getCallDate()+" at "+model.getCallTime());
            binding.tvDiscription.setText(model.getDiscription());
            binding.tvPostby.setText("Post By : "+model.getPostedBy());
            binding.tvPostby.setVisibility(View.VISIBLE);
            binding.viewPby.setVisibility(View.VISIBLE);
            binding.tvStatus.setText(model.getStatus());
            binding.ivDelete.setVisibility(View.VISIBLE);

            if(model.getStatus().equals(Constants.converted))
            {
                binding.tvStatus.setBackgroundColor(Color.parseColor("#067841"));
            }
            else {
                binding.tvStatus.setBackgroundColor(Color.parseColor("#ff6516"));

            }
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostSelect(model);
                }
            });

            binding.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onEdit(model);
                }
            });

            binding.tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onStatusChange(model);
                }
            });

            binding.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDelete(model);
                }
            });

            binding.tvphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCall(model.getPhone());
                }
            });
        }
    }
}
