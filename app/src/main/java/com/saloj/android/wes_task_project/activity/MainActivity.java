package com.saloj.android.wes_task_project.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saloj.android.wes_task_project.R;
import com.saloj.android.wes_task_project.activity.login.LoginActivity;
import com.saloj.android.wes_task_project.activity.register.RegisterActivity;
import com.saloj.android.wes_task_project.helper.AppPrefrence;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
LinearLayout ll_logout;
AppPrefrence appPrefrence;
FloatingActionButton fb_add_note;

//String uidStr;
    boolean notify = false;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    private NotesListAdapter adapter;
    private ArrayList<NotesModel> notesModelArrayList;
    private RecyclerView rv_notes;
ProgressDialog progressDialog;
LinearLayout ll_not_found;
    ShimmerFrameLayout lazzyLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appPrefrence = new AppPrefrence(this);
        progressDialog = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
     //   uidStr = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        lazzyLoader = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        lazzyLoader.startShimmerAnimation();

        ll_logout = findViewById(R.id.ll_logout);
        fb_add_note = findViewById(R.id.fb_add_note);
        ll_not_found = findViewById(R.id.ll_not_found);

        rv_notes = findViewById(R.id.recy_notes);

        ll_not_found.setVisibility(View.GONE);
        rv_notes.setVisibility(View.GONE);
        lazzyLoader.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       // linearLayoutManager.setStackFromEnd(true);

        rv_notes.setHasFixedSize(true);
        rv_notes.setLayoutManager(linearLayoutManager);

        notesModelArrayList = new ArrayList<>();

        progressDialog.show();

        fetchNotes();

        ll_logout.setOnClickListener(v -> {

      alertDialog();
        });

        fb_add_note.setOnClickListener(v -> {
            getBottomSheetNoteAdd();
        });

    }

    private void fetchNotes() {

        notesModelArrayList = new ArrayList<>();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mFirebaseDatabase.getReference("Notes").child(appPrefrence.getUserID());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.cancel();
                notesModelArrayList.clear();
                lazzyLoader.setVisibility(View.GONE);
                lazzyLoader.stopShimmerAnimation();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    NotesModel messageModel = ds.getValue(NotesModel.class);
                    notesModelArrayList.add(messageModel);
                }

                if (notesModelArrayList.size()==0){
                    ll_not_found.setVisibility(View.VISIBLE);
                    rv_notes.setVisibility(View.GONE);
                }else {
                    rv_notes.setVisibility(View.VISIBLE);
                    ll_not_found.setVisibility(View.GONE);
                }
                adapter = new NotesListAdapter(MainActivity.this,notesModelArrayList,MainActivity.this);
                rv_notes.setAdapter(adapter);
              adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.cancel();
                ll_not_found.setVisibility(View.VISIBLE);
                lazzyLoader.setVisibility(View.GONE);
                lazzyLoader.stopShimmerAnimation();
            }
        });
    }

    private void getBottomSheetNoteAdd() {

        Dialog dialog = new Dialog(this, R.style.FullScreenDialogWithStatusBarColorAccent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.note_add_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView cancel = (ImageView)dialog.findViewById(R.id.img_cancel);
        EditText edtTitleNotes = (EditText)dialog.findViewById(R.id.edt_title_notes);
        EditText edtDesNotes = (EditText)dialog.findViewById(R.id.edt_notes);
        CardView btnSubmit = (CardView) dialog.findViewById(R.id.card_view_submit);
        LinearLayout ll_notes =(LinearLayout) dialog.findViewById(R.id.ll_notes);

        String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String dateString = sdf.format(date);

        String  date_time = dateString+" "+currentTime;

        Log.d("Main", "getBottomSheetNoteAdd: "+date_time);

        cancel.setOnClickListener(v -> {
            dialog.cancel();

        });



        btnSubmit.setOnClickListener(v -> {

            btnSubmit.setEnabled(false);
            String titleNotesStr = edtTitleNotes.getText().toString();
            String  descNotes = edtDesNotes.getText().toString();

            progressDialog.show();
            saveNotes(titleNotesStr,descNotes,date_time);

            dialog.cancel();
        });

        edtTitleNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    btnSubmit.setVisibility(View.VISIBLE);

                } else {
                    btnSubmit.setVisibility(View.GONE);

                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btnSubmit.setVisibility(View.VISIBLE);

                } else {
                    btnSubmit.setVisibility(View.GONE);

                }
            }
        });
    }

    private void saveNotes(String titleNotesStr, String descNotes, String date_time) {

        String notes_id = databaseReference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", appPrefrence.getUserID());
        hashMap.put("notes_id", notes_id);
        hashMap.put("title", titleNotesStr);
        hashMap.put("description", descNotes);
        hashMap.put("time_stamp", date_time);
        databaseReference.child("Notes").child(appPrefrence.getUserID()).child(notes_id).setValue(hashMap);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(appPrefrence.getUserID());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                progressDialog.cancel();
            //    NotesModel notesModel = snapshot.getValue(NotesModel.class);
                if (notify) {
                    /*overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);*/
                    fetchNotes();
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
progressDialog.cancel();
            }
        });

    }


    private void alertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout !");
        builder.setMessage("Are you sure to Logout ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                appPrefrence.setIsLogin("false");
                appPrefrence.setUsername("");
                appPrefrence.setUserID("");

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

                dialog.cancel();


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void alertDelete(String notes_id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete !");
        builder.setMessage("Are you sure to Delete ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(appPrefrence.getUserID()).child(notes_id);
                databaseReference.removeValue();
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                fetchNotes();

                dialog.cancel();


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void getNotesEdit(String notes_id, String title ,String description) {

        Dialog dialog = new Dialog(this, R.style.FullScreenDialogWithStatusBarColorAccent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.note_add_layout);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags = WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView cancel = (ImageView)dialog.findViewById(R.id.img_cancel);
        EditText edtTitleNotes = (EditText)dialog.findViewById(R.id.edt_title_notes);
        EditText edtDesNotes = (EditText)dialog.findViewById(R.id.edt_notes);
        CardView btnSubmit = (CardView) dialog.findViewById(R.id.card_view_submit);
        LinearLayout ll_notes =(LinearLayout) dialog.findViewById(R.id.ll_notes);

        edtTitleNotes.setText(title);
        edtDesNotes.setText(description);
        String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String dateString = sdf.format(date);

        String  date_time = dateString+" "+currentTime;

        Log.d("Main", "getBottomSheetNoteAdd: "+date_time);

        cancel.setOnClickListener(v -> {
            dialog.cancel();

        });


        btnSubmit.setOnClickListener(v -> {

            btnSubmit.setEnabled(false);
            String titleNotesStr = edtTitleNotes.getText().toString();
            String  descNotes = edtDesNotes.getText().toString();

            updateNotes(notes_id,titleNotesStr,descNotes,date_time);

            dialog.cancel();
        });

        edtTitleNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    btnSubmit.setVisibility(View.VISIBLE);

                } else {
                    btnSubmit.setVisibility(View.GONE);

                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btnSubmit.setVisibility(View.VISIBLE);

                } else {
                    btnSubmit.setVisibility(View.GONE);

                }
            }
        });




    }

    private void updateNotes(String notes_id,String titleNotesStr, String descNotes, String date_time) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(appPrefrence.getUserID()).child(notes_id);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", appPrefrence.getUserID());
        hashMap.put("notes_id", notes_id);
        hashMap.put("title", titleNotesStr);
        hashMap.put("description", descNotes);
        hashMap.put("time_stamp", date_time);

        databaseReference.setValue(hashMap);
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();

        fetchNotes();

    }

    public void getDeleted(String notes_id, String title) {


        alertDelete(notes_id);


    }
}