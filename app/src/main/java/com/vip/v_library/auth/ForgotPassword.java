package com.vip.v_library.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vip.v_library.R;

public class ForgotPassword extends AppCompatActivity {


    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user=auth.getCurrentUser();



    EditText email;


    Button SENDOTPBTN;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);



        email=findViewById(R.id.FEmail);

        SENDOTPBTN=findViewById(R.id.FSendEmail);
        SENDOTPBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this, "Password Reset Email Sent !!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                /*auth.sendPasswordResetEmail(email.getText().toString()).then(function() {
                    // Email sent.
                }).catch(function(error) {
                    // An error happened.
                });*/
            }
        });

        if (user!=null){
            email.setText(user.getEmail().replace(" ",""));
            email.setEnabled(false);
        }
    }

}