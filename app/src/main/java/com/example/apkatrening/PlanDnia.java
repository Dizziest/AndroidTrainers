package com.example.apkatrening;

public class PlanDnia {
    int id;
    String date;

    public PlanDnia(int id, String date) {
        this.id = id;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
