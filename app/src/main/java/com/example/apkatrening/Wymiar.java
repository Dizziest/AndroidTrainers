package com.example.apkatrening;

public class Wymiar {

    private int id;
    private double waga;
    private int wzrost;
    private double obw_biceps;
    private double obw_klatka;
    private String data;

    public Wymiar(double waga, int wzrost, double obw_biceps, double obw_klatka, String data, int id) {
        this.id = id;
        this.waga = waga;
        this.wzrost = wzrost;
        this.obw_biceps = obw_biceps;
        this.obw_klatka = obw_klatka;
        this.data = data;
    }

    public double getWaga() {
        return waga;
    }

    public int getWzrost() {
        return wzrost;
    }

    public double getObw_biceps() {
        return obw_biceps;
    }

    public double getObw_klatka() {
        return obw_klatka;
    }

    public String getData() {
        return data;
    }

    public int getId(){ return id; }
}
