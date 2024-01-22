package com.vip.v_library.students;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vip.v_library.R;

import java.util.ArrayList;
import java.util.List;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {
    private List<StudentsModel> studentsModelList;
    private List<StudentsModel> list;

    public StudentsAdapter(List<StudentsModel> studentsModelList) {
        this.studentsModelList = studentsModelList;
        this.list=studentsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.students_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.SetData(studentsModelList.get(position).getImg(),studentsModelList.get(position).getEnno()
        ,studentsModelList.get(position).getName(),studentsModelList.get(position).getMono(),studentsModelList.get(position).getDept());



    }


    @Override
    public int getItemCount() {
        return studentsModelList.size();
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchtext=constraint.toString();
                List<StudentsModel> templist=new ArrayList<>();
                if (searchtext.isEmpty()){
                    templist.addAll(list);
                    studentsModelList.addAll(list);
                    notifyDataSetChanged();
                }else {
                    for (StudentsModel item:list){
                        if (item.getName().toLowerCase().contains(searchtext.toLowerCase())){
                            templist.add(item);
                        }
                    }
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=templist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //booksModelList.clear();
                studentsModelList=(List<StudentsModel>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        private ImageView Simg;
        private TextView Senno;
        private TextView Sname;
        private TextView Smono;
        private TextView Sdept;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Simg=itemView.findViewById(R.id.Simg);
            Senno=itemView.findViewById(R.id.SEnNo);
            Sname=itemView.findViewById(R.id.SName);
            Smono=itemView.findViewById(R.id.Mono);
            Sdept=itemView.findViewById(R.id.SDept);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Intent i=new Intent();
                    i.putExtra("enno",studentsModelList.get(getAdapterPosition()).toString());*/

                }
            });

        }
        private void SetData(String img,String enno,String name,String mono,String dept){
            Glide.with(itemView.getContext()).load(img).into(Simg);
            Senno.setText(enno);
            Sname.setText(name);
            Smono.setText(mono);
            Sdept.setText(dept);

        }
    }
    
}
