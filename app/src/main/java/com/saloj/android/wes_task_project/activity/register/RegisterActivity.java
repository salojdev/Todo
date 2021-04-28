package com.saloj.android.wes_task_project.activity.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saloj.android.wes_task_project.R;
import com.saloj.android.wes_task_project.activity.MainActivity;
import com.saloj.android.wes_task_project.activity.login.LoginActivity;
import com.saloj.android.wes_task_project.helper.AppPrefrence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    LinearLayout ll_login;
    Button btn_register;
    EditText edt_uname, edt_pwd;
    String username, password;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    ProgressDialog progressDialog;

    AppPrefrence appPrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ll_login = findViewById(R.id.ll_login);
        edt_uname = findViewById(R.id.edt_uname);
        edt_pwd = findViewById(R.id.edt_pwd);
        btn_register = findViewById(R.id.btn_submit);

        progressDialog = new ProgressDialog(this);
        appPrefrence = new AppPrefrence(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        ll_login.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        btn_register.setOnClickListener(v -> {
            username = edt_uname.getText().toString();
            password = edt_pwd.getText().toString();

            if (username.equals("")) {
                edt_uname.setError("can't be blank");
            } else if (password.equals("")) {
                edt_pwd.setError("can't be blank");
            } else if (password.length() < 5) {
                edt_pwd.setError("at least 5 characters long");
            } else {

                progressDialog.setTitle("Creating New Account ");
                progressDialog.setMessage("Please wait , while we are creating new account for you...");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();

                createNewAcc();

            }
        });
    }

    private void createNewAcc() {

        String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String dateString = sdf.format(date);

        String date_time = dateString + " " + currentTime;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String currentUserID = databaseReference.push().getKey();

     //   FirebaseDatabase database = FirebaseDatabase.getInstance();


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", currentUserID);
        hashMap.put("username", username);
        hashMap.put("password", password);
        hashMap.put("time_stamp", date_time);


      //  DatabaseReference users = database.getReference("Users"); //users is a node in your Firebase Database.
        //databaseReference.child("Users").setValue(hashMap);
        databaseReference.child("Users").child(password).setValue(hashMap);



        appPrefrence.setIsLogin("true");
        appPrefrence.setUserID(currentUserID);
        appPrefrence.setUsername(username);

        progressDialog.cancel();
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

        Toast.makeText(RegisterActivity.this, "Account created successfully..", Toast.LENGTH_SHORT).show();


    /*    mAuth.createUserWithEmailAndPassword(username,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(RegisterActivity.this, "Account created successfully..", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                            String currentUserID = mAuth.getCurrentUser().getUid();

                            databaseReference.child("Users").child(currentUserID).setValue("");

                            databaseReference.child("Users").child(currentUserID).child("user_id")
                                    .setValue(currentUserID);


                            Log.d("uid",currentUserID);

                            databaseReference.child("Users").child(currentUserID).child("password")
                                    .setValue(password);

                            appPrefrence.setIsLogin("true");
                            appPrefrence.setUserID(currentUserID);
                            appPrefrence.setUsername(username);

                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();


                        }
                        else {

                            String message = task.getException().toString();
                            Toast.makeText(RegisterActivity.this,  message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                        }
                    }
                });*/

    }
}