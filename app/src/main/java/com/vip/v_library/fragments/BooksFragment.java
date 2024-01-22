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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.vip.v_library.R;
import com.vip.v_library.books.AddBooksActivity;
import com.vip.v_library.books.BooksAdapter;
import com.vip.v_library.books.BooksModel;

import java.util.ArrayList;
import java.util.List;


public class BooksFragment extends Fragment {
    private RecyclerView BooksRecyclerView;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth mauth=FirebaseAuth.getInstance();
    ////////////////////////////
    private CardView addBooks;
    private TextView text;
    private ConstraintLayout layout;
    public static BooksAdapter adapter;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_books, container, false);
        BooksRecyclerView=view.findViewById(R.id.Books);
        addBooks=view.findViewById(R.id.AddBooks);
        text=view.findViewById(R.id.empty);
        layout=view.findViewById(R.id.BOOKLAYOUT);
        searchView=view.findViewById(R.id.BsearchView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        List<BooksModel> booksModelList=new ArrayList<>();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        adapter=new BooksAdapter(booksModelList);
        BooksRecyclerView.setLayoutManager(layoutManager);
        BooksRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        db.collection("USERS").document(mauth.getUid()).collection("BOOKS").
                orderBy("index").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot:task.getResult()){
                        if (snapshot.exists()) {
                            text.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            booksModelList.add(new BooksModel(snapshot.get("Bimg").toString(), snapshot.get("Bid").toString()
                                    , snapshot.get("Bname").toString(), snapshot.get("BPname").toString()
                                    , snapshot.get("Bedition").toString(), snapshot.get("Bquatity").toString()
                                    , snapshot.get("Bprice").toString()));
                        }else {
                            layout.setVisibility(View.GONE);
                            text.setVisibility(View.VISIBLE);

                        }


                    }
                    adapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        ///////////////////////////////////////////
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), AddBooksActivity.class);
                startActivity(i);
            }
        });

        addBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), AddBooksActivity.class);
                startActivity(i);

            }
        }
        );
        ///////////////////////////////////////////

        ///////////////////////////////////////////

    }

}