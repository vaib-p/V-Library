package com.vip.v_library.home;

public class LDashboardList {
    private String total;
    private String remaining;
    private String borrowed;

    public LDashboardList(String total, String remaining, String borrowed) {
        this.total = total;
        this.remaining = remaining;
        this.borrowed = borrowed;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public String getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(String borrowed) {
        this.borrowed = borrowed;
    }
}
