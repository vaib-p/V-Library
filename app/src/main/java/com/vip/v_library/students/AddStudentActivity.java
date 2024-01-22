package com.vip.v_library.students;

import android.Manifest;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vip.v_library.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddStudentActivity extends AppCompatActivity  {

    Spinner Sclass;
    Spinner SPyear;
    Spinner SDept;

    private EditText name;
    private EditText EnNo;
    private EditText MoNo;
    private Button selectimg;
    private Button submit;
    private ImageView Simg;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    String currentPhotoPath;
    String dept;
    String year;
    String claas;
    int c;

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    String Imagename;
    Uri Imageuri;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        Toolbar toolbar=findViewById(R.id.toolbar22);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ///////////////////////////SPINNERS//////////////////////////////////////////////////////
        Sclass=(Spinner)findViewById(R.id.ASclass);
        SPyear=(Spinner)findViewById(R.id.ASPyear);
        SDept=(Spinner)findViewById(R.id.ASDept);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.classs, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> PyeararrayAdapter=ArrayAdapter.createFromResource(this,R.array.purshingyears, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> DeptarrayAdapter=ArrayAdapter.createFromResource(this,R.array.dept, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PyeararrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DeptarrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sclass.setAdapter(arrayAdapter);
        SPyear.setAdapter(PyeararrayAdapter);
        SDept.setAdapter(DeptarrayAdapter);
        SPyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String a=parent.getItemAtPosition(position).toString();
                year=a;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String a=parent.getItemAtPosition(position).toString();
                dept=a;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Sclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String a=parent.getItemAtPosition(position).toString();
                claas=a;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ///////////////////////////SPINNERS//////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////////////////////
        name=findViewById(R.id.ASName);
        EnNo=findViewById(R.id.ASEnNo);
        MoNo=findViewById(R.id.ASMoNo);
        selectimg=findViewById(R.id.SelectIMG);
        submit=findViewById(R.id.ASSbtn);
        Simg=findViewById(R.id.Simg);
        progressBar=findViewById(R.id.ASprogressbar);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(name.getText())){
                    submit.setEnabled(true);
                    submit.setTextColor(Color.rgb(255,255,255));
                }else {
                    submit.setEnabled(false);
                    submit.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);
                    name.setError("please enter name");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        EnNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(EnNo.getText())&& EnNo.length()==10){
                    submit.setEnabled(true);
                    submit.setTextColor(Color.rgb(255,255,255));

                }else {
                    submit.setEnabled(false);
                    submit.setTextColor(Color.argb(50,255,255,255));
                    EnNo.setError("please enter valid EnrollMent Number");
                    progressBar.setVisibility(View.GONE);



                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        MoNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(MoNo.getText())&& MoNo.length()==10){
                    submit.setEnabled(true);
                    submit.setTextColor(Color.rgb(255,255,255));


                }else{
                    submit.setEnabled(false);
                    submit.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);
                    MoNo.setError("please enter valid Mobile Number");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBookimg();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Imagename!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.animate();
                    uploadImageToFirebase(Imagename,Imageuri);
                }else {

                    Toast.makeText(AddStudentActivity.this, "Please Select Image!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////

        storageReference = FirebaseStorage.getInstance().getReference();
        //////////////////////////////////ADD BOOKS//////////////////////////////////////
        firebaseFirestore.collection("USERS").document(auth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot db=task.getResult();
                            c= Integer.parseInt(db.get("totalstudents").toString());
                        }

                    }
                });
        //////////////////////////////////////////////////////////////////////////////////

    }






    ///////////////////////////////////////////////////////////////////////////
    private void setData(String img){
        Map<String,Object> data = new HashMap<>();
        String a= String.valueOf(c+1);
        data.put("index",c+1);
        data.put("Sname",name.getText().toString());
        data.put("EnNO",EnNo.getText().toString());
        data.put("MoNo",MoNo.getText().toString());
        data.put("dept",dept);
        data.put("Simg",img);
        data.put("Pyear",year);
        data.put("Sclass",claas);
        firebaseFirestore.collection("USERS").document(auth.getUid()).collection("STUDENTS").document(EnNo.getText().toString())
                .set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    DocumentReference documentReference=firebaseFirestore.collection("USERS").document(auth.getUid());
                    documentReference.update("totalstudents",a);
                    progressBar.animate().cancel();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddStudentActivity.this, "STUDENT ADDED SUCCESFULLY!!!", Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(AddStudentActivity.this, "something went wrong? Try Again?", Toast.LENGTH_SHORT).show();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddStudentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void setBookimg(){

        AlertDialog.Builder builder = new AlertDialog.Builder(AddStudentActivity.this);
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
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
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
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                Simg.setImageURI(Imageuri);
                selectimg.setText("Image Selected");
                selectimg.setBackgroundColor(Color.GREEN);




            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);

                Imagename=imageFileName;
                Imageuri=contentUri;
                Simg.setImageURI(Imageuri);
                selectimg.setText("Image Selected");
                selectimg.setBackgroundColor(Color.GREEN);


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
                        setData(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddStudentActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddStudentActivity.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
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
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {

                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(AddStudentActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.vip.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

            }
        }
    }
///////////////////////////////////////////////////////////////////////////////////////
}