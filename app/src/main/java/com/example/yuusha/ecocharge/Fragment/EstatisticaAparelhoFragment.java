package com.example.yuusha.ecocharge.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.yuusha.ecocharge.R;


public class EstatisticaAparelhoFragment extends Fragment{

    Fragment fragment = null;
    WebView webviewaparelho;
    int id_aparelho;
    WebView view;
    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_estatisticaaparelho, container, false);

        webviewaparelho = v.findViewById(R.id.webviewaparelho);
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
            id_aparelho = Integer.parseInt((String)this.getArguments().get("id"));
        } catch (Exception ex) {
            id_aparelho = 0;
            ex.printStackTrace();
        }

        try {
            if (id_aparelho > 0)
                webviewaparelho.loadUrl("https://apiconsumo.herokuapp.com/api/app/src/estatisticas/aparelho/" + id_aparelho);
            else {
                fragment = new LaparelhoFragment();
                replaceFragment(fragment);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

        return v;
    }


    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, someFragment, someFragment.getClass().getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
    }
}