package com.example.apkatrening;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressFragment extends Fragment {

    private static final String KEY_ID = "id";
    private String show_wymiary_url = "https://treneiro.000webhostapp.com/show_wymiary.php";
    private RecyclerView recyclerView;
    private WymiarAdapter adapter;
    private Integer id;
    private FloatingActionButton button_add;

    List<Wymiar> wymiarList;

    public static ProgressFragment newInstance(int id){
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                double waga = Double.parseDouble(data.getStringExtra("waga"));
                int wzrost = Integer.parseInt(data.getStringExtra("wzrost"));
                double biceps = Double.parseDouble(data.getStringExtra("obwod_biceps"));
                double klatka = Double.parseDouble(data.getStringExtra("obwod_klatka"));
                String date = data.getStringExtra("data");
                int wymiar_id = Integer.parseInt(data.getStringExtra("id_wymiar"));
                Wymiar wymiar = new Wymiar(waga, wzrost, biceps, klatka, date, wymiar_id);
                wymiarList.add(wymiar);
                adapter = new WymiarAdapter(getActivity(), wymiarList);
                recyclerView.setAdapter(adapter);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress,container,false);
        wymiarList = new ArrayList<>();

        if (getArguments()!=null){
            id = getArguments().getInt(KEY_ID);
        }

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        button_add = v.findViewById(R.id.addWymiar);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddWymiarActivity.class);
                i.putExtra(KEY_ID, id);
                startActivityForResult(i, 1);
            }
        });

        showWymiar();

        return v;

    }


    private void showWymiar(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, show_wymiary_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray wymiary = new JSONArray(response);

                    for (int i=0; i<wymiary.length(); i++){
                        JSONObject wymiarObject = wymiary.getJSONObject(i);

                        double waga = wymiarObject.getDouble("waga");
                        int wzrost = wymiarObject.getInt("wzrost");
                        double obw_biceps = wymiarObject.getDouble("obw_biceps");
                        double obw_klatka = wymiarObject.getDouble("obw_klatka");
                        String date = wymiarObject.getString("date");
                        int id_wymiar = wymiarObject.getInt("id");

                        Wymiar wymiar = new Wymiar(waga, wzrost, obw_biceps, obw_klatka, date, id_wymiar);
                        wymiarList.add(wymiar);
                    }

                    adapter = new WymiarAdapter(getActivity(), wymiarList);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ID, id.toString());
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
}
