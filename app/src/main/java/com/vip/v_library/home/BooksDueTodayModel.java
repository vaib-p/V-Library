package com.vip.v_library.home;

public class BooksDueTodayModel {

    private String Studentimg;
    private String StudentName;
    private String StudentMoNo;
    private String BorrowedBook;
    private String Due;
    private String EnNo;

    public BooksDueTodayModel(String studentimg, String studentName, String studentMoNo, String borrowedBook, String due, String enNo) {
        Studentimg = studentimg;
        StudentName = studentName;
        StudentMoNo = studentMoNo;
        BorrowedBook = borrowedBook;
        Due = due;
        EnNo = enNo;
    }

    public String getStudentimg() {
        return Studentimg;
    }

    public void setStudentimg(String studentimg) {
        Studentimg = studentimg;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public String getStudentMoNo() {
        return StudentMoNo;
    }

    public void setStudentMoNo(String studentMoNo) {
        StudentMoNo = studentMoNo;
    }

    public String getBorrowedBook() {
        return BorrowedBook;
    }

    public void setBorrowedBook(String borrowedBook) {
        BorrowedBook = borrowedBook;
    }

    public String getDue() {
        return Due;
    }

    public void setDue(String due) {
        Due = due;
    }

    public String getEnNo() {
        return EnNo;
    }

    public void setEnNo(String enNo) {
        EnNo = enNo;
    }
}
