package com.vip.v_library.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vip.v_library.R;

import java.util.List;

public class Recents_Students_Adapter extends RecyclerView.Adapter<Recents_Students_Adapter.ViewHolder> {
    private List<Recent_Students_Model> recentStudentsModelList;

    public Recents_Students_Adapter(List<Recent_Students_Model> recentStudentsModelList) {
        this.recentStudentsModelList = recentStudentsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recents_students_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(recentStudentsModelList.get(position).getSimg(),recentStudentsModelList.get(position).getSname(),
                recentStudentsModelList.get(position).getSdept(),recentStudentsModelList.get(position).getSenno());
    }


    @Override
    public int getItemCount() {
        return recentStudentsModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView simg;
        private TextView sname;
        private TextView sdept;
        private TextView senno;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            simg=itemView.findViewById(R.id.StudentImg);
            sname=itemView.findViewById(R.id.StudentName);
            sdept=itemView.findViewById(R.id.StudentDept);
            senno=itemView.findViewById(R.id.StudentEnNo);
        }
        private void setData(String  img,String name,String dept,String enno) {
            Glide.with(itemView.getContext()).load(img).into(simg);
            sname.setText(name);
            sdept.setText(dept);
            senno.setText(enno);
        }

    }


}
