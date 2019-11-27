package com.example.apkatrening;

public class Trener {
    private String imie;
    private String nazwisko;
    private String email;
    private int doswiadczenie;
    private String login;
    private int id;

    public Trener(String imie, String nazwisko, String email, int doswiadczenie, String login, int id) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.doswiadczenie = doswiadczenie;
        this.login = login;
        this.id = id;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public String getEmail() {
        return email;
    }

    public int getDoswiadczenie() {
        return doswiadczenie;
    }

    public String getLogin(){
        return login;
    }

    public int getId(){ return id; }
}
