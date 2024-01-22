package com.vip.v_library.students;

public class StudentsModel {
    private String img;
    private String enno;
    private String name;
    private String mono;
    private String dept;
    private String clas;
    private String purshingyear;

    public StudentsModel(String img, String enno, String name, String mono, String dept, String clas, String purshingyear) {
        this.img = img;
        this.enno = enno;
        this.name = name;
        this.mono = mono;
        this.dept = dept;
        this.clas = clas;
        this.purshingyear = purshingyear;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEnno() {
        return enno;
    }

    public void setEnno(String enno) {
        this.enno = enno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMono() {
        return mono;
    }

    public void setMono(String mono) {
        this.mono = mono;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getPurshingyear() {
        return purshingyear;
    }

    public void setPurshingyear(String purshingyear) {
        this.purshingyear = purshingyear;
    }
}
