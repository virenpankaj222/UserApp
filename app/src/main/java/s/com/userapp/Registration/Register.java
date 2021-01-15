package s.com.userapp.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import s.com.userapp.MainDashboard.Dashboard;
import s.com.userapp.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        getSupportActionBar().hide();
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Context context;
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Registering..");
        binding.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,email,phone,password;

                name=binding.etName.getText().toString().trim();
                phone=binding.etPhone.getText().toString().trim();
                email=binding.etEmail.getText().toString().trim();
                password=binding.etPassword.getText().toString().trim();

                validate(name,phone,email,password);

            }
        });
    }

    private void validate(String name, String phone, String email, String password) {

        if(name.isEmpty())
        {
            binding.tlName.setError("Enter name");
            return;
        }

        else if(phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches())
        {
            binding.tlPhone.setError("Enter Valid Phone");
            return;
        }
        else if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.tlEmail.setError("Enter Valid Email");
            return;
        }
        else if(password.isEmpty() || password.length()<8)
        {
            binding.tlPassword.setError("Password should be 8 or more chars");
            return;
        }
        else
        {
            progressDialog.show();
            register(name,phone,email,password);
        }

    }

    private void register(String name, String phone, String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                authResult.getUser().updateProfile(profileUpdates);
             Map<String, String> user = new HashMap<>();
             user.put("name",name);
             user.put("email",email);
             user.put("phone",phone);
             user.put("password",password);;

             FirebaseFirestore.getInstance().collection("users").document(authResult.getUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {
                     progressDialog.dismiss();
                     Toast.makeText(Register.this, "Register Success", Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(Register.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                     finish();
                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     progressDialog.dismiss();
                     Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     progressDialog.dismiss();
                     Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
             });
            }
        });
    }
}