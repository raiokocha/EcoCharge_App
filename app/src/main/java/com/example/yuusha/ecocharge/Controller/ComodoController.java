package com.example.yuusha.ecocharge.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuusha.ecocharge.Fragment.AparelhoFragment;
import com.example.yuusha.ecocharge.Fragment.ComodoFragment;
import com.example.yuusha.ecocharge.Model.Aparelho;
import com.example.yuusha.ecocharge.Model.Comodo;
import com.example.yuusha.ecocharge.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ComodoController extends ArrayAdapter<Comodo> {

    private List<Comodo> comodoList;
    LayoutInflater inflater;
    private Context mCtx;
    private SparseBooleanArray mSelectedItemsIds;
    private android.support.v4.app.Fragment fragment;
    private Button buttonPopupComodo;

    public ComodoController(Context mCtx, int resourceId, List<Comodo> comodoList) {
        super(mCtx, resourceId, comodoList);
        this.comodoList = comodoList;
        this.mCtx = mCtx;
        mSelectedItemsIds = new SparseBooleanArray();
        inflater =  LayoutInflater.from(mCtx);
    }

    public void showMenu (View view, final int position)
    {
        final Comodo p = comodoList.get(position);

        PopupMenu menu = new PopupMenu (getContext(), view);
        menu.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
        {
            @Override
            public boolean onMenuItemClick (MenuItem item)
            {
                int id = item.getItemId();
                switch (id)
                {
                    case R.id.editar:
                        fragment = new ComodoFragment();

                        Comodo selecteditem = p;

                        Bundle b = new Bundle();
                        b.putString("id", String.valueOf(selecteditem.id));
                        b.putString("nome" , String.valueOf(selecteditem.nome));
                        b.putString("data_criacao", String.valueOf(selecteditem.data_criacao));
                        b.putString("status" , String.valueOf(selecteditem.status));

                        fragment.setArguments(b);
                        replaceFragment(fragment);
                        break;
                    case R.id.delete:
                        // Add  dialog for confirmation to delete selected item
                        // record.
                        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
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
                                RequestQueue MyRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
                                @SuppressLint("RestrictedApi") GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(mCtx);

                                String url = "https://apiconsumo.herokuapp.com/api/app/del/comodo/" + String.valueOf(p.getId()) + "/" + acct.getId();

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

                                comodoList.remove(position);

                                MyRequestQueue.add(req);

                                notifyDataSetChanged();
                            }
                        });
                        AlertDialog alert = builder.create();
                        //alert.setIcon(R.drawable.common_google_signin_btn_icon_disabled);// dialog  Icon
                        alert.setTitle("Confirmação"); // dialog  Title
                        alert.show();
                        break;
                }
                return true;
            }
        });
        menu.inflate(R.menu.menu_edit_delete);
        menu.show();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        this.inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.single_item_comodo, null, true);

        buttonPopupComodo = listViewItem.findViewById(R.id.buttonPopupComodo);
        buttonPopupComodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, position);
            }
        });

        TextView textViewnome = listViewItem.findViewById(R.id.nome);


        Comodo comodo = comodoList.get(position);


        textViewnome.setText((CharSequence) comodo.getNome());


        return listViewItem;
    }

    // get List after update or delete
    public  List<Comodo> getMyList() {
        return comodoList;
    }

    // Item checked on selection
    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
        }
        else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    void replaceFragment(android.support.v4.app.Fragment frag) {
        android.support.v4.app.FragmentManager fm = ((android.support.v7.app.AppCompatActivity) mCtx).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft =  fm.beginTransaction();
        ft.replace(R.id.mainFrame, frag, frag.getClass().getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
    }
}