package com.example.yuusha.ecocharge.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuusha.ecocharge.Controller.AparelhoController;
import com.example.yuusha.ecocharge.Controller.ComodoController;
import com.example.yuusha.ecocharge.Model.Aparelho;
import com.example.yuusha.ecocharge.Model.Comodo;
import com.example.yuusha.ecocharge.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LaparelhoFragment extends Fragment {
    List<Aparelho> myList;
    ListView lv;
    AparelhoController adapter;

    ProgressDialog pDialog;
    private Spinner spinnerLcomodo;
    List<Aparelho> contactList;

    TextView textViewSemAparelho;

    Button buttonPopupAparelho;

    Fragment fragment = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_laparelho, container, false);

        spinnerLcomodo = v.findViewById(R.id.spinnerLcomodo);
        this.getListaComodo();

        buttonPopupAparelho = v.findViewById(R.id.buttonPopupAparelho);

        textViewSemAparelho = v.findViewById(R.id.textViewSemAparelho);
        spinnerLcomodo.setPrompt("Selecione o cômodo para listar");

        spinnerLcomodo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Comodo comodo = (Comodo)spinnerLcomodo.getAdapter().getItem(spinnerLcomodo.getSelectedItemPosition());
                getListaAparelho(comodo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        FloatingActionButton fab = v.findViewById(R.id.fabAparelho);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new AparelhoFragment();
                replaceFragment(fragment);
            }
        });

        this.myList = new ArrayList<Aparelho>();
        this.contactList = new ArrayList<Aparelho>();
        //adapter = new ComodoController(this, R.layout.single_item_comodo, myList);
        adapter = new AparelhoController(getActivity(), R.layout.single_item_aparelho, myList);
        lv = (ListView) v.findViewById(R.id.listAparelho);
        lv.setAdapter(adapter);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                try {
                    Aparelho selecteditem = adapter.getMyList().get((int) id);
                    android.support.v4.app.Fragment frag = new EstatisticaAparelhoFragment();
                    Bundle b = new Bundle();
                    b.putString("id", String.valueOf(selecteditem.id));
                    frag.setArguments(b);

                    replaceFragment(frag);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        /*lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO  Auto-generated method stub


            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO  Auto-generated method stub
                mode.getMenuInflater().inflate(R.menu.menu_edit_delete, menu);

                return true;

            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                // TODO  Auto-generated method stub
                switch (item.getItemId()) {
                    case R.id.selectAll:
                        //
                        final int checkedCount = myList.size();
                        // If item  is already selected or checked then remove or
                        // unchecked  and again select all
                        adapter.removeSelection();
                        for (int i = 0; i < checkedCount; i++) {
                            lv.setItemChecked(i, true);
                            //  listviewadapter.toggleSelection(i);
                        }
                        // Set the  CAB title according to total checked items

                        // Calls  toggleSelection method from ListViewAdapter Class

                        // Count no.  of selected item and print it
                        mode.setTitle(checkedCount + "  Selecionado");
                        return true;
                    case R.id.delete:
                        // Add  dialog for confirmation to delete selected item
                        // record.
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                getActivity());
                        builder.setMessage("Deseja apagar esse aparelho?");

                        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO  Auto-generated method stub

                            }
                        });
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO  Auto-generated method stub
                                SparseBooleanArray selected = adapter
                                        .getSelectedIds();
                                for (int i = (selected.size() - 1); i >= 0; i--) {
                                    if (selected.valueAt(i)) {
                                        Aparelho selecteditem = adapter.getItem(selected.keyAt(i));
                                        // Remove  selected items following the ids
                                        RequestQueue MyRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                                        @SuppressLint("RestrictedApi") GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());

                                        String url = "https://apiconsumo.herokuapp.com/api/app/del/aparelho/" + String.valueOf(selecteditem.getId());

                                        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url,
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

                                        adapter.remove(selecteditem);

                                        MyRequestQueue.add(req);
                                    }
                                }

                                // Close CAB
                                mode.finish();
                                selected.clear();

                            }
                        });
                        AlertDialog alert = builder.create();
                        //alert.setIcon(R.drawable.common_google_signin_btn_icon_disabled);// dialog  Icon
                        alert.setTitle("Confirmação"); // dialog  Title
                        alert.show();
                        return true;
                    default:
                        return false;
                }

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // TODO  Auto-generated method stub

                final int checkedCount = lv.getCheckedItemCount();
                // Set the  CAB title according to total checked items
                mode.setTitle(checkedCount + "  Selected");
                // Calls  toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
            }
        });*/

        return v;
    }

    public void getListaAparelho(final Comodo comodo) {
        @SuppressLint("RestrictedApi")
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LaparelhoFragment.this.getContext());
        String personId = acct.getId();

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this.getContext());
        int method = Request.Method.GET;
        String url = "https://apiconsumo.herokuapp.com/api/app/src/aparelho/"+ comodo.getId() + "/" + personId;

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

                                Aparelho aparelho = new Aparelho();

                                String nome = c.getString("nome");
                                int id = Integer.parseInt(c.getString("id"));
                                int serial_arduino = c.getInt("arduino_id");
                                boolean status = c.getBoolean("status");
                                String cor = c.getString("color");

                                aparelho.setNome(nome);
                                aparelho.setId(id);
                                aparelho.setComodo_id(comodo.getId());
                                aparelho.setArduino_id(serial_arduino);
                                aparelho.setColor(cor);
                                aparelho.setAtivo(status);
                                contactList.add(aparelho);

                                textViewSemAparelho.setVisibility(View.INVISIBLE);
                            }

                            adapter = new AparelhoController(getActivity(), R.layout.single_item_aparelho, contactList);

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

    public void getListaComodo() {
        @SuppressLint("RestrictedApi")
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LaparelhoFragment.this.getContext());
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
                                //String color = c.getString("color");

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
                            }

                            ArrayAdapter<Comodo> adapter = new ArrayAdapter<Comodo>(LaparelhoFragment.this.getActivity(), android.R.layout.simple_spinner_item, lista);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerLcomodo.setAdapter(adapter);
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
