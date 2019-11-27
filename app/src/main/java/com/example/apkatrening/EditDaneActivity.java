package com.example.apkatrening;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class EditDaneActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMPTY = "";
    private static final String KEY_IMIE = "imie";
    private static final String KEY_NAZWISKO = "nazwisko";
    private static final String KEY_WIEK = "wiek";
    private static final String KEY_LOGIN = "login";

    private String update_dane_url = "https://treneiro.000webhostapp.com/update_dane.php";
    private EditText editImie;
    private EditText editNazwisko;
    private EditText editWiek;
    private Button buttonEdit;
    private Integer wiek;
    private String imie;
    private String nazwisko;
    private String login;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dane);

        editImie = findViewById(R.id.editTextEditImie);
        editNazwisko = findViewById(R.id.editTextEditNazwisko);
        editWiek = findViewById(R.id.editTextEditWiek);

        editImie.setText(getIntent().getExtras().getString("imie"));
        editNazwisko.setText(getIntent().getExtras().getString("nazwisko"));
        wiek = getIntent().getExtras().getInt("wiek");
        login = getIntent().getExtras().getString("login");

        if (wiek == 0){
            editWiek.setText("");
        } else {
            editWiek.setText(wiek.toString());
        }

        buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imie = editImie.getText().toString().trim();
                nazwisko = editNazwisko.getText().toString().trim();
                wiek = Integer.parseInt(editWiek.getText().toString());
                if (validateInputs()){
                    updateDane();
                }
            }
        });


    }

    private void updateDane(){
        displayLoader();
        final JSONObject request = new JSONObject();
        try {
            request.put(KEY_IMIE, imie);
            request.put(KEY_NAZWISKO, nazwisko);
            request.put(KEY_WIEK, wiek);
            request.put(KEY_LOGIN, login);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, update_dane_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    if (response.getInt(KEY_STATUS) == 0) {
                        Toast.makeText(getApplicationContext(), "Pomyślnie edytowano dane. Zmiany będą widoczne po ponownym zalogowaniu.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);

    }

    private void displayLoader(){
        pDialog = new ProgressDialog(EditDaneActivity.this);
        pDialog.setMessage("Edytowanie..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private boolean validateInputs(){
        if (KEY_EMPTY.equals(imie)){
            editImie.setError("Imie nie moze byc puste");
            editImie.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(nazwisko)){
            editNazwisko.setError("Nazwisko nie moze byc puste");
            editNazwisko.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(wiek)){
            editWiek.setError("Wiek nie moze byc pusty");
            editWiek.requestFocus();
            return false;
        }
        return true;
    }
}
