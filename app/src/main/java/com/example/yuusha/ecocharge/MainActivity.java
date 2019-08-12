package com.example.yuusha.ecocharge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.yuusha.ecocharge.Fragment.ArduinoFragment;
import com.example.yuusha.ecocharge.Fragment.ConfiguracaoFragment;
import com.example.yuusha.ecocharge.Fragment.InicioFragment;
import com.example.yuusha.ecocharge.Fragment.LaparelhoFragment;
import com.example.yuusha.ecocharge.Fragment.LcomodoFragment;
import com.example.yuusha.ecocharge.Fragment.LserialFragment;
import com.example.yuusha.ecocharge.Fragment.SobreFragment;
import com.example.yuusha.ecocharge.View.LogInActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener {

    private ImageView photoImageView;
    private TextView nameTextView;
    private TextView emailTextView;

    private GoogleApiClient googleApiClient;

    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*sidebar*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*login*/

        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_main);

        photoImageView = (ImageView) hView.findViewById(R.id.photoImageView);
        nameTextView = (TextView) hView.findViewById(R.id.nameTextView);
        emailTextView = (TextView) hView.findViewById(R.id.emailTextView);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        displaySelectScreen(R.id.inicio);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            FragmentManager fm = getSupportFragmentManager();
            boolean mainact = fm.findFragmentByTag(InicioFragment.class.getSimpleName()) != null && fm.findFragmentByTag(InicioFragment.class.getSimpleName()).isVisible();

            if (mainact) {
                this.finishAffinity();
                return;
            } else {
                boolean Lcomodo = fm.findFragmentByTag(LcomodoFragment.class.getSimpleName()) != null && fm.findFragmentByTag(LcomodoFragment.class.getSimpleName()).isVisible();
                boolean lserial = fm.findFragmentByTag(LserialFragment.class.getSimpleName()) != null && fm.findFragmentByTag(LserialFragment.class.getSimpleName()).isVisible();
                boolean Laparelho = fm.findFragmentByTag(LaparelhoFragment.class.getSimpleName()) != null && fm.findFragmentByTag(LaparelhoFragment.class.getSimpleName()).isVisible();

                if (Lcomodo || lserial || Laparelho) {
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        fragment = null;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragment = new ConfiguracaoFragment();
        }
        if (id == R.id.action_sobre) {
            fragment = new SobreFragment();
        }

        if (fragment != null){
            replaceFragment(fragment);
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectScreen(int id){
        fragment = null;

        switch (id){
            case R.id.inicio:
                fragment = new InicioFragment();
                break;
            case R.id.dashboard:
                fragment = new com.example.yuusha.ecocharge.Fragment.EstatisticaGeralFragment();
                break;
            case R.id.ecosense:
                fragment = new ArduinoFragment();
                break;
            case R.id.comodos:
                fragment = new LcomodoFragment();
                break;
            case R.id.aparelhos:
                fragment = new LaparelhoFragment();
                break;
            case R.id.configuracoes:
                fragment = new ConfiguracaoFragment();
                break;
            case R.id.compartilhar:
                android.net.Uri imageUri = android.net.Uri.parse("android.resource://" + getPackageName()
                        + "/drawable/" + "frase.png");

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "BAIXE o App EcoCharge e assuma o Controle");
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/png");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Compartilhar"));
                break;
            case R.id.sair:
                logOut();
                break;
        }

        if (fragment != null){
            replaceFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, someFragment, someFragment.getClass().getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        displaySelectScreen(id);

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();

            String id = account.getId();
            nameTextView.setText(account.getDisplayName());
            emailTextView.setText(account.getEmail());
            Glide.with(this).load(account.getPhotoUrl()).into(photoImageView);

            RequestQueue MyRequestQueue = Volley.newRequestQueue(this);

            String url = "https://apiconsumo.herokuapp.com/api/app/reg/usuario";

            /* JSON*/

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("google_id",account.getId());
            params.put("nome", account.getDisplayName());
            params.put("email", account.getEmail());

            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST,url, new JSONObject(params),
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

        } else {
            goLogInScreen();
        }
    }

    private void goLogInScreen() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.not_close_session, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
