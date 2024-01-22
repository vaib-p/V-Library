package com.vip.v_library.auth;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.vip.v_library.MainActivity;
import com.vip.v_library.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class RegistrationFragment extends Fragment {
    public static String LIBRARYID;
    private EditText LName;
    private EditText Lid;
    private EditText LEmail;
    private EditText LMoNo;
    private EditText LNpin;
    private Button LRegister;
    private TextView LAlreadyRegistered;
    private FrameLayout frameLayout;

    private CountryCodePicker codePicker;

    private Button Select;
    private ImageView logo;

    private ImageView CDbtn;
    private String verification_id;
    private Dialog dialog;
    private ProgressBar progressBar;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

    private EditText OTP;
    private Button SOTP;
    private TextView ResendOTP;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String Imagename;
    Uri Imageuri;

    String currentPhotoPath;
    private StorageReference storageReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_registration, container, false);
        LName=view.findViewById(R.id.RLibraryName);
        Lid=view.findViewById(R.id.RLibraryCode);
        LEmail=view.findViewById(R.id.REmail);
        LMoNo=view.findViewById(R.id.RMobileNo);
        LNpin=view.findViewById(R.id.RPin);
        LRegister=view.findViewById(R.id.Update);
        LAlreadyRegistered=view.findViewById(R.id.RAlready);
        frameLayout=getActivity().findViewById(R.id.AuthFrameLayout);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        progressBar.animate();
        codePicker=view.findViewById(R.id.CcodePicker);
        codePicker.registerCarrierNumberEditText(LMoNo);
        storageReference = FirebaseStorage.getInstance().getReference();
        Select=view.findViewById(R.id.Selectimg);
        logo=view.findViewById(R.id.img);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog=new Dialog(getContext());
        View Dview=getLayoutInflater().inflate(R.layout.otp_dialog,null);
        dialog.setContentView(Dview);
        ResendOTP=Dview.findViewById(R.id.ResendOTP);
        CDbtn=Dview.findViewById(R.id.CloseDialog);
        OTP=Dview.findViewById(R.id.Rotp);
        SOTP=Dview.findViewById(R.id.RotpBTN);
        CountDownTimer startTimer = new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {

                long sec = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));


                ResendOTP.setText(String.valueOf(sec));
                if (sec == 1) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ResendOTP.setText("( 00 SEC )");
                        }
                    }, 1000);
                }


            }

            public void onFinish() {
                ResendOTP.setText("Resend OTP");
            }
        }.start();
        CDbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setimg();
            }
        });
        ResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(codePicker.getFullNumberWithPlus());
            }
        });

        LRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////////////
                checkemail();
                AuthCredential credential = EmailAuthProvider.getCredential(LEmail.getText().toString(), LNpin.getText().toString());
                SignUpwithEmail(credential);

              /*  sendVerificationCode(codePicker.getFullNumberWithPlus());
                progressBar.setVisibility(View.VISIBLE);
                //////////////////////////




                SOTP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification_id, OTP.getText().toString());
                       signInWithCredential(credential);

                    }
                });

               // Intent i=new Intent(getContext(), MainActivity.class);
                //startActivity(i);*/

            }
        });
        LAlreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragement(new LogInFragment());
            }
        });

        ////////////////////////////////////////////////////////////////////////////////

        LEmail.addTextChangedListener(new TextWatcher() {
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
        LName.addTextChangedListener(new TextWatcher() {
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
        Lid.addTextChangedListener(new TextWatcher() {
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
        LMoNo.addTextChangedListener(new TextWatcher() {
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
        LNpin.addTextChangedListener(new TextWatcher() {
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

        ////////////////////////////////////////////////////////////////////////////////

    }
    private void setFragement(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void checkinputs(){
        if (!TextUtils.isEmpty(LEmail.getText())){
            if (!TextUtils.isEmpty(LName.getText())){
                //
                if (!TextUtils.isEmpty(Lid.getText()) && Lid.length() ==6){
                    LRegister.setEnabled(true);
                    LRegister.setTextColor(Color.rgb(255,255,255));
                    if (!TextUtils.isEmpty(codePicker.getFullNumberWithPlus().toString()) && codePicker.getFullNumberWithPlus().toString().length() == 13){
                        LRegister.setEnabled(true);
                        LRegister.setTextColor(Color.rgb(255,255,255));

                        if (!TextUtils.isEmpty(LNpin.getText()) && LNpin.length() == 6){
                            LRegister.setEnabled(true);
                            LRegister.setTextColor(Color.rgb(255,255,255));

                        }else{
                            LRegister.setEnabled(false);
                            LRegister.setTextColor(Color.argb(50,255,255,255));
                            progressBar.setVisibility(View.GONE);

                        }

                    }else{
                        LRegister.setEnabled(false);
                        LRegister.setTextColor(Color.argb(50,255,255,255));
                        progressBar.setVisibility(View.GONE);

                    }

                }else{
                    LRegister.setEnabled(false);
                    LRegister.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);

                }
                //
            }else {
                LRegister.setEnabled(false);
                LRegister.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);

            }

        }else {
            LRegister.setEnabled(false);
            LRegister.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);

        }
    }
    private void checkemail(){

        if (LName.getText().toString().matches("^[a-z,A-Z\\s]*$")) {
            if (LEmail.getText().toString().matches(emailPattern)) {
                LRegister.setEnabled(true);
                LRegister.setTextColor(Color.argb(50, 255, 255, 255));
                LRegister.setError(null);

            } else {
                LRegister.setError("Invalid Email");
                Toast.makeText(getContext(),"Invalid Email!",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        }else{
            Toast.makeText(getContext(),"Enter Valid Name!!!",Toast.LENGTH_LONG).show();progressBar.setVisibility(View.GONE);
        }

    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user=task.getResult().getUser();
                            AuthCredential credential = EmailAuthProvider.getCredential(LEmail.getText().toString(), LNpin.getText().toString());
                            SignUpwithEmail(credential);


                        } else {
                            OTP.setText(null);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void SignUpwithEmail(AuthCredential authCredential){
       firebaseAuth.createUserWithEmailAndPassword(LEmail.getText().toString(), LNpin.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            uploadImageToFirebase(Imagename,Imageuri);
                            LIBRARYID=Lid.getText().toString();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });/*
        firebaseAuth.getCurrentUser().linkWithCredential(authCredential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            uploadImageToFirebase(Imagename,Imageuri);
                            Toast.makeText(getActivity(), user.getUid()
                                    , Toast.LENGTH_SHORT).show();
                           // updateUI(user);
                        } else {

                            Toast.makeText(getContext(), task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }
                    }
                });*/
    }
    private void SetUserData(String img){
        Map<Object, String> userdata = new HashMap<>();
        userdata.put("LibraryName", LName.getText().toString());
        userdata.put("MobileNumber",codePicker.getFullNumberWithPlus());
        userdata.put("EmailID", LEmail.getText().toString());
        userdata.put("LibraryID", Lid.getText().toString());
        userdata.put("PIN", LNpin.getText().toString());
        userdata.put("user", "1");
        userdata.put("totalbooks", "0");
        userdata.put("logo", img);
        userdata.put("borrowedbooks", "0");
        userdata.put("remainingbooks", "0");
        userdata.put("totalstudents", "0");

        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .set(userdata)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        } else {
                            LRegister.setEnabled(true);
                            LRegister.setTextColor(Color.rgb(255, 255, 255));
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseAuth.signOut();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        Map<Object, String> userdata2 = new HashMap<>();
        userdata2.put("LibraryName", LName.getText().toString());
        userdata2.put("MobileNumber",codePicker.getFullNumberWithPlus());
        userdata2.put("EmailID", LEmail.getText().toString());
        userdata2.put("LibraryID", Lid.getText().toString());
        userdata2.put("PIN", LNpin.getText().toString());

        firebaseFirestore.collection("USERIDs").document(Lid.getText().toString())
                .set(userdata2)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent main = new Intent(getActivity(), MainActivity.class);
                            progressBar.setVisibility(View.GONE);
                            startActivity(main);
                            getActivity().finish();

                        } else {
                            LRegister.setEnabled(true);
                            LRegister.setTextColor(Color.rgb(255, 255, 255));
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                firebaseAuth.signOut();
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void setimg(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.book_img_dialog, null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        ImageView imageViewADPPCamera = dialogView.findViewById(R.id.imageViewADPPCamera);
        ImageView imageViewADPPGallery = dialogView.findViewById(R.id.imageViewADPPGallery);

        final AlertDialog alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();

        imageViewADPPCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermissions();
                alertDialogProfilePicture.cancel();

            }
        });

        imageViewADPPGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                alertDialogProfilePicture.cancel();
            }
        });
    }
    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.getActivity(),new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
            ActivityCompat.requestPermissions(this.getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
            ActivityCompat.requestPermissions(this.getActivity(),new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
        }else {
            dispatchTakePictureIntent();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else {
                Toast.makeText(getContext(), "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);
                Imagename=f.getName();
                Imageuri=contentUri;
                logo.setImageURI(Imageuri);
                Select.setText("Image Selected");
                Select.setBackgroundColor(Color.GREEN);




            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);

                Imagename=imageFileName;
                Imageuri=contentUri;
                logo.setImageURI(Imageuri);
                Select.setText("Image Selected");
                Select.setBackgroundColor(Color.GREEN);


            }

        }



    }
    private void uploadImageToFirebase(String name, Uri contentUri) {

        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressBar.setVisibility(View.GONE);
                        SetUserData(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {

                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.vip.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

            }
        }
    }


}