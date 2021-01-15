package s.com.userapp.MainDashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import s.com.userapp.Registration.Login;
import s.com.userapp.databinding.ActivityAddPostBinding;

public class AddPost extends AppCompatActivity {

    ActivityAddPostBinding binding;
    String callTime="";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Add Post");
        progressDialog.setMessage("Post Saving...");
        binding.tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postTitle,email,phone,skypeId,telegram,costRange,discription,name,callDate;
                postTitle=binding.etPostName.getText().toString().trim();
                email=binding.etEmail.getText().toString().trim();
                phone=binding.etPhone.getText().toString().trim();
                skypeId=binding.etSkype.getText().toString().trim();
                telegram=binding.etTelegram.getText().toString().trim();
                costRange=binding.etCost.getText().toString().trim();
                discription=binding.etDiscription.getText().toString().trim();
                callDate=binding.etCallDate.getText().toString().trim();
                name=binding.etName.getText().toString().trim();
                validate(postTitle,name,email,phone,skypeId,telegram,callDate,costRange,discription);
            }
        });

        binding.etCallTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseTimeForNext();
            }
        });


    }

    private void validate(String postTitle, String name,String email, String phone, String skypeId, String telegram, String callDate,String costRange, String discription) {
        if(postTitle.isEmpty())
        {
            binding.tlPostName.setError("Enter Title");
            return;
        }
        else if(callDate.isEmpty())
        {
            binding.tlCallDate.setError("Enter Date");
            return;
        }
        else if(callTime.isEmpty())
        {
            binding.tlCallRange.setError("Select Call Time");
            return;
        }
        else if (costRange.isEmpty())
        {
            binding.tlCostrange.setError("Enter Cost Range");
            return;
        }
        else if(discription.isEmpty())
        {
            binding.tlDiscription.setError("Enter Discription");
            return;
        }
        else
        {
            progressDialog.show();
            savePost(postTitle,name,email,phone,skypeId,telegram,callDate,callTime,costRange,discription);
        }


    }

    private void savePost(String postTitle,String name, String email, String phone, String skypeId, String telegram, String callDate, String callTime, String costRange, String discription) {

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            startActivity(new Intent(AddPost.this, Login.class));
            return;
        }

        Map<String, String> map=new HashMap<>();
        map.put("userId",FirebaseAuth.getInstance().getCurrentUser().getUid());
        map.put("postTitle",postTitle);
        map.put("name",name);
        map.put("email",email);
        map.put("phone",phone);
        map.put("skypeId",skypeId);
        map.put("telegram",telegram);
        map.put("callDate",callDate);
        map.put("callTime",callTime);
        map.put("costRange",costRange);
        map.put("discription",discription);
        FirebaseFirestore.getInstance().collection("posts").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Map<String, Object> map1=new HashMap<>();
                map1.put("addedAt", FieldValue.serverTimestamp());
                FirebaseFirestore.getInstance().collection("posts").document(documentReference.getId()).set(map1, SetOptions.merge());
                progressDialog.dismiss();
                Toast.makeText(AddPost.this, "Post Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddPost.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ChooseTimeForNext()
    {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddPost.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                callTime=showTime(selectedHour,selectedMinute);
                binding.etCallTime.setText(showTime(selectedHour,selectedMinute));
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Next Avalable");
        mTimePicker.show();

    }

    public String showTime(int hour, int min) {
        String tourStartTime="",mint,hours;
        String format="";

        Log.d("hasbdsdsasaa",""+hour+"  "+min);
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        if(hour<10)
        {
            hours="0"+String.valueOf(hour);
        }
        else
        {
            hours=String.valueOf(hour);
        }
        if(min<10)
        {
            mint="0"+String.valueOf(min);
        }
        else {
            mint=String.valueOf(min);
        }
        tourStartTime=tourStartTime+""+hours+":"+mint+format;
        return tourStartTime;
    }
}