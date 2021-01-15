package s.com.userapp.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import s.com.userapp.MainDashboard.Dashboard;
import s.com.userapp.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login..");

        binding.tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;

                email=binding.etEmail.getText().toString().trim();
                password=binding.etPassword.getText().toString().trim();
                validate(email,password);
            }
        });



    }

    private void validate(String email, String password) {
        if (email.isEmpty())
        {
            binding.tlEmail.setError("Enter Email");
            return;
        }
        else if (password.isEmpty())
        {
            binding.tlPassword.setError("Enter Password");
            return;
        }
        else
        {
            progressDialog.show();
            login(email,password);
        }
    }

    private void login(String email, String password) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                progressDialog.dismiss();
                startActivity(new Intent(Login.this, Dashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}