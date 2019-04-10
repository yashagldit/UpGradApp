package com.upgrad.internshipapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    LinearLayout button_layout;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button_layout=(LinearLayout) findViewById(R.id.button_lay);
        webView=(WebView) findViewById(R.id.webview);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        //webView.getSettings().setPluginsEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                evaluate(url);
                findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
                Log.d("WebView", "your current url when webpage loading.." + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "your current url when webpage loading.. finish" + url);
                evaluate(url);
                findViewById(R.id.progressBar2).setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
    }
    public void login(View view){
        button_layout.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl("https://stackoverflow.com/oauth/dialog?client_id=14871&redirect_uri=https://stackexchange.com/oauth/login_success");
    }
    void evaluate(String url){
        String ab;
        SharedPreferenceUtils sharedPreferenceUtils=new SharedPreferenceUtils(Login.this,"auth");
        if(url.contains("access_token")){
            ab=url.split("=")[1];
            ab=ab.split("&")[0];
            Log.v("Access Token",ab);
            Toast.makeText(Login.this,"Logged In",Toast.LENGTH_SHORT).show();
            sharedPreferenceUtils.setValue("token",ab);
            startActivity(new Intent(Login.this,UserInterest.class));

        }
    }
}
