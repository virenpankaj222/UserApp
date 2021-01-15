package s.com.userapp.MainDashboard.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import s.com.userapp.MainDashboard.PostListener;
import s.com.userapp.Utils.FirestoreAdapter;
import s.com.userapp.databinding.PostViewDetailsLayoutBinding;

public class PostsAdapter extends FirestoreAdapter<PostsAdapter.MyViewHolder> {

    PostListener listener;
    public PostsAdapter(Query query,PostListener listener) {
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

            binding.tvPostName.setText(model.getPostTitle());
            binding.tvName.setText("Name : "+model.getName());
            binding.tvEmail.setText("Email : "+model.getEmail());
            binding.tvphone.setText("Mobile : "+model.getPhone());
            binding.tvSkypeId.setText("Skype Id : "+model.getSkypeId());
            binding.tvTelegram.setText("Telegram : "+model.getTelegram());
            binding.tvCast.setText("Cost : "+model.getCostRange());
            binding.callDate.setText("Date : "+model.getCallDate()+" at "+model.getCallTime());
            binding.tvDiscription.setText(model.getDiscription());

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
        }
    }
}
