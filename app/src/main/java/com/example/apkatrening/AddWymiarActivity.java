package com.example.apkatrening;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddWymiarActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMPTY = "";
    private static final String KEY_ID = "id";
    private static final String KEY_WZROST = "wzrost";
    private static final String KEY_WAGA = "waga";
    private static final String KEY_BICEPS = "obwod_biceps";
    private static final String KEY_KLATKA = "obwod_klatka";
    private static final String KEY_DATA = "data";
    private String add_wymiar_url = "https://treneiro.000webhostapp.com/add_wymiar.php";

    final Calendar myCalendar = Calendar.getInstance();
    private EditText editWzrost;
    private EditText editWaga;
    private EditText editBiceps;
    private EditText editKlatka;
    private EditText editData;
    private ProgressDialog pDialog;
    private Button buttonAdd;
    private int id;
    private String wzrost;
    private String waga;
    private String biceps;
    private String klatka;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wymiar);

        editData = findViewById(R.id.editText_Date);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddWymiarActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editWzrost = findViewById(R.id.editText_editWzrost);
        editWaga = findViewById(R.id.editText_editWaga);
        editBiceps = findViewById(R.id.editText_editBiceps);
        editKlatka = findViewById(R.id.editText_editKlatka);
        buttonAdd = findViewById(R.id.button_btnAddWymiar);

        id = getIntent().getExtras().getInt(KEY_ID);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wzrost = editWzrost.getText().toString();
                waga = editWaga.getText().toString();
                biceps = editBiceps.getText().toString();
                klatka = editKlatka.getText().toString();
                data = editData.getText().toString();
                if (validateInputs()) {
                    addWymiar();
                }
            }
        });

    }

    private void addWymiar() {
        displayLoader();
        final JSONObject request = new JSONObject();
        try {
            request.put(KEY_ID, id);
            request.put(KEY_WZROST, wzrost);
            request.put(KEY_WAGA, waga);
            request.put(KEY_BICEPS, biceps);
            request.put(KEY_KLATKA, klatka);
            request.put(KEY_DATA, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, add_wymiar_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    if (response.getInt(KEY_STATUS) == 0) {
                        Toast.makeText(getApplicationContext(), "Pomyślnie dodano wymiar.", Toast.LENGTH_LONG).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(KEY_WZROST, wzrost);
                        returnIntent.putExtra(KEY_WAGA, waga);
                        returnIntent.putExtra(KEY_BICEPS, biceps);
                        returnIntent.putExtra(KEY_KLATKA, klatka);
                        returnIntent.putExtra(KEY_DATA, data);
                        returnIntent.putExtra("id_wymiar", response.getString("id_wymiar"));
                        setResult(Activity.RESULT_OK,returnIntent);
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

    private void updateLabel() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);

        editData.setText(simpleDateFormat.format(myCalendar.getTime()));
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(AddWymiarActivity.this);
        pDialog.setMessage("Dodawanie..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(wzrost)) {
            editWzrost.setError("Wzrost nie moze byc pusty");
            editWzrost.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(waga)) {
            editWaga.setError("Waga nie moze byc pusta");
            editWaga.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(biceps)) {
            editBiceps.setError("Biceps nie moze byc pusty");
            editBiceps.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(klatka)) {
            editKlatka.setError("Klatka nie moze byc pusta");
            editKlatka.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(data)) {
            editData.setError("Data nie moze byc pusta");
            editData.requestFocus();
            return false;
        }
        return true;
    }
}
