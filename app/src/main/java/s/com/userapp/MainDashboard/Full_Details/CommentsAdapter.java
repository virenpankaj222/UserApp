package s.com.userapp.MainDashboard.Full_Details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import s.com.userapp.databinding.CommentLayoutBinding;

public class CommentsAdapter  extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    List<CommentModel> models;

    public CommentsAdapter(List<CommentModel> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(CommentLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.onDatabind(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CommentLayoutBinding binding;

        public MyViewHolder(CommentLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void onDatabind(CommentModel model)
        {
            if(model.getUserId().equals("OFJsDD2O9jZDriY3GY7a")) {
                binding.tvUser.setText("Me :");
            }
            else
            {
                binding.tvUser.setText(model.getUserName());
            }

            binding.tvComment.setText(model.getComment());
        }


    }
}
