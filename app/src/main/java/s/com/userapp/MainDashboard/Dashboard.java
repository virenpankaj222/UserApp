package s.com.userapp.MainDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import s.com.userapp.R;
import s.com.userapp.Registration.Login;
import s.com.userapp.databinding.ActivityDashboardBinding;

public class Dashboard extends AppCompatActivity {


    ActivityDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            startActivity(new Intent(Dashboard.this, Login.class));
            return;
        }

        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("deviceId",getSharedPreferences("TokenID",MODE_PRIVATE).getString("tokenId",""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.header_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.exit)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Dashboard.this,Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
        if (item.getItemId()==R.id.filter)
        {
            Toast.makeText(this, "in Progress", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}