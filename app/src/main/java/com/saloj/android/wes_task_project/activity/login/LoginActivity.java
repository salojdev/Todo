package com.saloj.android.wes_task_project.activity.login;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saloj.android.wes_task_project.R;
import com.saloj.android.wes_task_project.activity.MainActivity;
import com.saloj.android.wes_task_project.activity.NotesModel;
import com.saloj.android.wes_task_project.activity.register.RegisterActivity;
import com.saloj.android.wes_task_project.helper.AppPrefrence;

public class LoginActivity extends AppCompatActivity {

    LinearLayout ll_register;
    Button btn_login;
    EditText edt_uname, edt_pwd;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String username, password;
    AppPrefrence appPrefrence;
    String userid = "", uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ll_register = findViewById(R.id.ll_register);
        edt_uname = findViewById(R.id.edt_uname);
        edt_pwd = findViewById(R.id.edt_pwd);
        btn_login = findViewById(R.id.btn_submit);

        appPrefrence = new AppPrefrence(this);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        ll_register.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });

        btn_login.setOnClickListener(v -> {

            username = edt_uname.getText().toString();
            password = edt_pwd.getText().toString();

            if (username.equals("")) {
                edt_uname.setError("can't be blank");
            } else if (password.equals("")) {
                edt_pwd.setError("can't be blank");
            } else if (password.length() < 5) {
                edt_pwd.setError("at least 5 characters long");
            } else {
                getAuthenticate(username, password);
            }
        });

    }

    private void getAuthenticate(String username, String password) {

        progressDialog.setTitle("Authenticate Your Account");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference database = mFirebaseDatabase.getReference("Users").child(password);
        DatabaseReference useridRef = database.child("user_id");
        DatabaseReference passwordRef = database.child("password");
        DatabaseReference usernameRef = database.child("username");


        usernameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Tag", "uname" + dataSnapshot.getValue(String.class));

                uname = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "onCancelled", databaseError.toException());
            }
        });


        useridRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Tag", "userid" + dataSnapshot.getValue(String.class));

                userid = dataSnapshot.getValue(String.class);
             /*   if (username.equals(userid)){
                    progressDialog.cancel();
                }else {
                    Toast.makeText(LoginActivity.this, "Username incorrect", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "onCancelled", databaseError.toException());
            }
        });


        passwordRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Tag", "pwd" + dataSnapshot.getValue(String.class));

                String pwd = dataSnapshot.getValue(String.class);

                 if (uname == null) {
                    progressDialog.cancel();
                     Toast.makeText(LoginActivity.this, "Both credential does not match !", Toast.LENGTH_SHORT).show();


                 } else if (pwd == null) {
                    progressDialog.cancel();
                     Toast.makeText(LoginActivity.this, "Both credential does not match !", Toast.LENGTH_SHORT).show();
                } else if (username.equals(uname) && password.equals(pwd)) {

                    progressDialog.cancel();

                    appPrefrence.setUserID(userid);
                    appPrefrence.setUsername(username);
                    appPrefrence.setIsLogin("true");
                    Toast.makeText(LoginActivity.this, "Logged is successfully..", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(LoginActivity.this, "Both credential not match", Toast.LENGTH_SHORT).show();

                    progressDialog.cancel();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "onCancelled", databaseError.toException());
            }
        });




/*
        database.orderByChild("password").equalTo(password).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                progressDialog.cancel();

            */
/*    appPrefrence.setUserID(userid);
                appPrefrence.setUsername(username);
                appPrefrence.setIsLogin("true");

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();*//*

                Toast.makeText(LoginActivity.this, "Logged is successfully..", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.cancel();
            }
        });
*/

     /*   mAuth.signInWithEmailAndPassword(username,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            String currentUserId = mAuth.getCurrentUser().getUid();

                            Toast.makeText(LoginActivity.this, "Logged is successfully..", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();

                            appPrefrence.setUserID(currentUserId);
                            appPrefrence.setUsername(username);
                            appPrefrence.setIsLogin("true");

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            String message = task.getException().toString();
                            Toast.makeText(LoginActivity.this,  message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                    }
                });*/


    }


}