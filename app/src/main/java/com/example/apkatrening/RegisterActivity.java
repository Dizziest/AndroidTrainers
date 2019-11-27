package com.example.apkatrening;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
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

public class RegisterActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "haslo";
    private static final String KEY_EMPTY = "";

    private EditText editLogin;
    private EditText editPassword;
    private EditText editEmail;
    private EditText editConfirmPassword;
    private String login;
    private String password;
    private String confirmPassword;
    private String email;
    private ProgressDialog pDialog;
    private String register_url = "https://treneiro.000webhostapp.com/register.php";
    private SessionHandler session;
    private TextView toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        setContentView(R.layout.activity_register);

        editLogin = findViewById(R.id.editRegLogin);
        editEmail = findViewById(R.id.editRegEmail);
        editPassword = findViewById(R.id.editRegPassword);
        editConfirmPassword = findViewById(R.id.editConfirmRegPassword);

        Button register = findViewById(R.id.buttonRegister);

        toLogin = findViewById(R.id.textView_toLogin);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = editLogin.getText().toString().toLowerCase().trim();
                password = editPassword.getText().toString().trim();
                confirmPassword = editConfirmPassword.getText().toString().trim();
                email = editEmail.getText().toString().toLowerCase().trim();
                if (validateInputs()) {
                    registerUser();
                }
            }
        });
    }

        private void displayLoader(){
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Rejestrowanie..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        private void registerUser(){
        displayLoader();
            final JSONObject request = new JSONObject();
            try {
                request.put(KEY_LOGIN, login);
                request.put(KEY_PASSWORD, password);
                request.put(KEY_EMAIL, email);
            } catch (JSONException e){
                e.printStackTrace();
            }

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pDialog.dismiss();
                    try {
                        if (response.getInt(KEY_STATUS) == 0) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                            Toast.makeText(getApplicationContext(), "Pomyślnie utworzono konto. Możesz teraz się zalogować.", Toast.LENGTH_LONG).show();
                        } else if (response.getInt(KEY_STATUS) == 1) {
                            editLogin.setError("Login zajety");
                            editLogin.requestFocus();
                        } else if (response.getInt(KEY_STATUS) == 3){
                          editEmail.setError("Email zajety");
                          editEmail.requestFocus();
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
            if (KEY_EMPTY.equals(email)){
                editEmail.setError("Email nie moze byc pusty");
                editEmail.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(login)){
                editLogin.setError("Login nie moze byc pusty");
                editLogin.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(password)){
                editPassword.setError("Haslo nie moze byc puste");
                editPassword.requestFocus();
                return false;
            }
            if (KEY_EMPTY.equals(confirmPassword)){
                editConfirmPassword.setError("Potwierdz haslo nie moze byc puste");
                editConfirmPassword.requestFocus();
                return false;
            }
            if (!password.equals(confirmPassword)){
                editConfirmPassword.setError("Hasla sie nie zgadzaja");
                editConfirmPassword.requestFocus();
                return false;
            }
            return true;
        }


}
