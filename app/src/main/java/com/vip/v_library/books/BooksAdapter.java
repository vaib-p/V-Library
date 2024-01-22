package com.vip.v_library.books;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vip.v_library.BookBorrowing;
import com.vip.v_library.R;

import java.util.ArrayList;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> implements Filterable {
    private List<BooksModel> booksModelList;
    private List<BooksModel> list;

    public BooksAdapter(List<BooksModel> booksModelList) {
        this.booksModelList = booksModelList;
        this.list=booksModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.books_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setdata(booksModelList.get(position).getImg(),booksModelList.get(position).getName(),booksModelList.get(position).getPublisher()
        ,booksModelList.get(position).getEdition());

    }

    @Override
    public int getItemCount() {
        return booksModelList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchtext=constraint.toString();
                List<BooksModel> templist=new ArrayList<>();
                if (searchtext.isEmpty()){
                    templist.addAll(list);
                    booksModelList.addAll(list);
                    notifyDataSetChanged();
                }else {
                    for (BooksModel item:list){
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
                booksModelList=(List<BooksModel>)results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView Bimg;
        private TextView Bname;
        private TextView Bpublisher;
        private TextView Bedition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Bimg=itemView.findViewById(R.id.Bookimg);
            Bname=itemView.findViewById(R.id.BookName);
            Bpublisher=itemView.findViewById(R.id.BookPublisher);
            Bedition=itemView.findViewById(R.id.BookEdition);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent borrow=new Intent(itemView.getContext(), BookBorrowing.class);
                    borrow.putExtra("id",booksModelList.get(getAdapterPosition()).getId());
                    borrow.putExtra("img",booksModelList.get(getAdapterPosition()).getImg());
                    borrow.putExtra("name",booksModelList.get(getAdapterPosition()).getName());
                    borrow.putExtra("edition",booksModelList.get(getAdapterPosition()).getEdition());
                    borrow.putExtra("publisher",booksModelList.get(getAdapterPosition()).getPublisher());
                    itemView.getContext().startActivity(borrow);
                }
            });
        }
        private void setdata(String img,String name,String publisher,String edition){
            Glide.with(itemView.getContext()).load(img).into(Bimg);
            Bname.setText(name);
            Bpublisher.setText("Published By "+publisher);
            Bedition.setText(edition+"\nEDITION");
        }


    }
}
