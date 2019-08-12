package com.example.yuusha.ecocharge.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuusha.ecocharge.Controller.SerialController;
import com.example.yuusha.ecocharge.Model.Serial;
import com.example.yuusha.ecocharge.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LserialFragment extends Fragment implements View.OnClickListener {
    ListView lv;
    SerialController adapter;

    ProgressDialog pDialog;
    List<Serial> contactList;
    TextView textViewSemSerial;

    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lserial, container, false);

        lv = (ListView) v.findViewById(R.id.list);

        this.contactList = new ArrayList<Serial>();
        textViewSemSerial = v.findViewById(R.id.textViewSemSerial);

        this.getListaArduino();

        FloatingActionButton fab = v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ArduinoFragment();
                replaceFragment(fragment);
            }
        });

        lv.setAdapter(adapter);

        return v;
    }

    @Override
    public void onClick(View v) {

    }

    public void getListaArduino() {
        @SuppressLint("RestrictedApi")
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LserialFragment.this.getContext());
        String personId = acct.getId();

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
        int method = Request.Method.GET;
        String url = "https://apiconsumo.herokuapp.com/api/app/src/arduino/" + personId;

        JsonObjectRequest req = new JsonObjectRequest(method,url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            contactList = new ArrayList<>();

                            JSONArray contacts = response.getJSONArray("results");
                            // looping through All Contacts
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);

                                Serial itemSerial = new Serial();

                                int id = Integer.parseInt(c.getString("id"));
                                String serial_arduino = c.getString("serial");

                                itemSerial.setId(id);
                                itemSerial.setSerial(serial_arduino);
                                contactList.add(itemSerial);

                                textViewSemSerial.setVisibility(View.INVISIBLE);
                            }

                            adapter = new SerialController(getActivity(), R.layout.single_item_serial, contactList);

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
