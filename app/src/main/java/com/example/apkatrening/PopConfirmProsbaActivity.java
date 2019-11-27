package com.example.apkatrening;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class PopConfirmProsbaActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMPTY = "";
    private static final String KEY_ID = "id";
    private static final String KEY_ID_TRENER = "id_trener";

    private String add_prosba_url = "https://treneiro.000webhostapp.com/add_prosba.php";
    private Button buttonAdd;
    private ProgressDialog pDialog;
    private SessionHandler session;
    private TextView potwierdzenie;
    private String login;
    private int id_trener;
    private int id;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_confirm_prosba);
        session = new SessionHandler(getApplicationContext());

        User user = session.getUserDetails();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.5));

        buttonAdd = findViewById(R.id.buttonPotwierdzProsbe);
        potwierdzenie = findViewById(R.id.textView_czyChceszProsbe);

        login = getIntent().getExtras().getString("login");
        id_trener = getIntent().getExtras().getInt("id");
        id = user.getId();

        potwierdzenie.setText("Czy na pewno chcesz wysłać prośbę o trenowanie do trenera " + login + " ?");

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProsba();
            }
        });

    }


    private void addProsba() {
        displayLoader();
        final JSONObject request = new JSONObject();
        try {
            request.put(KEY_ID, id);
            request.put(KEY_ID_TRENER, id_trener);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, add_prosba_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    if (response.getInt(KEY_STATUS) == 0) {
                        Toast.makeText(getApplicationContext(), "Pomyślnie dodano prośbę.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(PopConfirmProsbaActivity.this, DashboardActivity.class);
                        startActivity(i);
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

    private void displayLoader() {
        pDialog = new ProgressDialog(PopConfirmProsbaActivity.this);
        pDialog.setMessage("Dodawanie..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
