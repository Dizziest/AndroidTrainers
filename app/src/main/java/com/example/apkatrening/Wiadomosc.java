package com.example.apkatrening;

public class Wiadomosc {

    String login;
    int id_uzytkownik;
    String tresc;
    String data;

    public Wiadomosc(String login, int id_uzytkownik, String tresc, String data) {
        this.login = login;
        this.id_uzytkownik = id_uzytkownik;
        this.tresc = tresc;
        this.data = data;
    }

    public String getLogin() {
        return login;
    }

    public int getId_uzytkownik() {
        return id_uzytkownik;
    }

    public String getTresc() {
        return tresc;
    }

    public String getData() {
        return data;
    }
}
