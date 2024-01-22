package com.vip.v_library.books;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import androidmads.library.qrgenearator.QRGEncoder;

public class AddBooksActivity extends AppCompatActivity {
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private ImageView logo;
    private TextView name;
    private TextView id;
    private TextView Pname;
    private TextView edition;
    private Button SelectImg;
    private TextView price;
    private TextView quantity;
    private Button addbook;
    private String bookimg;
    private ProgressBar progressBar;
    private int c;
    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;

    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    String currentPhotoPath;
    public static final int RESULT_CROP = 100;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int STORAGWCODE = 101;
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
        setContentView(R.layout.activity_add_books);

        Toolbar toolbar=findViewById(R.id.toolbar22);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        logo=findViewById(R.id.ABLogo);
        name=findViewById(R.id.ABName);
        id=findViewById(R.id.ABId);
        Pname=findViewById(R.id.ABPName);
        edition=findViewById(R.id.ABEdition);
        SelectImg=findViewById(R.id.SelectIMG);
        price=findViewById(R.id.ABPrice);
        quantity=findViewById(R.id.ABQuantity);
        addbook=findViewById(R.id.ASSbtn);
        progressBar=findViewById(R.id.ASprogressbar);
        progressBar.setVisibility(View.GONE);
        addbook.setEnabled(true);
        addbook.setTextColor(Color.rgb(255,255,255));

        storageReference = FirebaseStorage.getInstance().getReference();
        //////////////////////////////////ADD BOOKS//////////////////////////////////////
        firebaseFirestore.collection("USERS").document(auth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot db=task.getResult();
                            c= Integer.parseInt(db.get("totalbooks").toString());
                        }

                    }
                });
        //////////////////////////////////////////////////////////////////////////////////
        SelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBookimg();
            }
        });
        addbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Imagename!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.animate();
                    uploadImageToFirebase(Imagename,Imageuri);
                }else {

                    Toast.makeText(AddBooksActivity.this, "Please Select Book Image!!!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        //////////////////////////////////ADD BOOKS////////////////////////////////////
        
        
        //////////////////////////////////////Text Watcher////////////////////////////////////////
        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(id.getText()) && id.length()==6){

                    //
                }else {
                    addbook.setEnabled(false);
                    addbook.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);
                    id.setError("id must be 6 digit!!!");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(name.getText())){


                }else {
                    addbook.setEnabled(false);
                    addbook.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Pname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(Pname.getText())){
                    addbook.setEnabled(true);
                    addbook.setTextColor(Color.rgb(255,255,255));


                }else{
                    addbook.setEnabled(false);
                    addbook.setTextColor(Color.argb(50,255,255,255));progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(edition.getText()) && edition.length()==4){
                    addbook.setEnabled(true);
                    addbook.setTextColor(Color.rgb(255,255,255));



                }else{
                    addbook.setEnabled(false);
                    addbook.setTextColor(Color.argb(50,255,255,255));
                    progressBar.setVisibility(View.GONE);
                    edition.setError("enter valid edition");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(price.getText())){
                    addbook.setEnabled(true);
                    addbook.setTextColor(Color.rgb(255,255,255));


                }else{
                    addbook.setEnabled(false);
                    addbook.setTextColor(Color.argb(50,255,255,255));
                    progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(quantity.getText()) && quantity.length()<=2){
                    addbook.setEnabled(true);
                    addbook.setTextColor(Color.rgb(255,255,255));

                }else{
                    addbook.setEnabled(false);
                    addbook.setTextColor(Color.argb(50,255,255,255));
                    progressBar.setVisibility(View.GONE);
                    quantity.setError("quantity not more than 100");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        //////////////////////////////////////Text Watcher////////////////////////////////////////

    }






    
    ///////////////////////////////////////////////////////////////////////////
    private void setData(String img){
        Map<String,Object> bookdata = new HashMap<>();
        String a= String.valueOf(c+1);
        bookdata.put("index",c+1);
        bookdata.put("Bname",name.getText().toString());
        bookdata.put("Bid",id.getText().toString());
        bookdata.put("BPname",Pname.getText().toString());
        bookdata.put("Bedition",edition.getText().toString());
        bookdata.put("Bimg",img);
        bookdata.put("Bprice",price.getText().toString());
        bookdata.put("Bquatity",quantity.getText().toString());
        firebaseFirestore.collection("USERS").document(auth.getUid()).collection("BOOKS").document(id.getText().toString())
                .set(bookdata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    DocumentReference documentReference=firebaseFirestore.collection("USERS").document(auth.getUid());
                    documentReference.update("totalbooks",a);
                    progressBar.animate().cancel();
                    progressBar.setVisibility(View.GONE);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(AddBooksActivity.this, "something went wrong? Try Again?", Toast.LENGTH_SHORT).show();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddBooksActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void setBookimg(){

        AlertDialog.Builder builder = new AlertDialog.Builder(AddBooksActivity.this);
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
                Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                alertDialogProfilePicture.cancel();
            }
        });
    }
    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,}, CAMERA_PERM_CODE);

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




            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);


                Imagename=imageFileName;
                Imageuri=contentUri;
                SelectImg.setText("Image Selected");
                SelectImg.setBackgroundColor(Color.GREEN);


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
                        Toast.makeText(AddBooksActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddBooksActivity.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddBooksActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

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


