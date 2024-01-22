package com.vip.v_library.myaccount;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vip.v_library.R;

public class MyLibraryInfoActivity extends AppCompatActivity {
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth auth=FirebaseAuth.getInstance();


    private ImageView logo;
    private TextView id;
    private TextView name;
    private TextView mono;
    private TextView email;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_library_info);
        Toolbar toolbar=findViewById(R.id.toolbarinfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MY LIBRARY INFO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logo=findViewById(R.id.MLLogo);
        id=findViewById(R.id.MLID);
        name=findViewById(R.id.MLName);
        mono=findViewById(R.id.MLMoNo);
        email=findViewById(R.id.MLEmail);
        db.collection("USERS").document(auth.getUid()).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot=task.getResult();
                            Glide.with(getApplicationContext()).load(snapshot.get("logo").toString()).into(logo);
                            id.setText("ID : \n"+snapshot.get("LibraryID").toString());
                            name.setText("Name : \n"+snapshot.get("LibraryName").toString());
                            mono.setText("Mobile Number : \n"+snapshot.get("MobileNumber").toString());
                            email.setText("Email ID : \n"+snapshot.get("EmailID").toString());
                        }
                    }
                }
        );
    }
}