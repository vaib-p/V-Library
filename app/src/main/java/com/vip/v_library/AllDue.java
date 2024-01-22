package com.vip.v_library;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.v_library.home.BooksDueTodayAdapter;
import com.vip.v_library.home.BooksDueTodayModel;

import java.util.ArrayList;
import java.util.List;

public class AllDue extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BooksDueTodayAdapter adapter;
    private List<BooksDueTodayModel> dueTodayModels;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth mauth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_due);
        recyclerView=findViewById(R.id.AllDueRV);
        dueTodayModels=new ArrayList<>();
        adapter=new BooksDueTodayAdapter(dueTodayModels);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        db.collection("USERS").document(mauth.getUid()).collection("BORROWDATA").orderBy("index")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot:task.getResult()){
                        if (snapshot.exists()){
                            dueTodayModels.add(new BooksDueTodayModel(snapshot.get("img").toString(),snapshot.get("name").toString(),snapshot.get("mono").toString(), snapshot.get("book").toString(),snapshot.get("return").toString(),snapshot.get("eno").toString()));;
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}