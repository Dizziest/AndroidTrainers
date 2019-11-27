package com.example.apkatrening;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanyDniaActivity extends AppCompatActivity {

    private String KEY_ID = "id";
    private String show_treningi_url = "https://treneiro.000webhostapp.com/show_treningi.php";
    private String show_diety_url = "https://treneiro.000webhostapp.com/show_diety.php";

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private TreningAdapter adapter;
    private DietaAdapter adapter2;

    private int id;
    List<Trening> treningiList;
    List<Dieta> dietyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plany_dnia);

        treningiList = new ArrayList<>();
        dietyList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view_treningi);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView2 = findViewById(R.id.recycler_view_diety);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView2.setNestedScrollingEnabled(false);

        id = getIntent().getExtras().getInt("id");

        showTrening();
        showDieta();

    }


    private void showTrening(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, show_treningi_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray treningi = new JSONArray(response);

                    for (int i=0; i<treningi.length(); i++){
                        JSONObject treningObject = treningi.getJSONObject(i);

                        int id_plan = treningObject.getInt("id_plan");
                        int id_cwiczenie = treningObject.getInt("id_cwiczenie");
                        int serie = treningObject.getInt("serie");
                        int powtorzenia = treningObject.getInt("powtorzenia");
                        String nazwa = treningObject.getString("nazwa");
                        int obciazenie = treningObject.getInt("obciazenie");
                        String opis = treningObject.getString("opis");

                        Trening trening = new Trening(id_plan, nazwa, opis, id_cwiczenie, serie, powtorzenia, obciazenie);
                        treningiList.add(trening);
                    }

                    adapter = new TreningAdapter(getApplicationContext(), treningiList);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ID, String.valueOf(id));
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void showDieta(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, show_diety_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray diety = new JSONArray(response);

                    for (int i=0; i<diety.length(); i++){
                        JSONObject dietaObject = diety.getJSONObject(i);

                        int id_plan = dietaObject.getInt("id_plan");
                        int id_danie = dietaObject.getInt("id_danie");
                        String nazwa = dietaObject.getString("nazwa");
                        String opis = dietaObject.getString("opis");
                        int kalorie = dietaObject.getInt("kalorycznosc");

                        Dieta dieta = new Dieta(id_plan, id_danie, nazwa, kalorie, opis);
                        dietyList.add(dieta);
                    }

                    adapter2 = new DietaAdapter(getApplicationContext(), dietyList);
                    recyclerView2.setAdapter(adapter2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ID, String.valueOf(id));
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
