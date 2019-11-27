package com.example.apkatrening;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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

public class PopConfirmWymiarDelete extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ID = "id";

    private String delete_wymiar_url = "https://treneiro.000webhostapp.com/delete_wymiar.php";
    private Button buttonDelete;
    private ProgressDialog pDialog;
    private SessionHandler session;
    private TextView potwierdzenie;
    private int id_wymiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_confirm_wymiar_delete);
        session = new SessionHandler(getApplicationContext());

        User user = session.getUserDetails();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .5));

        buttonDelete = findViewById(R.id.buttonPotwierdzUsuniecieWymiaru);
        potwierdzenie = findViewById(R.id.textView_czyChceszUsunac);

        id_wymiar = getIntent().getExtras().getInt("id");

        potwierdzenie.setText("Czy na pewno chcesz usunąć ten wymiar?");

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWymiar();
            }
        });

    }


    private void deleteWymiar() {
        displayLoader();
        final JSONObject request = new JSONObject();
        try {
            request.put(KEY_ID, id_wymiar);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, delete_wymiar_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    if (response.getInt(KEY_STATUS) == 0) {
                        Toast.makeText(getApplicationContext(), "Pomyślnie usunięto wymiar.", Toast.LENGTH_LONG).show();
                        finish();
                        Intent i = new Intent(PopConfirmWymiarDelete.this, DashboardActivity.class);
                        startActivity(i);
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
        pDialog = new ProgressDialog(PopConfirmWymiarDelete.this);
        pDialog.setMessage("Usuwanie..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}