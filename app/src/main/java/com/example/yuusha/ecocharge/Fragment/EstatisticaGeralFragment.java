package com.example.yuusha.ecocharge.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.yuusha.ecocharge.R;


public class EstatisticaGeralFragment extends Fragment{

    Fragment fragment = null;
    WebView webviewaparelho;
    String personId;
    WebView view;
    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_estatisticageral, container, false);

        mySwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.swipeContainer);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        webviewaparelho.reload(); // refreshes the WebView

                        if (null != mySwipeRefreshLayout) {
                            mySwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }

        );

        webviewaparelho = v.findViewById(R.id.webviewgeral);
        webviewaparelho.getSettings().setJavaScriptEnabled(true);
        webviewaparelho.setWebViewClient(new WebViewClient());
        webviewaparelho.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        webviewaparelho.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        try {
            @android.annotation.SuppressLint("RestrictedApi")
            com.google.android.gms.auth.api.signin.GoogleSignInAccount acct = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(EstatisticaGeralFragment.this.getContext());
            personId = acct.getId();

        } catch (Exception ex) {
            personId = "0";
            ex.printStackTrace();
        }

        try {
            if (personId.length() > 0)
                webviewaparelho.loadUrl("https://apiconsumo.herokuapp.com/api/app/src/estatisticas/gerais/" + personId);
            else {
                fragment = new InicioFragment();
                replaceFragment(fragment);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return v;
    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, someFragment, someFragment.getClass().getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
    }
}