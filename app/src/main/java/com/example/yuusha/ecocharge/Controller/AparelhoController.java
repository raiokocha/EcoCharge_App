package com.example.yuusha.ecocharge.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yuusha.ecocharge.Fragment.AparelhoFragment;
import com.example.yuusha.ecocharge.Fragment.EstatisticaAparelhoFragment;
import com.example.yuusha.ecocharge.Model.Aparelho;
import com.example.yuusha.ecocharge.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AparelhoController extends ArrayAdapter<Aparelho>{

    private List<Aparelho> aparelhoList;
    LayoutInflater inflater;
    private Context mCtx;
    private SparseBooleanArray mSelectedItemsIds;
    private ImageView imgView;
    private Button buttonPopupAparelho;

    public AparelhoController(Context mCtx, int resourceId, List<Aparelho> aparelhoList) {
        super(mCtx, resourceId, aparelhoList);
        this.aparelhoList = aparelhoList;
        this.mCtx = mCtx;
        mSelectedItemsIds = new SparseBooleanArray();
        inflater =  LayoutInflater.from(mCtx);

    }

    public void showMenu (View view, final int position)
    {
        final Aparelho p = aparelhoList.get(position);

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
                        android.support.v4.app.Fragment fragment = new AparelhoFragment();

                        Aparelho selecteditem = p;

                        Bundle b = new Bundle();
                        b.putString("id", String.valueOf(selecteditem.id));
                        b.putString("arduino_id" , String.valueOf(selecteditem.arduino_id));
                        b.putString("nome", String.valueOf(selecteditem.nome));
                        b.putBoolean("status" , selecteditem.isAtivo());
                        b.putString("comodo_id", String.valueOf(selecteditem.comodo_id));
                        b.putString("color", String.valueOf(selecteditem.color));

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

                                String url = "https://apiconsumo.herokuapp.com/api/app/del/aparelho/" + String.valueOf(p.getId());

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

                                aparelhoList.remove(position);

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
        final int position1 = position;


        this.inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.single_item_aparelho, null, true);

        TextView textViewnome = listViewItem.findViewById(R.id.nome);

        buttonPopupAparelho = listViewItem.findViewById(R.id.buttonPopupAparelho);
        buttonPopupAparelho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, position);
            }
        });

        Switch sw = (Switch) listViewItem.findViewById(R.id.switch_ativar_aparelho);
        sw.setChecked(aparelhoList.get(position).isAtivo);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    List<Aparelho> lista = new ArrayList<>();
                    for (int i = 0; i < aparelhoList.size(); i++) {
                        Aparelho itemLista = getMyList().get(i);

                        if (i != position1) {
                            if (itemLista.isAtivo && itemLista.getArduino_id() == getMyList().get(position).getArduino_id()) {
                                itemLista.setAtivo(false);
                                toggleAtivo(itemLista);
                            }
                        } else {
                            if (!itemLista.isAtivo && itemLista.getArduino_id() == getMyList().get(position).getArduino_id())
                                itemLista.setAtivo(true);
                            else if (itemLista.isAtivo && itemLista.getArduino_id() == getMyList().get(position).getArduino_id()) {
                                itemLista.setAtivo(false);
                            }

                            toggleAtivo(itemLista);
                        }

                        lista.add(itemLista);
                    }

                    aparelhoList = lista;

                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            notifyDataSetChanged();
                        }
                    }, 250);

                } else {
                    getMyList().get(position1).setAtivo(false);


                    toggleAtivo(getMyList().get(position1));

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            notifyDataSetChanged();
                        }
                    }, 250);
                }
            }
        });

        Aparelho aparelho = aparelhoList.get(position);


        textViewnome.setText((CharSequence) aparelho.getNome());


        return listViewItem;
    }

    // get List after update or delete
    public  List<Aparelho> getMyList() {
        return aparelhoList;
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

    public void toggleAtivo(Aparelho aparelho) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(mCtx);
        final String ativado = aparelho.isAtivo() ? "ativado" : "desativado";
        String url = "https://apiconsumo.herokuapp.com/api/app/edit/aparelho/" + aparelho.getId();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            Toast.makeText(mCtx, "Aparelho " + ativado, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(mCtx, "Ocorreu um erro ao"  + ativado + "este aparelho. " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyRequestQueue.add(req);
    }

    void replaceFragment(android.support.v4.app.Fragment frag) {
        android.support.v4.app.FragmentManager fm = ((android.support.v7.app.AppCompatActivity) mCtx).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft =  fm.beginTransaction();
        ft.replace(R.id.mainFrame, frag, frag.getClass().getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
    }

}