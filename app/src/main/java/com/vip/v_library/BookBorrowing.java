package com.vip.v_library;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import static com.vip.v_library.DatePickerFragment.RDate;

public class BookBorrowing extends AppCompatActivity {

    private ImageView BookImg;
    private TextView BookName;
    private TextView BookEdition;
    private TextView BookPublisherName;

    private EditText enrollmentNO;
    private Button VerifyStudent;
    private CardView studentdata;
    private ImageView studentimage;
    private TextView Studntname;
    private TextView Studentclass;
    private TextView Studentdept;
    public static Button borrowbtn;
    public static TextView date;
    private int a;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String studentname;
    private String studentmono;
    private String studentimg;

    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_borrowing);
        BookImg=findViewById(R.id.BBimg);
        BookName=findViewById(R.id.BBname);
        BookEdition=findViewById(R.id.BBedition);
        BookPublisherName=findViewById(R.id.BBPname);
        enrollmentNO=findViewById(R.id.BBSEnNo);
        VerifyStudent=findViewById(R.id.BBverifystudent);
        studentdata=findViewById(R.id.BBSdata);
        studentimage=findViewById(R.id.BBSimg);
        Studntname=findViewById(R.id.BBSname);
        Studentclass=findViewById(R.id.BBSclass);
        Studentdept=findViewById(R.id.BBSdept);
        borrowbtn=findViewById(R.id.BBbtn);
        date=findViewById(R.id.SelectDate);
        //////////////////////////////////////
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        Glide.with(this).load(bundle.getString("img")).into(BookImg);
        BookName.setText(bundle.getString("name"));
        BookEdition.setText(bundle.getString("edition"));
        BookPublisherName.setText(bundle.getString("publisher"));
        ///////////////////////////////////////////////////////////////
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        VerifyStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyStudent(enrollmentNO.getText().toString());
            }
        });
        db.collection("USERS").document(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if (documentSnapshot.exists()){
                        a= Integer.parseInt(documentSnapshot.get("borrowedbooks").toString());
                    }else{
                        a=1;
                    }
                }
            }
        });

       borrowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int i=a+1;
                Map<String,Object> borrowdata = new HashMap<>();
                borrowdata.put("eno",enrollmentNO.getText().toString());
                borrowdata.put("bookid",bundle.getString("id"));
                borrowdata.put("book",bundle.getString("name"));
                borrowdata.put("return",RDate);
                borrowdata.put("name",studentname);
                borrowdata.put("mono",studentmono);
                borrowdata.put("img",studentimg);
                borrowdata.put("index",i);


                db.collection("USERS").document(auth.getUid()).collection("BORROWDATA").document(enrollmentNO.getText().toString())
                        .set(borrowdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DocumentReference documentReference=db.collection("USERS").document(auth.getUid());
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot snapshot=task.getResult();
                                int j= Integer.parseInt(snapshot.get("borrowedbooks").toString());
                                int k=j+1;
                                String c= String.valueOf(k);
                                documentReference.update("borrowedbooks",c);

                            }
                        });
                        Toast.makeText(BookBorrowing.this, "Book Borrowed Succesful!!!", Toast.LENGTH_SHORT).show();
                        onBackPressed();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });



    }
    private void VerifyStudent(String EnNo){
        DocumentReference reference=db.collection("USERS").document(auth.getUid()).collection("STUDENTS").document(EnNo);
    //    if (db.collection("USERS").document(auth.getUid()).collection("STUDENTS").document(EnNo).get().isSuccessful()) {
            db.collection("USERS").document(auth.getUid()).collection("STUDENTS")
                    .document(EnNo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if (documentSnapshot.exists()) {
                        studentdata.setVisibility(View.VISIBLE);
                        Glide.with(BookBorrowing.this).load(documentSnapshot.get("Simg")).into(studentimage);
                        Studntname.setText(documentSnapshot.get("Sname").toString());
                        Studentclass.setText(documentSnapshot.get("Sclass").toString());
                        Studentdept.setText(documentSnapshot.get("dept").toString());
                        studentname=documentSnapshot.get("Sname").toString();
                        studentmono=documentSnapshot.get("MoNo").toString();
                        studentimg=documentSnapshot.get("Simg").toString();
                        enrollmentNO.setEnabled(false);
                        borrowbtn.setVisibility(View.VISIBLE);

                    }else {
                        Toast.makeText(BookBorrowing.this, "student not exist", Toast.LENGTH_SHORT).show();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BookBorrowing.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
      /*  }else {
            Toast.makeText(this, "Invalid Student", Toast.LENGTH_SHORT).show();
        }*/

    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }
}
