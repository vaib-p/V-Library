package com.vip.v_library.home;

public class LInfoList {
    private String logo;
    private String name;
    private String code;

    public LInfoList(String logo, String name, String code) {
        this.logo = logo;
        this.name = name;
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
