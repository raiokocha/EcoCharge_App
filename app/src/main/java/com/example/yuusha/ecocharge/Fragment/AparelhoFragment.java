package com.example.yuusha.ecocharge.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.yuusha.ecocharge.Model.Aparelho;
import com.example.yuusha.ecocharge.Model.Comodo;
import com.example.yuusha.ecocharge.Model.Serial;
import com.example.yuusha.ecocharge.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import petrov.kristiyan.colorpicker.ColorPicker;


public class AparelhoFragment extends Fragment implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    private EditText editText_aparelho;
    private TextView textView;
    private Spinner spinner1;
    private Spinner spinner2;
    Fragment fragment = null;
    private Aparelho aparelho;
    String cor = "#00ffff";
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_aparelho, container, false);

        try {
            aparelho = new Aparelho();

            this.aparelho.id = Integer.parseInt((String)this.getArguments().get("id"));
            this.aparelho.nome = (String)this.getArguments().get("nome");
            this.aparelho.arduino_id = Integer.parseInt((String)this.getArguments().get("arduino_id"));
            this.aparelho.isAtivo = (Boolean)this.getArguments().get("status");
            this.aparelho.comodo_id = Integer.parseInt((String)this.getArguments().get("comodo_id"));
            this.aparelho.color = (String)this.getArguments().get("color");

            textView = v.findViewById(R.id.textView);
            textView.setText("Editar seu aparelho");
        } catch (Exception ex){
            aparelho = null;
        }

        editText_aparelho = (EditText) v.findViewById(R.id.editText_aparelho);

        try { editText_aparelho.setText(this.aparelho.nome); } catch (Exception ex) {  }

        spinner1 = (Spinner) v.findViewById(R.id.spinner);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);

        this.getListaComodo();

        this.getListaSerial();

        final Button button3 = v.findViewById(R.id.button3);
        if (button3 != null) {
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ColorPicker colorPicker = new ColorPicker(getActivity());
                    final ArrayList<String> colors = new ArrayList<String>();
                    colors.add("#00ffff");
                    colors.add("#900090");
                    colors.add("#ffff00");
                    colors.add("#00ff00");
                    colors.add("#ff0000");
                    colors.add("#0000ff");

                    colorPicker
                            .setColors(colors)
                            .setColumns(3)
                            .setRoundColorButton(true)
                            .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                                @Override
                                public void onChooseColor(int position, int color) {
                                    Log.d("position", "" + position + "color" + color);

                                    cor = (String) colors.get(position);

                                }

                                @Override                                public void onCancel() {


                                }
                            }).setTitle("Selecione uma cor:").show();


                }
            });
        }


        button = v.findViewById(R.id.button_aparelho);

        button.setOnClickListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                @SuppressLint("RestrictedApi") GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
                String personId = acct.getId();

                switch (view.getId()) {
                    case R.id.button_aparelho:

                        /* 3 SPINNERS*/
                        Comodo item1;
                        Serial item2;
                        try {
                            item1 = (Comodo)spinner1.getAdapter().getItem(spinner1.getSelectedItemPosition());
                            item2 = (Serial)spinner2.getAdapter().getItem(spinner2.getSelectedItemPosition());
                        } catch (Exception ex){
                            item1 = new Comodo();
                            item2 = new Serial();
                        }

                        /* 3 SPINNERS*/

                        String nome_aparelho = editText_aparelho.getText().toString();

                        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

                        String url;

                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("color", String.valueOf(cor));
                        params.put("nome", String.valueOf(nome_aparelho));

                        int method = Request.Method.PUT;

                        if (aparelho != null)
                            url = "https://apiconsumo.herokuapp.com/api/app/edit/aparelho/" + aparelho.getId();
                        else {
                            method = Request.Method.POST;
                            url = "https://apiconsumo.herokuapp.com/api/app/reg";
                            params.put("comodo_id", String.valueOf(item1.getId()));
                            params.put("arduino_id", String.valueOf(item2.getId()));
                            params.put("voltagem", String.valueOf("127"));
                        }

                        JsonObjectRequest req = new JsonObjectRequest(method, url, new JSONObject(params),
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

                        fragment = new LaparelhoFragment();
                        replaceFragment(fragment);

                        break;
                }

            }
        });

        if (aparelho != null) {
            spinner1.setVisibility(View.INVISIBLE);
            spinner2.setVisibility(View.INVISIBLE);
        }


        return v;
    }

    public static void hideKeyboard( Context context ) {

        try {
            InputMethodManager inputManager = ( InputMethodManager ) context.getSystemService( Context.INPUT_METHOD_SERVICE );

            View view = ( (Activity) context ).getCurrentFocus();
            if ( view != null ) {
                inputManager.hideSoftInputFromWindow( view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, someFragment, someFragment.getClass().getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View view) {

    }

    public void getListaComodo() {
        @SuppressLint("RestrictedApi")
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(AparelhoFragment.this.getContext());
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

                            ArrayList<Comodo> lista = new  ArrayList<Comodo>();

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
                                lista.add(comodo);
                            }

                            if (!(lista.size() > 0)) {
                                Comodo c = new Comodo();
                                c.id = -1;
                                c.nome = "Nenhum cômodo cadastrado.";
                                lista.add(c);
                                button.setEnabled(false);
                            }

                            ArrayAdapter<Comodo> adapter = new ArrayAdapter<Comodo>(AparelhoFragment.this.getActivity(), android.R.layout.simple_spinner_item, lista);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner1.setAdapter(adapter);

                            if (editText_aparelho != null && aparelho != null) {

                                for (int i=0;i<spinner1.getCount();i++){
                                    if (((Comodo)spinner1.getItemAtPosition(i)).getId() == aparelho.getComodo_id()){
                                        spinner1.setSelection(i);
                                        break;
                                    }
                                }
                            }
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

    public void getListaSerial() {
        @SuppressLint("RestrictedApi")
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(AparelhoFragment.this.getContext());
        final String personId = acct.getId();

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
        int method = Request.Method.GET;
        String url = "https://apiconsumo.herokuapp.com/api/app/src/arduino/" + personId;

        JsonObjectRequest req = new JsonObjectRequest(method,url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray contacts = response.getJSONArray("results");

                            ArrayList<Serial> lista = new  ArrayList<Serial>();

                            // looping through All Contacts
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);

                                Serial serial = new Serial();

                                String ser = c.getString("serial");
                                int id = Integer.parseInt(c.getString("id"));

                                serial.setSerial(ser);
                                serial.setId(id);

                                // adding contact to contact list
                                lista.add(serial);
                            }

                            if (!(lista.size() > 0)) {
                                Serial s = new Serial();
                                s.id = -1;
                                s.serial = "Nenhum serial cadastrado.";
                                lista.add(s);
                                button.setEnabled(false);
                            }

                            ArrayAdapter<Serial> adapter2 = new ArrayAdapter<Serial>(AparelhoFragment.this.getActivity(), android.R.layout.simple_spinner_item, lista);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(adapter2);

                            if (editText_aparelho != null && aparelho != null) {

                                for (int i=0;i<spinner2.getCount();i++){
                                    if (((Serial)spinner2.getItemAtPosition(i)).getId() == aparelho.getArduino_id()){
                                        spinner2.setSelection(i);
                                        break;
                                    }
                                }
                            }
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
}
