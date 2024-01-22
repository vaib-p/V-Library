package com.vip.v_library.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vip.v_library.AllDue;
import com.vip.v_library.R;

import java.util.List;


public class HomePageAdapter extends RecyclerView.Adapter {
    private List<HomePageModel> homePageModelList;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case HomePageModel.LINFO_VIEW:
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.library_view,parent,false);
                return new LibraryIfoViewHolder(view);
            case HomePageModel.DASHBOARD_VIEW:
                View DView= LayoutInflater.from(parent.getContext()).inflate(R.layout.library_dashboard_layout,parent,false);
                return new DashBoardViewHolder(DView);
            case HomePageModel.DUE_RECYCLERVIEW:
                View DueRecyclerView= LayoutInflater.from(parent.getContext()).inflate(R.layout.due_books_layout,parent,false);
                return new DueViewHolder(DueRecyclerView);
            case HomePageModel.STUDENTS_RECYCLERVIEW:
                View SRecyclerView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_students_layout,parent,false);
                return new RecentStudentsViewHolder(SRecyclerView);
            case HomePageModel.BOOKS_RECYCLERVIEW:
                View BRecyclerView= LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_books_layout,parent,false);
                return new RecentBookViewHolder(BRecyclerView);

            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()){
            case HomePageModel.LINFO_VIEW:
                List<LInfoList> lInfoLists=homePageModelList.get(position).getlInfoLists();
                ((LibraryIfoViewHolder)holder).setLibraryInfo(lInfoLists);
                break;
            case HomePageModel.DASHBOARD_VIEW:
                List<LDashboardList> dashboardLists=homePageModelList.get(position).getlDashboardLists();
                ((DashBoardViewHolder)holder).setDashboard(dashboardLists);
                break;
            case HomePageModel.DUE_RECYCLERVIEW:
                List<BooksDueTodayModel> dueTodayModelList=homePageModelList.get(position).getDueTodayModelList();
                ((DueViewHolder)holder).setDueBooks(dueTodayModelList);
                break;
            case HomePageModel.STUDENTS_RECYCLERVIEW:
                List<Recent_Students_Model> recentStudentsModelList=homePageModelList.get(position).getRecentStudentsModelList();
                ((RecentStudentsViewHolder)holder).setStudents(recentStudentsModelList);
                break;
            case HomePageModel.BOOKS_RECYCLERVIEW:
                List<Recent_Students_Model> recent_books_models=homePageModelList.get(position).getRecentBooksModelList();
                ((RecentBookViewHolder)holder).setBooks(recent_books_models);
                break;

            default:
                return;
        }


    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.LINFO_VIEW;
            case 1:
                return HomePageModel.DASHBOARD_VIEW;
            case 2:
                return HomePageModel.DUE_RECYCLERVIEW;
            case 3:
                return HomePageModel.STUDENTS_RECYCLERVIEW;
            case 4:
                return HomePageModel.BOOKS_RECYCLERVIEW;
            default: return -1;

        }
    }


    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }


    private class LibraryIfoViewHolder extends RecyclerView.ViewHolder {
        private ImageView logo;
        private TextView name;
        private TextView code;
        public LibraryIfoViewHolder(View view) {
            super(view);
            logo=view.findViewById(R.id.LLogo);
            name=view.findViewById(R.id.LName);
            code=view.findViewById(R.id.LCode);


        }

        public void setLibraryInfo(List<LInfoList> lInfoLists) {
            Glide.with(itemView.getContext()).load(lInfoLists.get(1).getLogo()).into(logo);
            name.setText(lInfoLists.get(1).getName());
            code.setText(lInfoLists.get(1).getCode());
        }
    }

    private class DashBoardViewHolder extends RecyclerView.ViewHolder {

        private TextView total;
        private TextView borrowed;
        private TextView remaing;
        private LinearLayout bord;
        public DashBoardViewHolder(View dView) {
            super(dView);
            total=dView.findViewById(R.id.TotalBooks);
            borrowed=dView.findViewById(R.id.TotalBorrowed);
            remaing=dView.findViewById(R.id.TotalRemaing);
            bord=dView.findViewById(R.id.BLL);
            bord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), AllDue.class));
                }
            });
        }

        public void setDashboard(List<LDashboardList> dashboardLists) {
            total.setText(dashboardLists.get(1).getTotal());
            borrowed.setText(dashboardLists.get(1).getBorrowed());
            remaing.setText(dashboardLists.get(1).getRemaining());
        }
    }

    private class DueViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView duerecyclerView;
        private Button showall;
        public DueViewHolder(View dueRecyclerView) {
            super(dueRecyclerView);
            duerecyclerView=dueRecyclerView.findViewById(R.id.TodayDue2);
            showall=dueRecyclerView.findViewById(R.id.ShowAllbtn);
            showall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), AllDue.class));
                }
            });
            dueRecyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void setDueBooks(List<BooksDueTodayModel> dueTodayModelList) {
            LinearLayoutManager layoutManager1=new LinearLayoutManager(itemView.getContext());
            layoutManager1.setOrientation(RecyclerView.VERTICAL);
            BooksDueTodayAdapter booksDueTodayAdapter=new BooksDueTodayAdapter(dueTodayModelList);
            duerecyclerView.setLayoutManager(layoutManager1);
            duerecyclerView.setAdapter(booksDueTodayAdapter);
            booksDueTodayAdapter.notifyDataSetChanged();

        }
    }

    private class RecentStudentsViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        public RecentStudentsViewHolder(View sRecyclerView) {
            super(sRecyclerView);
            recyclerView=sRecyclerView.findViewById(R.id.SHOME);
        }

        public void setStudents(List<Recent_Students_Model> recentStudentsModelList) {
            Recents_Students_Adapter adapter=new Recents_Students_Adapter(recentStudentsModelList);
            LinearLayoutManager layoutManager=new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    private class RecentBookViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        public RecentBookViewHolder(View bRecyclerView) {
            super(bRecyclerView);
            recyclerView=bRecyclerView.findViewById(R.id.BHOME);
        }
        private void setBooks(List<Recent_Students_Model> list){
            Recents_Students_Adapter adapter=new Recents_Students_Adapter(list);
            LinearLayoutManager layoutManager=new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }
}
