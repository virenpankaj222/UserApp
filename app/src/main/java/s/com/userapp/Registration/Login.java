package s.com.userapp.Registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import s.com.userapp.AdminModule.AdminMain;
import s.com.userapp.MainDashboard.Dashboard;
import s.com.userapp.MainDashboard.Model.AdminCredentials_model;
import s.com.userapp.R;
import s.com.userapp.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    String type="";
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

        binding.rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.rb_admin)
                {
                    type="ADMIN";
                }
                else if (id==R.id.rb_bda)
                {
                    type="USER";
                }
            }
        });

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.isEmpty())
                {
                    Toast.makeText(Login.this, "Please Select Login Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                String email,password;
                email=binding.etEmail.getText().toString().trim();
                password=binding.etPassword.getText().toString().trim();
                validate(email,password,type);
            }
        });



    }

    private void validate(String email, String password,String type) {
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
            if (type.equals("USER"))
            {
            progressDialog.show();
            login(email,password);
        }
            else
            {
                progressDialog.show();
                loginAdmin(email,password);
            }
        }
    }

    private void loginAdmin(String email, String password) {
        FirebaseFirestore.getInstance().collection("Admin").document("OFJsDD2O9jZDriY3GY7a").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {

                progressDialog.dismiss();

                AdminCredentials_model model=snapshot.toObject(AdminCredentials_model.class);

                if (model.getUserId().equals(email) && model.getPassword().equals(password))
                {
                    if(model.isLogin())
                    {
                        Toast.makeText(Login.this, "Admin Already Login..", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        getSharedPreferences("userData",MODE_PRIVATE).edit().putBoolean("login",true).commit();
                        FirebaseFirestore.getInstance().collection("Admin").document("OFJsDD2O9jZDriY3GY7a").update("login",true);

                        startActivity(new Intent(Login.this, AdminMain.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(Login.this, "User doesn't exist ", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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