package com.example.apkatrening;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "haslo";
    private static final String KEY_EMPTY = "";
    private static final String KEY_IMIE = "imie";
    private static final String KEY_NAZWISKO = "nazwisko";
    private static final String KEY_WIEK = "wiek";
    private static final String KEY_ID = "id";
    private static final String KEY_EXPERIENCE = "doswiadczenie";

    private ProgressDialog pDialog;
    private EditText editLogin;
    private EditText editPassword;
    private String login;
    private String password;
    private String login_url = "https://treneiro.000webhostapp.com/login.php";
    private SessionHandler session;

    private TextView textNoAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        if (session.isLoggedIn()){
            loadDashboard();
        }
        setContentView(R.layout.activity_main);

        editLogin = findViewById(R.id.editLogin);
        editPassword = findViewById(R.id.editPassword);

        Button btnLogin = findViewById(R.id.buttonZaloguj);
        textNoAccount = findViewById(R.id.textView_NoAccount);

        textNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = editLogin.getText().toString().toLowerCase().trim();
                password = editPassword.getText().toString().trim();
                if (validateInputs()){
                    login();
                }
            }
        });
    }

    private void loadDashboard(){
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);
        finish();
    }

    private void displayLoader(){
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logowanie..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void login(){
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put(KEY_LOGIN, login);
            request.put(KEY_PASSWORD, password);
        } catch (JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    if (response.getInt(KEY_STATUS) == 0) {
                        if (response.isNull(KEY_WIEK) || response.isNull(KEY_IMIE) || response.isNull(KEY_NAZWISKO)){
                            session.loginUser(login, response.getString(KEY_EMAIL), "", "", 0, response.getInt(KEY_ID));
                            loadDashboard();
                        } else {
                            session.loginUser(login, response.getString(KEY_EMAIL), response.getString(KEY_IMIE), response.getString(KEY_NAZWISKO), response.getInt(KEY_WIEK), response.getInt(KEY_ID));
                            loadDashboard();
                        }
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

    private boolean validateInputs(){
        if (KEY_EMPTY.equals(login)){
            editLogin.setError("Login nie moze byc pusty");
            editLogin.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            editPassword.setError("Haslo nie moze byc puste");
            editPassword.requestFocus();
            return false;
        }
        return true;
    }
}
