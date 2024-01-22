package com.vip.v_library;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView scannerView;
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


    }

    @Override
    public void handleResult(Result rawResult) {
        String data = rawResult.getText().toString();
        if (data!="" && data.length()==6) {
            firebaseFirestore.collection("USERS").document(auth.getUid())
                    .collection("BOOKS").document(data).get().addOnCompleteListener(
                    new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot snapshot=task.getResult();
                                if (snapshot.exists()){
                                    Intent borrow=new Intent(QrScanner.this, BookBorrowing.class);
                                    borrow.putExtra("id",snapshot.get("Bid").toString());
                                    borrow.putExtra("img",snapshot.get("Bimg").toString());
                                    borrow.putExtra("name",snapshot.get("Bname").toString());
                                    borrow.putExtra("edition",snapshot.get("Bedition").toString());
                                    borrow.putExtra("publisher",snapshot.get("BPname").toString());
                                    startActivity(borrow);

                                }else {
                                    Toast.makeText(QrScanner.this, "Book Not Found!!!Not Valid", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            }
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(QrScanner.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }
            });
        }else {
            Toast.makeText(this, "Invalid Input QR/Barcode", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}