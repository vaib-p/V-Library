package com.vip.v_library.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.v_library.QrScanner;
import com.vip.v_library.R;
import com.vip.v_library.home.BooksDueTodayModel;
import com.vip.v_library.home.HomePageAdapter;
import com.vip.v_library.home.HomePageModel;
import com.vip.v_library.home.LDashboardList;
import com.vip.v_library.home.LInfoList;
import com.vip.v_library.home.Recent_Students_Model;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<HomePageModel> homePageModelList=new ArrayList<>();
    private HomePageAdapter adapter=new HomePageAdapter(homePageModelList);;
    private List<Recent_Students_Model> students_modelList=new ArrayList<>();;
    private List<Recent_Students_Model> bookmodellist=new ArrayList<>();;

    private List<BooksDueTodayModel> dueTodayModels=new ArrayList<>();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth mauth=FirebaseAuth.getInstance();

    private CardView SCAN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.HOME);
        SCAN=view.findViewById(R.id.ScanBTN);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<LInfoList> lInfoLists=new ArrayList<>();
        List<LDashboardList> dashboardLists=new ArrayList<>();
        homePageModelList.add(new HomePageModel(0,5,"",lInfoLists));
        homePageModelList.add(new HomePageModel(1,5,6,dashboardLists));
        FirebaseUser user=mauth.getCurrentUser();
        SCAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), QrScanner.class));
            }
        });
        db.collection("USERS").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();


                            lInfoLists.add(new LInfoList(documentSnapshot.get("logo").toString(),documentSnapshot.get("LibraryName").toString(),documentSnapshot.get("PIN").toString()));
                            lInfoLists.add(new LInfoList(documentSnapshot.get("logo").toString(),documentSnapshot.get("LibraryName").toString(),documentSnapshot.get("PIN").toString()));

                            int i= Integer.parseInt(documentSnapshot.get("borrowedbooks").toString());
                            int j= Integer.parseInt(documentSnapshot.get("totalbooks").toString());
                            int add=j-i;
                            dashboardLists.add(new LDashboardList(documentSnapshot.get("totalbooks").toString(),String.valueOf(add),documentSnapshot.get("borrowedbooks").toString()));
                            dashboardLists.add(new LDashboardList(documentSnapshot.get("totalbooks").toString(),String.valueOf(add),documentSnapshot.get("borrowedbooks").toString()));





                            LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        ///////////////////////////////////Due Books////////////////////////////////////////////
        db.collection("USERS").document(user.getUid()).collection("BORROWDATA").orderBy("index").limit(6)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot snapshot:task.getResult()){
                        if (snapshot.exists()){
                            dueTodayModels.add(new BooksDueTodayModel(snapshot.get("img").toString(),snapshot.get("name").toString(),snapshot.get("mono").toString(), snapshot.get("book").toString(),snapshot.get("return").toString(),snapshot.get("eno").toString()));
                        }

                    }
                    if (dueTodayModels.size()>0){
                        homePageModelList.add(new HomePageModel(2,dueTodayModels));

                    }else {
                        homePageModelList.remove(dueTodayModels);

                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        db.collection("USERS").document(mauth.getUid()).collection("STUDENTS").orderBy("index", Query.Direction.DESCENDING)
                .limit(4).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot:task.getResult()){
                        if (snapshot.exists()){
                            students_modelList.add(new Recent_Students_Model(snapshot.get("Simg").toString(),snapshot.get("Sname").toString(),snapshot.get("dept").toString(),snapshot.get("EnNO").toString()));

                        }

                    }
                    if (students_modelList.size()>0){
                        homePageModelList.add(new HomePageModel(3,students_modelList,3));
                    }else {
                        homePageModelList.remove(students_modelList);
                    }
                    adapter.notifyDataSetChanged();

                }
            }
        });



        db.collection("USERS").document(mauth.getUid()).collection("BOOKS").orderBy("index", Query.Direction.DESCENDING)
                .limit(4).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot:task.getResult()){
                        if (snapshot.exists()){
                            bookmodellist.add(new Recent_Students_Model(snapshot.get("Bimg").toString(),snapshot.get("Bname").toString(),snapshot.get("Bedition").toString()+"\nEDITION","Rs."+snapshot.get("Bprice").toString()));

                        }

                    }
                    if (bookmodellist.size()>0){
                        homePageModelList.add(new HomePageModel(4,5,bookmodellist));
                    }else {
                        homePageModelList.remove(bookmodellist);

                    }
                    adapter.notifyDataSetChanged();

                }
            }
        });
        ///////////////////////////////////////////////////////////////////////////////


    }



}