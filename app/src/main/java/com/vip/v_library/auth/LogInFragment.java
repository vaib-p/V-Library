package com.vip.v_library.auth;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vip.v_library.MainActivity;
import com.vip.v_library.R;

import java.util.concurrent.TimeUnit;


public class LogInFragment extends Fragment {
    private EditText ID;
    private EditText PIN;
    private Button LogIn;
    private TextView NotRegistered;
    private FrameLayout frameLayout;

    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private String Email;

    private EditText OTP;
    private Button SOTP;
    private TextView ResendOTP;
    private TextView forgotpass;
    private String verification_id;
    private Dialog dialog;
    private ImageView CDbtn;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_log_in, container, false);
        ID=view.findViewById(R.id.LID);
        PIN=view.findViewById(R.id.LPIN);
        LogIn=view.findViewById(R.id.Lbtn);
        NotRegistered=view.findViewById(R.id.LNRegistered);
        frameLayout=getActivity().findViewById(R.id.AuthFrameLayout);
        progressBar=view.findViewById(R.id.progressBar2);
        forgotpass=view.findViewById(R.id.ForgotPass);
        progressBar.animate();
        progressBar.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), ForgotPassword.class);
                i.putExtra("CODE","0");
                startActivity(i);
            }
        });
        ID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        PIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                firebaseFirestore.collection("USERIDs").document(ID.getText().toString())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot db=task.getResult();
                            if(db.exists()){
                                Email=db.get("EmailID").toString();
                                sendVerificationCode(db.get("MobileNumber").toString());
                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Please Enter Valid Login Details!!!", Toast.LENGTH_SHORT).show();
                            }




                        }

                    }
                });

            }
        });
        NotRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragement(new RegistrationFragment());

            }
        });
        /////////////////////////////////////////////////////////////////
        dialog=new Dialog(getContext());
        View Dview=getLayoutInflater().inflate(R.layout.otp_dialog,null);
        dialog.setContentView(Dview);
        ResendOTP=Dview.findViewById(R.id.ResendOTP);
        CDbtn=Dview.findViewById(R.id.CloseDialog);
        OTP=Dview.findViewById(R.id.Rotp);
        SOTP=Dview.findViewById(R.id.RotpBTN);
        CDbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
            }
        });
        SOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, OTP.getText().toString());
                signInWithCredential(credential);

            }
        });
        /////////////////////////////////////////////////////////////////
    }
    private void setFragement(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slide_out_from_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkinputs(){
        if (!TextUtils.isEmpty(ID.getText()) && ID.length() ==6){
                if (!TextUtils.isEmpty(PIN.getText()) && PIN.length() ==6){
                    LogIn.setEnabled(true);
                    LogIn.setTextColor(Color.rgb(255,255,255));


                }else{
                    LogIn.setEnabled(false);
                    LogIn.setTextColor(Color.argb(50,255,255,255));

                }
                //


        }else {
            LogIn.setEnabled(false);
            LogIn.setTextColor(Color.argb(50,255,255,255));

        }
    }

    private void LoginWithEmail(String email,String pin){



        mAuth.signInWithEmailAndPassword(email, pin)
                .addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i=new Intent(getContext(), MainActivity.class);
                            progressBar.setVisibility(View.GONE);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // ...
                        }

                        // ...
                    }
                });

    }

    ///////////////////////////////////////////
    private void sendVerificationCode(String number) {
        // progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,getActivity(),
                mCallBack
        );

        // progressBar.setVisibility(View.GONE);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {



        @Override
        public void onCodeSent(String id, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(id, forceResendingToken);
            verification_id=id;
            dialog.show();
            progressBar.setVisibility(View.GONE);


        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Toast.makeText(getContext(),"verification complete",Toast.LENGTH_LONG).show();
            signInWithCredential(phoneAuthCredential);

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            //progressBar.setVisibility(View.GONE);
        }
    };


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            FirebaseUser user=task.getResult().getUser();
                            mAuth.signOut();
                            LoginWithEmail(Email,PIN.getText().toString());




                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
    ///////////////////////////////////////////
}