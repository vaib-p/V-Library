package com.vip.v_library.home;

import java.util.List;

public class HomePageModel {

    public static final int LINFO_VIEW=0;
    public static final int DASHBOARD_VIEW=1;
    public static final int DUE_RECYCLERVIEW=2;
    public static final int STUDENTS_RECYCLERVIEW=3;
    public static final int BOOKS_RECYCLERVIEW=4;
    private int type;
    private int type2;
    private int color;
    private int l;
    private String d;


    private List<BooksDueTodayModel> dueTodayModelList;
    private List<Recent_Students_Model> recentStudentsModelList;
    private List<Recent_Students_Model> recentBooksModelList;
    private List<LInfoList> lInfoLists;
    private List<LDashboardList> lDashboardLists;

    public HomePageModel(int type, int l, String d, List<LInfoList> lInfoLists) {
        this.type = type;
        this.l = l;
        this.d = d;
        this.lInfoLists = lInfoLists;
    }

    public HomePageModel(int type, int color, int l, List<LDashboardList> lDashboardLists) {
        this.type = type;
        this.color = color;
        this.l = l;
        this.lDashboardLists = lDashboardLists;
    }

    public List<LInfoList> getlInfoLists() {
        return lInfoLists;
    }

    public void setlInfoLists(List<LInfoList> lInfoLists) {
        this.lInfoLists = lInfoLists;
    }

    public List<LDashboardList> getlDashboardLists() {
        return lDashboardLists;
    }

    public void setlDashboardLists(List<LDashboardList> lDashboardLists) {
        this.lDashboardLists = lDashboardLists;
    }

    public HomePageModel(int type, List<BooksDueTodayModel> dueTodayModelList) {
        this.type = type;
        this.dueTodayModelList = dueTodayModelList;
    }

    public HomePageModel(int type,List<Recent_Students_Model> recentStudentsModelList,int type2) {
        this.type = type;
        this.recentStudentsModelList = recentStudentsModelList;
    }

    public HomePageModel(int type, int color, List<Recent_Students_Model> recentBooksModelList) {
        this.type = type;
        this.color = color;
        this.recentBooksModelList = recentBooksModelList;
    }

    public static int getLinfoView() {
        return LINFO_VIEW;
    }

    public static int getDashboardView() {
        return DASHBOARD_VIEW;
    }

    public static int getDueRecyclerview() {
        return DUE_RECYCLERVIEW;
    }

    public static int getStudentsRecyclerview() {
        return STUDENTS_RECYCLERVIEW;
    }

    public static int getBooksRecyclerview() {
        return BOOKS_RECYCLERVIEW;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<BooksDueTodayModel> getDueTodayModelList() {
        return dueTodayModelList;
    }

    public void setDueTodayModelList(List<BooksDueTodayModel> dueTodayModelList) {
        this.dueTodayModelList = dueTodayModelList;
    }

    public List<Recent_Students_Model> getRecentStudentsModelList() {
        return recentStudentsModelList;
    }

    public void setRecentStudentsModelList(List<Recent_Students_Model> recentStudentsModelList) {
        this.recentStudentsModelList = recentStudentsModelList;
    }

    public List<Recent_Students_Model> getRecentBooksModelList() {
        return recentBooksModelList;
    }

    public void setRecentBooksModelList(List<Recent_Students_Model> recentBooksModelList) {
        this.recentBooksModelList = recentBooksModelList;
    }
}

