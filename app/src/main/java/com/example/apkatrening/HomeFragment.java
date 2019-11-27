package com.example.apkatrening;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private static final String ARG_LOGIN = "argLogin";
    private static final String ARG_EMAIL = "argEmail";

    private String KEY_STATUS = "status";
    private String KEY_ID = "id";
    private String KEY_MESSAGE = "message";
    private String check_trener_url = "https://treneiro.000webhostapp.com/check_trener.php";
    private String show_plany_url = "https://treneiro.000webhostapp.com/show_plany.php";

    private String login;
    private RecyclerView recyclerView;
    private PlanDniaAdapter adapter;
    private SessionHandler session;
    private TextView textHome;

    List<PlanDnia> planyList;

    public static HomeFragment newInstance(String login, String email){
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN, login);
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        planyList = new ArrayList<>();
        session = new SessionHandler(getContext());
        User user = session.getUserDetails();

        textHome = v.findViewById(R.id.TextViewHome);
        textHome.setText("Witaj w Treneiro, " + user.getLogin() + ". Aktualnie nie masz żadnych planów dnia. Przejdź do zakładki Trenerzy aby wysłać prośbę do trenera i zacząć z nim współpracę już dziś!");

        recyclerView = v.findViewById(R.id.recycler_view_plany);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        checkForTrener(user);

        return v;
    }

    private void showPlany(final User user){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, show_plany_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray plany = new JSONArray(response);

                    for (int i=0; i<plany.length(); i++){
                        JSONObject planObject = plany.getJSONObject(i);

                        String date = planObject.getString("date");
                        int id = planObject.getInt("id");

                        PlanDnia planDnia = new PlanDnia(id, date);
                        planyList.add(planDnia);
                    }

                    adapter = new PlanDniaAdapter(getActivity(), planyList);
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
                params.put(KEY_ID, String.valueOf(user.getId()));
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private void checkForTrener(final User user){
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
                    if (response.getInt(KEY_STATUS) == 1) {
                        textHome.setVisibility(View.GONE);
                        showPlany(user);
                    } else if (response.getInt(KEY_STATUS) == 0){
                        textHome.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Nie masz trenera więc nie masz planów dnia.", Toast.LENGTH_LONG).show();
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
