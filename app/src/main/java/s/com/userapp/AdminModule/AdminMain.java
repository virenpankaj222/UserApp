package s.com.userapp.AdminModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import s.com.userapp.MainDashboard.Dashboard;
import s.com.userapp.MainDashboard.Model.PostModel;
import s.com.userapp.R;
import s.com.userapp.Registration.Login;
import s.com.userapp.databinding.ActivityAdminMainBinding;
import s.com.userapp.databinding.FilterDialogeBinding;
import s.com.userapp.databinding.PasswordChangeLayoutBinding;

public class AdminMain extends AppCompatActivity {

    ActivityAdminMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Admin");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_header,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.exit)
        {
            FirebaseAuth.getInstance().signOut();
            getSharedPreferences("userData",MODE_PRIVATE).edit().putBoolean("login",false).commit();

            FirebaseFirestore.getInstance().collection("Admin").document("OFJsDD2O9jZDriY3GY7a").update("login",false);

            startActivity(new Intent(AdminMain.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
        if (item.getItemId()==R.id.password)
        {
            showFilterView();
        }

        return super.onOptionsItemSelected(item);
    }
    public void showFilterView()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        PasswordChangeLayoutBinding binding;
        binding= PasswordChangeLayoutBinding.inflate(LayoutInflater.from(this),null,false);
        builder.setView(binding.getRoot());

        AlertDialog alertDialog = builder.create();


        binding.tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass, cPass;
                pass = binding.etPassword.getText().toString().trim();
                cPass = binding.etCPass.getText().toString().trim();
                if (pass.isEmpty())
                {
                    binding.tlPassword.setError("Enter Password");
                    return;
                }
                else if (!cPass.equals(pass) )
                {
                    Toast.makeText(AdminMain.this, "Passwords should be match", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    FirebaseFirestore.getInstance().collection("Admin").document("OFJsDD2O9jZDriY3GY7a").update("password", pass);
                    alertDialog.dismiss();
                }
            }
        });

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });


        alertDialog.show();
    }
}