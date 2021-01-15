package s.com.userapp.Entry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

import s.com.userapp.MainDashboard.Dashboard;
import s.com.userapp.R;
import s.com.userapp.Registration.Login;
import s.com.userapp.Registration.Register;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser()==null) {
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(MainActivity.this, Dashboard.class));
                    finish();
                }
            }
        },2000);
    }

}