package com.example.apkatrening;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainersFragment extends Fragment {

    private String KEY_STATUS = "status";
    private String KEY_ID = "id";
    private String KEY_MESSAGE = "message";
    private String show_trenerzy_url = "https://treneiro.000webhostapp.com/show_trenerzy.php";
    private String check_trener_url = "https://treneiro.000webhostapp.com/check_trener.php";

    private RecyclerView recyclerView;
    private TrenerAdapter adapter;
    private SessionHandler session;
    HomeFragment homeFragment;

    List<Trener> trenerzyList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trainers,container,false);
        trenerzyList = new ArrayList<>();
        session = new SessionHandler(getContext());
        User user = session.getUserDetails();

        homeFragment = HomeFragment.newInstance(user.getLogin(), user.getEmail());

        recyclerView = v.findViewById(R.id.recycler_view_trener);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        checkForTrener(user);

        return v;
    }


    private void showTrener(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, show_trenerzy_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray trenerzy = new JSONArray(response);

                    for (int i=0; i<trenerzy.length(); i++){
                        JSONObject trenerObject = trenerzy.getJSONObject(i);

                        String login = trenerObject.getString("login");
                        String imie = trenerObject.getString("imie");
                        String nazwisko = trenerObject.getString("nazwisko");
                        String email = trenerObject.getString("email");
                        String check = trenerObject.getString("doswiadczenie");
                        Integer doswiadczenie;
                        if (check.equals("null")){
                            doswiadczenie = 0;
                        } else {
                            doswiadczenie = trenerObject.getInt("doswiadczenie");
                        }
                        int id = trenerObject.getInt("id");

                        Trener trener = new Trener(imie, nazwisko, email, doswiadczenie, login, id);
                        trenerzyList.add(trener);
                    }

                    adapter = new TrenerAdapter(getActivity(), trenerzyList);
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
        });

        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private void checkForTrener(User user){
        final JSONObject request = new JSONObject();
        try {
            request.put(KEY_ID, user.getId());
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, check_trener_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt(KEY_STATUS) == 0) {
                        showTrener();
                    } else {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, homeFragment).commit();
                        Toast.makeText(getContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);
    }
}