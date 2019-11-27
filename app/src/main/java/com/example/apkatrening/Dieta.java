package com.example.apkatrening;

public class Dieta {
    int id_plan;
    int id_danie;
    String nazwa;
    int kalorycznosc;
    String opis;

    public Dieta(int id_plan, int id_danie, String nazwa, int kalorycznosc, String opis) {
        this.id_plan = id_plan;
        this.id_danie = id_danie;
        this.nazwa = nazwa;
        this.kalorycznosc = kalorycznosc;
        this.opis = opis;
    }

    public int getId_plan() {
        return id_plan;
    }

    public int getId_danie() {
        return id_danie;
    }

    public String getNazwa() {
        return nazwa;
    }

    public int getKalorycznosc() {
        return kalorycznosc;
    }

    public String getOpis() {
        return opis;
    }
}
