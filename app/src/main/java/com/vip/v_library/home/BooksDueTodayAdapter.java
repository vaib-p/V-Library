package com.vip.v_library.home;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vip.v_library.R;

import java.util.List;

public class BooksDueTodayAdapter extends RecyclerView.Adapter<BooksDueTodayAdapter.ViewHolder> {
    private List<BooksDueTodayModel> dueTodayModelList;

    public BooksDueTodayAdapter(List<BooksDueTodayModel> dueTodayModelList) {
        this.dueTodayModelList = dueTodayModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.due_students_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.SetData(dueTodayModelList.get(position).getStudentimg(),dueTodayModelList.get(position).getStudentName()
        ,dueTodayModelList.get(position).getStudentMoNo(),dueTodayModelList.get(position).getBorrowedBook()
                ,dueTodayModelList.get(position).getDue());

    }

    @Override
    public int getItemCount() {
        return dueTodayModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView Simg;
        private TextView Sname;
        private TextView Smono;
        private TextView SBbook;
        private TextView Sdue;
        private FirebaseFirestore db;
        private FirebaseAuth auth;
        private int a;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Simg=itemView.findViewById(R.id.SDimg);
            Sname=itemView.findViewById(R.id.SDname);
            Smono=itemView.findViewById(R.id.SDmono);
            SBbook=itemView.findViewById(R.id.SDbookname);
            Sdue=itemView.findViewById(R.id.SDdue);

            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            View dialogView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.confirm_dialog, null);
            builder.setCancelable(false);
            builder.setView(dialogView);
            Button confirm=dialogView.findViewById(R.id.Confirm);
            Button cancel=dialogView.findViewById(R.id.Cancel);
            Button remind=dialogView.findViewById(R.id.Remind);
            final AlertDialog alertDialogProfilePicture = builder.create();


            remind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sendsms(dueTodayModelList.get(getAdapterPosition()).getStudentMoNo(),"Mr."+dueTodayModelList.get(getAdapterPosition()).getStudentName()+"\nBook Name:"+dueTodayModelList.get(getAdapterPosition()).getBorrowedBook()+"\nDue Date:"+
                            dueTodayModelList.get(getAdapterPosition()).getDue()+"\nThis is Reminder of Book Borrowed!!!\nPlease Return On Time!!");
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(itemView.getContext());
                    View dialogView2 = LayoutInflater.from(itemView.getContext()).inflate(R.layout.confirm_via_pin, null);
                    builder2.setCancelable(false);
                    builder2.setView(dialogView2);
                    EditText pin=dialogView2.findViewById(R.id.ConfirmPIN);
                    Button submit=dialogView2.findViewById(R.id.SubmitPIN);
                    final AlertDialog alertDialogProfilePicture2 = builder2.create();
                    alertDialogProfilePicture2.show();
                    db=FirebaseFirestore.getInstance();
                    auth=FirebaseAuth.getInstance();
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            db.collection("USERS").document(auth.getUid()).get().addOnCompleteListener(
                                    new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                alertDialogProfilePicture.cancel();
                                                DocumentSnapshot snapshot=task.getResult();
                                                int PIN= Integer.parseInt(snapshot.get("PIN").toString());
                                                int pin2= Integer.parseInt(pin.getText().toString());
                                                if (PIN==pin2){
                                                    alertDialogProfilePicture2.cancel();
                                                    Confirmed();
                                                }else {
                                                    Toast.makeText(itemView.getContext(), "Please Enter Correct PIN", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        }
                                    }
                            ).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    });

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogProfilePicture.cancel();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogProfilePicture.show();
                }
            });
        }
        private void SetData(String img,String name,String mono,String book,String due){
            Glide.with(itemView.getContext()).load(img).into(Simg);
            Sname.setText(name);
            Smono.setText(mono);
            SBbook.setText(book);
            Sdue.setText(due);

        }
        private void Confirmed(){
            db.collection("USERS").document(auth.getUid()).collection("BORROWDATA").
                    document(dueTodayModelList.get(getAdapterPosition()).getEnNo()).delete().addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                DocumentReference documentReference=db.collection("USERS").document(auth.getUid());
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot snapshot=task.getResult();
                                        int j= Integer.parseInt(snapshot.get("borrowedbooks").toString());
                                        int k=j-1;
                                        String c= String.valueOf(k);
                                        documentReference.update("borrowedbooks",c);

                                    }
                                });
                                Toast.makeText(itemView.getContext(), "Book Returned Succesfull!!!", Toast.LENGTH_SHORT).show();
                                dueTodayModelList.remove(getAdapterPosition());
                                notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(itemView.getContext(), task.getException().getMessage()+"Try Again?", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void Sendsms(String no,String msg){

            Intent intent=new Intent(itemView.getContext(),itemView.getClass());
            PendingIntent pi=PendingIntent.getActivity(itemView.getContext(), 0, intent,0);

            //Get the SmsManager instance and call the sendTextMessage method to send message
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(no, null, msg, pi,null);

            Toast.makeText(itemView.getContext(), "Book Reminded Succesfuly!!!",
                    Toast.LENGTH_LONG).show();

        }
    }
}
