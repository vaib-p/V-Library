package com.vip.v_library.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vip.v_library.AboutUs;
import com.vip.v_library.AuthActivity;
import com.vip.v_library.R;
import com.vip.v_library.auth.ForgotPassword;
import com.vip.v_library.myaccount.EditProfileActivity;
import com.vip.v_library.myaccount.MyLibraryInfoActivity;


public class MyAccountFragment extends Fragment {
    private String libraryID;
    private ImageView logo;
    private TextView libraryname;
    private CardView myinfo;
    private CardView editprofile;
    private CardView changepin;
    private CardView aboutus;
    private CardView logout;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    private String MoNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_account, container, false);
        logo=view.findViewById(R.id.Mlogo);
        libraryname=view.findViewById(R.id.MLname);
        myinfo=view.findViewById(R.id.MyInfo);
        aboutus=view.findViewById(R.id.AboutUs);
        editprofile=view.findViewById(R.id.EditProfile);
        changepin=view.findViewById(R.id.ChangePIN);
        logout=view.findViewById(R.id.LogOut);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent j=new Intent(getContext(), AuthActivity.class);
                startActivity(j);
                getActivity().finish();

            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(), AboutUs.class));
            }
        });
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditProfileActivity.class));
            }
        });
        myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MyLibraryInfoActivity.class));
            }
        });
        changepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), ForgotPassword.class);
                startActivity(i);
            }
        });

///////////////////////////////////////////////////////////////////////////////////////
        db.collection("USERS").document(auth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot=task.getResult();
                            String l=snapshot.get("logo").toString();
                            String n=snapshot.get("LibraryName").toString();
                            Glide.with(getActivity()).load(l).into(logo);
                            libraryname.setText(n);
                            MoNo=snapshot.get("MobileNumber").toString();
                            libraryID=snapshot.get("LibraryID").toString();
                            //number.setText(snapshot.get("MobileNumber").toString());
                            //number.setEnabled(false);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });





    }

}