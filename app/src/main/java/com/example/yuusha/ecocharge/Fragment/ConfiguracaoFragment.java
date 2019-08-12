package com.example.yuusha.ecocharge.Fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuusha.ecocharge.Model.Serial;
import com.example.yuusha.ecocharge.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ConfiguracaoFragment extends Fragment {

    Button btnSalvarConf;
    EditText inputConf;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_configuracao, container, false);

        btnSalvarConf = (Button) v.findViewById(R.id.button_salvar_conf);
        inputConf = (EditText) v.findViewById(R.id.input_tarifa);

        getConfiguracaoUsuario();

        btnSalvarConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("RestrictedApi")
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ConfiguracaoFragment.this.getContext());

                RequestQueue MyRequestQueue = Volley.newRequestQueue(ConfiguracaoFragment.this.getContext());
                String url = "https://apiconsumo.herokuapp.com/api/app/edit/usuario/" + acct.getId();

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("google_id",acct.getId());
                params.put("nome", acct.getDisplayName());
                params.put("email", acct.getEmail());
                params.put("tarifa", inputConf.getText().toString());

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Toast.makeText(ConfiguracaoFragment.this.getContext(), "Configuração salva.", Toast.LENGTH_SHORT).show();

                                    fragment = new InicioFragment();
                                    replaceFragment(fragment);

                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                } catch (JSONException e) {
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
        });
        // Inflate the layout for this fragment
        return v;
    }

    public void getConfiguracaoUsuario() {
        @SuppressLint("RestrictedApi")
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ConfiguracaoFragment.this.getContext());

        RequestQueue MyRequestQueue = Volley.newRequestQueue(ConfiguracaoFragment.this.getContext());
        int method = Request.Method.GET;
        String url = "https://apiconsumo.herokuapp.com/api/app/src/usuario/" + acct.getId();

        JsonObjectRequest req = new JsonObjectRequest(method,url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String contacts = String.valueOf(response.getDouble("tarifa"));

                            if (!contacts.equals(null)) {
                                inputConf.setText(contacts);
                            }

                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
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