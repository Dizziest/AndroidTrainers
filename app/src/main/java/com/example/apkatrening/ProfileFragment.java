package com.example.apkatrening;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    private static final String ARG_LOGIN = "argLogin";
    private static final String ARG_EMAIL = "argEmail";
    private static final String ARG_IMIE = "argImie";
    private static final String ARG_NAZWISKO = "argNazwisko";
    private static final String ARG_WIEK = "argWiek";

    private String login;
    private String email;
    private String imie;
    private String nazwisko;
    private Integer wiek;

    public static ProfileFragment newInstance(String login, String email, String imie, String nazwisko, int wiek){
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN, login);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_IMIE, imie);
        args.putString(ARG_NAZWISKO, nazwisko);
        args.putInt(ARG_WIEK, wiek);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile,container,false);
        TextView textViewLogin = v.findViewById(R.id.textViewDane_login);
        TextView textViewEmail = v.findViewById(R.id.textViewDane_email);
        TextView textViewImie = v.findViewById(R.id.textViewDane_imie);
        TextView textViewNazwisko = v.findViewById(R.id.textViewDane_nazwisko);
        TextView textViewWiek = v.findViewById(R.id.textViewDane_wiek);
        Button buttonEditDane = v.findViewById(R.id.buttonEditDane);
        Button buttonChangePass = v.findViewById(R.id.buttonChangePassword);

        if (getArguments()!=null){
            login = getArguments().getString(ARG_LOGIN);
            email = getArguments().getString(ARG_EMAIL);
            imie = getArguments().getString(ARG_IMIE);
            nazwisko = getArguments().getString(ARG_NAZWISKO);
            wiek = getArguments().getInt(ARG_WIEK);
        }

        textViewLogin.setText("Login: " + login);
        textViewEmail.setText("Email: " + email);
        textViewImie.setText("Imię: " + imie);
        textViewNazwisko.setText("Nazwisko: " + nazwisko);

        if(nazwisko != null && !nazwisko.isEmpty()){
            textViewNazwisko.setText("Nazwisko: " + nazwisko);
        } else {
            textViewNazwisko.setText("Nazwisko: brak danych");
        }

        if (imie != null && !imie.isEmpty()){
            textViewImie.setText("Imię: " + imie );
        } else {
            textViewImie.setText("Imię: brak danych");
        }

        if (wiek != null && wiek != 0) {
            textViewWiek.setText("Wiek: " + wiek.toString());
        } else {
            textViewWiek.setText("Wiek: brak danych");
        }

        buttonEditDane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditDaneActivity.class);
                i.putExtra("imie", imie);
                i.putExtra("nazwisko", nazwisko);
                i.putExtra("wiek", wiek);
                i.putExtra("login", login);
                startActivity(i);
            }
        });

        buttonChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PopChangePass.class);
                i.putExtra("login", login);
                startActivity(i);
            }
        });

        return v;
    }
}
