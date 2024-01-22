package com.vip.v_library.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.v_library.R;
import com.vip.v_library.students.AddStudentActivity;
import com.vip.v_library.students.StudentsAdapter;
import com.vip.v_library.students.StudentsModel;

import java.util.ArrayList;
import java.util.List;

public class StudentsFragment extends Fragment {

    private RecyclerView StudentRecyclerView;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth mauth=FirebaseAuth.getInstance();
    private CardView ADD;
    private ConstraintLayout layout;
    private TextView textView;

    private SearchView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_students, container, false);
        StudentRecyclerView=view.findViewById(R.id.Students);
        ADD=view.findViewById(R.id.AddStudents);
        layout=view.findViewById(R.id.STUDENTLAYOUT);
        textView=view.findViewById(R.id.sempty);
        searchView=view.findViewById(R.id.StudentsearchView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser user=mauth.getCurrentUser();
        ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddStudentActivity.class));
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddStudentActivity.class));
            }
        });

        List<StudentsModel> list=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        StudentsAdapter adapter=new StudentsAdapter(list);
        StudentRecyclerView.setLayoutManager(layoutManager);
        StudentRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ///////////////////////Search/////////////////////
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(getContext(), "closed", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();

                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText.toString());
                return true;
            }

        });
        ///////////////////////////////////////////
        ///////////////////////////////////////////////////////


        db.collection("USERS").document(mauth.getUid()).collection("STUDENTS").
                orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot:task.getResult()){
                        if (snapshot.exists()) {
                            layout.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.GONE);
                            list.add(new StudentsModel(snapshot.get("Simg").toString(), snapshot.get("EnNO").toString()
                                    , snapshot.get("Sname").toString(), snapshot.get("MoNo").toString()
                                    , snapshot.get("dept").toString(), snapshot.get("Sclass").toString(), snapshot.get("Pyear").toString()));
                        }else {
                            layout.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);


                        }




                    }
                    adapter.notifyDataSetChanged();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ///////////////////////////////////////////////////////

    }

}