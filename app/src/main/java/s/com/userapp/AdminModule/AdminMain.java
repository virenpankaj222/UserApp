package s.com.userapp.AdminModule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import s.com.userapp.MainDashboard.Dashboard;
import s.com.userapp.R;
import s.com.userapp.Registration.Login;
import s.com.userapp.databinding.ActivityAdminMainBinding;

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
        getMenuInflater().inflate(R.menu.header_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.exit)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminMain.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
        if (item.getItemId()==R.id.filter)
        {
            Toast.makeText(this, "in Progress", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

}