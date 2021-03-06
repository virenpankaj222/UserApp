package s.com.userapp.AdminModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import s.com.userapp.MainDashboard.Model.PostModel;
import s.com.userapp.MainDashboard.Model.PostsAdapter;
import s.com.userapp.MainDashboard.PostListener;
import s.com.userapp.R;
import s.com.userapp.Utils.Constants;
import s.com.userapp.databinding.ActivityAdminHistoryBinding;
import s.com.userapp.databinding.FilterDialogeBinding;

public class Admin_History extends AppCompatActivity {

    ActivityAdminHistoryBinding binding;
    Query query;
    AminPostsAdapter adapter;
    FilterModel model;
    List<String> status=new ArrayList<>();
    List<String> months=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        status.clear();
        months.clear();
        status.add(Constants.under_review);
        status.add(Constants.converted);
        status.add(Constants.not_converted);
        status.add(Constants.under_disscusssion);
        status.add(Constants.not_interested);


        months.add("January");
        months.add("Feburary");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.filter)
        {
            showFilterView();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();
        model=new FilterModel();
        listener.onFilter(model);


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

        adapter = new AminPostsAdapter(query,listener1) {

            @Override
            protected void onDataChanged() {

                Log.d("asdfdsa",""+getItemCount());
                if (getItemCount() == 0) {
                    binding.rvPosts.setVisibility(View.GONE);
                } else {
                    binding.rvPosts.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(binding.rvPosts,
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        binding.rvPosts.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPosts.setAdapter(adapter);
    }


    private FilterListener listener=new FilterListener() {
        @Override
        public void onFilter(FilterModel model) {
             if (model.getStatus()==null)
             {
                 query= FirebaseFirestore.getInstance().collection("posts").orderBy("addedAt", Query.Direction.DESCENDING);
                 initRecyclerView();
                 adapter.startListening();
             }
             else
             {
                 query= FirebaseFirestore.getInstance().collection("posts").whereEqualTo("status",model.getStatus()).orderBy("addedAt", Query.Direction.DESCENDING);
                 initRecyclerView();
                 adapter.startListening();
             }



        }
    };
    private PostListener listener1=new PostListener() {
        @Override
        public void onPostSelect(PostModel model) {

        }

        @Override
        public void onEdit(PostModel model) {

        }

        @Override
        public void onDelete(PostModel model) {

        }

        @Override
        public void onStatusChange(PostModel model) {

        }

        @Override
        public void onCall(String phone) {

        }
    };



    public void showFilterView()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Admin_History.this);
        FilterDialogeBinding binding;
        binding=FilterDialogeBinding.inflate(LayoutInflater.from(this),null,false);
        builder.setView(binding.getRoot());
        ArrayAdapter statusAdapter=new ArrayAdapter(Admin_History.this, android.R.layout.simple_dropdown_item_1line,status);
        binding.spinnerStatus.setAdapter(statusAdapter);

        binding.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                model.setStatus(status.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog alertDialog = builder.create();

        binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFilter(model);
                alertDialog.dismiss();
            }
        });

        binding.tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model=new FilterModel();
                listener.onFilter(model);
                alertDialog.dismiss();

            }
        });


        alertDialog.show();
    }
}