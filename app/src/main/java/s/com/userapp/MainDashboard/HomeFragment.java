package s.com.userapp.MainDashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import s.com.userapp.MainDashboard.Model.PostModel;
import s.com.userapp.MainDashboard.Model.PostsAdapter;
import s.com.userapp.R;
import s.com.userapp.databinding.FragmentHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FragmentHomeBinding binding;
    Query query;
    PostsAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),AddPost.class));
            }
        });

        binding.tvMyHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),UserHistory.class));
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        query= FirebaseFirestore.getInstance().collection("posts").whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid()).orderBy("addedAt", Query.Direction.DESCENDING);
        initRecyclerView();

        if(adapter!=null)
        {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter!=null)
        {
            adapter.stopListening();
        }
    }

    private void initRecyclerView() {
        if (query == null) {
            Log.w("TAG", "No query, not initializing RecyclerView");
        }

        adapter = new PostsAdapter(query,listener) {

            @Override
            protected void onDataChanged() {

                Log.d("asdfdsa",""+getItemCount());
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.rvPosts.setVisibility(View.GONE);
//                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    binding.rvPosts.setVisibility(View.VISIBLE);
//                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(binding.rvPosts,
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        binding.rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPosts.setAdapter(adapter);
    }

    private PostListener listener=new PostListener() {
        @Override
        public void onPostSelect(PostModel model) {
            Bundle bundle = new Bundle();
            bundle.putString("postId", model.getPostId());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_homeToDetails, bundle);
        }

        @Override
        public void onEdit(PostModel model) {

            Bundle bundle = new Bundle();
            bundle.putString("postId", model.getPostId());
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_update, bundle);

//            NavHostFragment.findNavController(HomeFragment.this)
//                    .navigate(R.id.action_update);
        }

        @Override
        public void onDelete(PostModel model) {

        }

        @Override
        public void onStatusChange(PostModel model) {

        }
    };

}