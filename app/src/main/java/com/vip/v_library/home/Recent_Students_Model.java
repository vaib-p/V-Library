package com.vip.v_library.home;

public class Recent_Students_Model {
    private String Simg;
    private String Sname;
    private String Sdept;
    private String Senno;

    public Recent_Students_Model(String simg, String sname, String sdept, String senno) {
        Simg = simg;
        Sname = sname;
        Sdept = sdept;
        Senno = senno;
    }

    public String getSimg() {
        return Simg;
    }

    public void setSimg(String simg) {
        Simg = simg;
    }

    public String getSname() {
        return Sname;
    }

    public void setSname(String sname) {
        Sname = sname;
    }

    public String getSdept() {
        return Sdept;
    }

    public void setSdept(String sdept) {
        Sdept = sdept;
    }

    public String getSenno() {
        return Senno;
    }

    public void setSenno(String senno) {
        Senno = senno;
    }
}
