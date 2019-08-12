package com.example.yuusha.ecocharge.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import  java.util.ArrayList;
import  java.util.List;
import  android.content.DialogInterface;
import  android.util.SparseBooleanArray;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.example.yuusha.ecocharge.Controller.ComodoController;
import com.example.yuusha.ecocharge.Model.Comodo;
import com.example.yuusha.ecocharge.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import org.json.JSONArray;

public class LcomodoFragment extends Fragment implements View.OnClickListener {
    List<Comodo> myList;
    ListView lv;
    ComodoController adapter;

    ProgressDialog pDialog;

    List<Comodo> contactList;
    TextView textViewSemComodo;

    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.getListaComodo();
        View v = inflater.inflate(R.layout.fragment_lcomodo, container, false);


        textViewSemComodo = v.findViewById(R.id.textViewSemComodo);

        FloatingActionButton fab = v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ComodoFragment();
                replaceFragment(fragment);
            }
        });

        this.myList = new ArrayList<Comodo>();
        this.contactList = new ArrayList<Comodo>();
        //adapter = new ComodoController(this, R.layout.single_item_comodo, myList);
        adapter = new ComodoController(getActivity(), R.layout.view_list, myList);
        lv = (ListView) v.findViewById(R.id.list);
        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        return v;
    }

    @Override
    public void onClick(View v) {
        Log.e("aaaaaaaa", "click normal");
    }

    public void getListaComodo() {
        @SuppressLint("RestrictedApi")
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LcomodoFragment.this.getContext());
        String personId = acct.getId();

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
        int method = Request.Method.GET;
        String url = "https://apiconsumo.herokuapp.com/api/app/src/comodo/" + personId;

        JsonObjectRequest req = new JsonObjectRequest(method,url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray contacts = response.getJSONArray("results");
                            // looping through All Contacts
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);

                                Comodo comodo = new Comodo();

                                String nome = c.getString("nome");
                                int id = Integer.parseInt(c.getString("id"));
                                String data_criacao = c.getString("data_criacao");
                                boolean status = c.getBoolean("status");

                                comodo.setNome(nome);
                                comodo.setId(id);
                                comodo.setData_criacao(data_criacao);
                                comodo.setStatus(String.valueOf(status));

                                // adding contact to contact list
                                contactList.add(comodo);

                                textViewSemComodo.setVisibility(View.INVISIBLE);

                            }

                            adapter = new ComodoController(getActivity(), R.layout.single_item_comodo, contactList);

                            lv.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(30000, 10, 1));

        MyRequestQueue.add(req);

    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, someFragment, someFragment.getClass().getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
    }
}
