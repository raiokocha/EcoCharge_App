package com.example.yuusha.ecocharge.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuusha.ecocharge.MainActivity;
import com.example.yuusha.ecocharge.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ArduinoFragment extends Fragment implements View.OnClickListener {

    private EditText text;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_arduino, container, false);

        Button button = v.findViewById(R.id.button_arduino);

        text = (EditText) v.findViewById(R.id.editText_arduino);

        button.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        @SuppressLint("RestrictedApi") GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        String personId = acct.getId();

        switch (view.getId()) {
            case R.id.button_arduino:

                Editable serial_arduino = text.getText();

                RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

                String url = "https://apiconsumo.herokuapp.com/api/app/reg/arduino";

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("usuario_id",personId);
                params.put("serial", String.valueOf(serial_arduino));

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                    String texto = "Cadastrado com successo.";
                                    Toast.makeText(getActivity(),texto, Toast.LENGTH_SHORT).show();

                                    fragment = new InicioFragment();
                                    replaceFragment(fragment);
                                } catch (JSONException e) {
                                    String texto = "Não foi possível cadastrar. Tente novamente. ";
                                    Toast.makeText(getActivity(),texto, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String texto = "Não foi possível cadastrar. Tente novamente. ";
                        Toast.makeText(getActivity(),texto, Toast.LENGTH_SHORT).show();
                    }
                });

                MyRequestQueue.add(req);

                break;


        }

    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, someFragment, someFragment.getClass().getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
    }

}
