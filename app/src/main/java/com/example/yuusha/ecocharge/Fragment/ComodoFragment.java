package com.example.yuusha.ecocharge.Fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuusha.ecocharge.Model.Comodo;
import com.example.yuusha.ecocharge.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Random;


public class ComodoFragment extends Fragment implements View.OnClickListener {

    private EditText text;
    private Comodo comodo;
    private TextView txtView;
    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_comodo, container, false);
        try {
            comodo = new Comodo();

            this.comodo.id = Integer.parseInt((String)this.getArguments().get("id"));
            this.comodo.nome = (String)this.getArguments().get("nome");
            this.comodo.data_criacao = (String)this.getArguments().get("data_criacao");
            this.comodo.status = (String)this.getArguments().get("status");

            txtView = v.findViewById(R.id.textViewComodo);
            txtView.setText("Editar seu comodo");

        } catch (Exception ex){
            comodo = null;
        }



        Button button = v.findViewById(R.id.button_comodo);

        text = (EditText) v.findViewById(R.id.editText);

        try { text.setText(this.comodo.nome); } catch (Exception ex) {  }

        button.setOnClickListener(this);

        return v;
    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    @Override
    public void onClick(View view) {
        @SuppressLint("RestrictedApi") GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        String personId = acct.getId();

        switch (view.getId()) {
            case R.id.button_comodo:

                int color = getRandomColor();

                String colorfinal = String.valueOf(color).replace("-","#");

                Editable nome_comodo = text.getText();

                RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

                String url = "https://apiconsumo.herokuapp.com/api/app/reg/comodo";

                HashMap<String, String> params = new HashMap<String, String>();

                int method = Request.Method.POST;

                try {
                    params.put("id", String.valueOf(this.comodo.id));
                    params.put("status", String.valueOf(this.comodo.status));
                    params.put("data_criacao", String.valueOf(this.comodo.data_criacao));

                    url = "https://apiconsumo.herokuapp.com/api/app/edit/comodo/";
                    method = Request.Method.PUT;
                } catch (Exception ex) {
                    params.put("color", colorfinal);
                }

                params.put("usuario_id",personId);
                params.put("nome", String.valueOf(nome_comodo));


                JsonObjectRequest req = new JsonObjectRequest(method,url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
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

                MyRequestQueue.add(req);

                String texto = "Cômodo Salvo";
                Toast.makeText(getActivity(),texto, Toast.LENGTH_SHORT).show();

                fragment = new LcomodoFragment();
                replaceFragment(fragment);

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