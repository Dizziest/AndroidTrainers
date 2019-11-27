package com.example.apkatrening;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class PopChangePass extends Activity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMPTY = "";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_NEW_PASSWORD = "new_password";
    private static final String KEY_OLD_PASSWORD = "old_password";

    private String change_password_url = "https://treneiro.000webhostapp.com/change_pass.php";
    private EditText editOldPassword;
    private EditText editNewPassword;
    private Button buttonChange;
    private ProgressDialog pDialog;
    private String login;
    private String oldPassword;
    private String newPassword;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_change_pass);
        session = new SessionHandler(getApplicationContext());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.65));

        editOldPassword = findViewById(R.id.editTextActualPass);
        editNewPassword = findViewById(R.id.editTextNewPass);

        login = getIntent().getExtras().getString("login");

        buttonChange = findViewById(R.id.buttonChangePassword);

        buttonChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPassword = editOldPassword.getText().toString().trim();
                newPassword = editNewPassword.getText().toString().trim();
                if (validateInputs()){
                    updateHaslo();
                }
            }
        });


    }

    private void updateHaslo(){
        displayLoader();
        final JSONObject request = new JSONObject();
        try {
            request.put(KEY_OLD_PASSWORD, oldPassword);
            request.put(KEY_NEW_PASSWORD, newPassword);
            request.put(KEY_LOGIN, login);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, change_password_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    if (response.getInt(KEY_STATUS) == 0) {
                        session.logoutUser();
                        Intent i = new Intent(PopChangePass.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(getApplicationContext(), "Pomyślnie zmieniono hasło. Zaloguj się ponownie.", Toast.LENGTH_LONG).show();
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
        pDialog = new ProgressDialog(PopChangePass.this);
        pDialog.setMessage("Edytowanie..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private boolean validateInputs(){
        if (KEY_EMPTY.equals(oldPassword)){
            editOldPassword.setError("Stare haslo nie moze byc puste");
            editOldPassword.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(newPassword)){
            editNewPassword.setError("Nowe haslo nie moze byc puste");
            editNewPassword.requestFocus();
            return false;
        }

        return true;
    }
}
