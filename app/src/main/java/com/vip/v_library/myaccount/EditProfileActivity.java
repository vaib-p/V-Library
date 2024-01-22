package com.vip.v_library.myaccount;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.vip.v_library.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {
    private EditText LName;
    private EditText Lid;
    private EditText LEmail;
    private EditText LMoNo;
    private EditText PASSWORD;
    private Button UpdateBtn;


    private CountryCodePicker codePicker;

    private Button Select;
    private ImageView logo;

    private ImageView CDbtn;
    private String verification_id;
    private Dialog dialog;
    private ProgressBar progressBar;


    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String Imagename;
    String LOGO;
    Uri Imageuri;

    String currentPhotoPath;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        LName=findViewById(R.id.RLibraryName);
        Lid=findViewById(R.id.RLibraryCode);
        LEmail=findViewById(R.id.REmail);
        LMoNo=findViewById(R.id.RMobileNo);
        UpdateBtn=findViewById(R.id.Update);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        progressBar.animate();
        PASSWORD=findViewById(R.id.Password);
        codePicker=findViewById(R.id.CcodePicker);
        codePicker.registerCarrierNumberEditText(LMoNo);
        storageReference = FirebaseStorage.getInstance().getReference();
        Select=findViewById(R.id.Selectimg);
        logo=findViewById(R.id.img);
        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot=task.getResult();
                    Lid.setText(snapshot.get("LibraryID").toString());
                    Lid.setEnabled(false);
                    LName.setText(snapshot.get("LibraryName").toString());
                    LEmail.setText(snapshot.get("EmailID").toString());
                    LMoNo.setText(snapshot.get("MobileNumber").toString());
                    LOGO=snapshot.get("logo").toString();
                    LMoNo.setEnabled(false);

                }
            }
        });
        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setimg();
            }
        });

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////////////
                checkemail();
                progressBar.setVisibility(View.VISIBLE);
                //////////////////////////





                // Intent i=new Intent(getContext(), MainActivity.class);
                //startActivity(i);*/

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
    }
    private void checkinputs(){
        if (!TextUtils.isEmpty(LEmail.getText())){
            if (!TextUtils.isEmpty(LName.getText())){
                //
                if (!TextUtils.isEmpty(Lid.getText()) && Lid.length() ==6){
                    UpdateBtn.setEnabled(true);
                    UpdateBtn.setTextColor(Color.rgb(255,255,255));
                    if (!TextUtils.isEmpty(codePicker.getFullNumberWithPlus().toString()) && codePicker.getFullNumberWithPlus().toString().length() == 13){
                        UpdateBtn.setEnabled(true);
                        UpdateBtn.setTextColor(Color.rgb(255,255,255));



                    }else{
                        UpdateBtn.setEnabled(false);
                        UpdateBtn.setTextColor(Color.argb(50,255,255,255));
                        progressBar.setVisibility(View.GONE);

                    }

                }else{
                    UpdateBtn.setEnabled(false);
                    UpdateBtn.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);

                }
                //
            }else {
                UpdateBtn.setEnabled(false);
                UpdateBtn.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);

            }

        }else {
            UpdateBtn.setEnabled(false);
            UpdateBtn.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);

        }
    }
    private void checkemail(){

        if (LName.getText().toString().matches("^[a-z,A-Z\\s]*$")) {
            if (LEmail.getText().toString().matches(emailPattern)) {
                UpdateBtn.setEnabled(true);
                UpdateBtn.setTextColor(Color.argb(50, 255, 255, 255));
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), PASSWORD.getText().toString());

// Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                uploadImageToFirebase(Imagename,Imageuri);
                            }
                        });
                UpdateBtn.setError(null);

            } else {
                UpdateBtn.setError("Invalid Email");
                Toast.makeText(EditProfileActivity.this,"Invalid Email!",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        }else{
            Toast.makeText(EditProfileActivity.this,"Enter Valid Name!!!",Toast.LENGTH_LONG).show();progressBar.setVisibility(View.GONE);
        }

    }

    private void SetUserData(String img){

        firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                .update(
                        "LibraryName", LName.getText().toString(),
                        "EmailID", LEmail.getText().toString(),
                        "logo",LOGO
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    firebaseAuth.getCurrentUser().updateEmail(LEmail.getText().toString());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(EditProfileActivity.this, "Data Updated Succesfully!!1", Toast.LENGTH_SHORT).show();
                } else {
                    UpdateBtn.setEnabled(true);
                    UpdateBtn.setTextColor(Color.rgb(255, 255, 255));
                    String error = task.getException().getMessage();
                    Toast.makeText(EditProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebaseFirestore.collection("USERIDs").document(Lid.getText().toString())
                .update(
                        "LibraryName", LName.getText().toString(),
                        "EmailID", LEmail.getText().toString(),
                        "logo",LOGO
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    onBackPressed();
                } else {
                    UpdateBtn.setEnabled(true);
                    UpdateBtn.setTextColor(Color.rgb(255, 255, 255));
                    String error = task.getException().getMessage();
                    Toast.makeText(EditProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });





    }
    private void setimg(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditProfileActivity.this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
            ActivityCompat.requestPermissions(EditProfileActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
            ActivityCompat.requestPermissions(EditProfileActivity.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERM_CODE);
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
                Toast.makeText(EditProfileActivity.this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
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
                this.sendBroadcast(mediaScanIntent);
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
                        LOGO=uri.toString();
                        SetUserData(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfileActivity.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String getFileExt(Uri contentUri) {
        ContentResolver c = this.getContentResolver();
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
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {

                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(EditProfileActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(EditProfileActivity.this,
                        "com.vip.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

            }
        }
    }
}