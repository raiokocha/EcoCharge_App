package com.example.yuusha.ecocharge.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuusha.ecocharge.Controller.ComodoController;
import com.example.yuusha.ecocharge.Model.Comodo;
import com.example.yuusha.ecocharge.R;
import com.example.yuusha.ecocharge.WelcomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment implements View.OnClickListener  {

    private String TAG = InicioFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_inicio, container, false);
        CardView cardarduino = (CardView) v.findViewById(R.id.arduinos);
        CardView cardcomodo = (CardView) v.findViewById(R.id.comodos);
        CardView cardaparelho = (CardView) v.findViewById(R.id.aparelhos);
        CardView carddash = (CardView) v.findViewById(R.id.dashboards);
        CardView cartarifa = (CardView) v.findViewById(R.id.tarifas);
        CardView cardajuda = (CardView) v.findViewById(R.id.ajudas);


        cardarduino.setOnClickListener(this);
        cardcomodo.setOnClickListener(this);
        cardaparelho.setOnClickListener(this);
        carddash.setOnClickListener(this);
        cartarifa.setOnClickListener(this);
        cardajuda.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {

        Fragment fragment = null;

        switch (view.getId()) {
            case R.id.arduinos:
                fragment = new LserialFragment();
                replaceFragment(fragment);
                break;
            case R.id.comodos:
                fragment = new LcomodoFragment();
                replaceFragment(fragment);
                break;
            case R.id.aparelhos:
                fragment = new LaparelhoFragment();
                replaceFragment(fragment);
                break;
            case R.id.dashboards:
                fragment = new EstatisticaGeralFragment();
                replaceFragment(fragment);
                break;
            case R.id.tarifas:
                fragment = new ConfiguracaoFragment();
                replaceFragment(fragment);
                break;
            case R.id.ajudas:
                Intent myIntent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(myIntent);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.mainFrame, someFragment, someFragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
