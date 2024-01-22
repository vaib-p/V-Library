package com.vip.v_library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread thread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (firebaseAuth.getCurrentUser()==null) {
                        Intent mainintent = new Intent(SplashScreenActivity.this, AuthActivity.class);
                        startActivity(mainintent);
                        finish();
                    }else {
                        startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                        finish();
                    }


                }
            }
        };
        thread.start();
    }
}