package com.example.apkatrening;

public class Trening {
    int id_plan;
    String nazwa;
    String opis;
    int id_cwiczenie;
    int serie;
    int powtorzenia;
    int obciazenie;

    public Trening(int id_plan, String nazwa, String opis, int id_cwiczenie, int serie, int powtorzenia, int obciazenie) {
        this.id_plan = id_plan;
        this.nazwa = nazwa;
        this.opis = opis;
        this.id_cwiczenie = id_cwiczenie;
        this.serie = serie;
        this.powtorzenia = powtorzenia;
        this.obciazenie = obciazenie;
    }

    public int getId_plan() {
        return id_plan;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public int getId_cwiczenie() {
        return id_cwiczenie;
    }

    public int getSerie() {
        return serie;
    }

    public int getPowtorzenia() {
        return powtorzenia;
    }

    public int getObciazenie() {
        return obciazenie;
    }
}
