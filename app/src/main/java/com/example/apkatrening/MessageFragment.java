package com.example.apkatrening;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MessageFragment extends Fragment {

    private static final String KEY_ID = "id";
    private static final String KEY_STATUS = "status";
    private static final String KEY_TRESC = "tresc";
    private static final String KEY_DATA = "data";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_EMPTY = "";
    private RecyclerView recyclerView;
    private WiadomosciAdapter wiadomosciAdapter;
    private List<Wiadomosc> wiadomosciList;
    private String show_wiadomosci_url = "https://treneiro.000webhostapp.com/show_wiadomosci.php";
    private String add_wiadomosc_url = "https://treneiro.000webhostapp.com/add_wiadomosc.php";
    private SessionHandler sessionHandler;
    private User user;
    private ProgressDialog pDialog;
    private Button button_send;
    private Integer id;
    private EditText edit_tresc;
    private String tresc;
    private String data;
    private Calendar myCalendar = Calendar.getInstance();
    private String format = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message,container,false);
        wiadomosciList = new ArrayList<>();

        sessionHandler = new SessionHandler(getContext());
        user = sessionHandler.getUserDetails();

        id = user.getId();

        recyclerView = v.findViewById(R.id.recycler_view_messagelist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        button_send = v.findViewById(R.id.button_chatbox_send);
        edit_tresc = v.findViewById(R.id.edittext_chatbox);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tresc = edit_tresc.getText().toString().trim();
                if (validateInputs()) {
                    addWiadomosc();
                }
            }
        });

        showWiadomosci();


        return v;
    }


    private void showWiadomosci(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, show_wiadomosci_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray wiadomosci = new JSONArray(response);

                    for (int i=0; i<wiadomosci.length(); i++){
                        JSONObject wiadomoscObject = wiadomosci.getJSONObject(i);

                        String tresc = wiadomoscObject.getString("tresc");
                        String data = wiadomoscObject.getString("data");
                        int id_uzytkownik = wiadomoscObject.getInt("id_uzytkownik");
                        String login = wiadomoscObject.getString("login");

                        Wiadomosc wiadomosc = new Wiadomosc(login, id_uzytkownik, tresc, data);
                        wiadomosciList.add(wiadomosc);
                    }

                    wiadomosciAdapter = new WiadomosciAdapter(getContext(), wiadomosciList);
                    recyclerView.setAdapter(wiadomosciAdapter);

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


    private void addWiadomosc() {
        displayLoader();
        final JSONObject request = new JSONObject();
        try {
            data = simpleDateFormat.format(myCalendar.getTime());
            Log.d("XDDDDDDD", data);
            request.put(KEY_ID, id);
            request.put(KEY_TRESC, tresc);
            request.put(KEY_DATA, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(Request.Method.POST, add_wiadomosc_url, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    if (response.getInt(KEY_STATUS) == 0) {
                        Wiadomosc wiadomosc = new Wiadomosc(user.getLogin(), user.getId(), tresc, data);
                        wiadomosciList.add(wiadomosc);

                        wiadomosciAdapter = new WiadomosciAdapter(getContext(), wiadomosciList);
                        recyclerView.setAdapter(wiadomosciAdapter);
                    } else {
                        Toast.makeText(getContext(), response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(jsArrayRequest);

    }

    private void displayLoader() {
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Wysyłanie..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(tresc)) {
            edit_tresc.setError("Wiadomość nie może być pusta");
            edit_tresc.requestFocus();
            return false;
        }
        return true;
    }
}
