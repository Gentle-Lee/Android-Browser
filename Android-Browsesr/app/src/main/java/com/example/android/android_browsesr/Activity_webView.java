package com.example.android.android_browsesr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.android_browsesr.R.id.webview;

/**
 * Created by Tommy on 2017/6/26.
 */

public class Activity_webView extends AppCompatActivity implements View.OnClickListener{

    private static String urlString ;
    WebView webView;
    ImageView backButton,bookmarkButton;
    TextView urlTextView;
    boolean goBackToHome = false;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh_layout);

        backButton = (ImageView)findViewById(R.id.back_icon);
        backButton.setOnClickListener(this);
        bookmarkButton = (ImageView)findViewById(R.id.bookmark_icon);
        bookmarkButton.setOnClickListener(this);

        urlTextView =(TextView)findViewById(R.id.url_TextView);
        urlTextView.setOnClickListener(this);

        init();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.back_icon:
                if (goBackToHome){
                    goBackToHome = false;
                    finish();
                    return;
                }
                if (webView.canGoBack()){
                    Toast.makeText(getBaseContext(),"go back",Toast.LENGTH_SHORT).show();
                    goBackToHome = false;
                    backButton.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
                    webView.goBack();
                }else{
                    backButton.setImageResource(R.drawable.ic_cancel_black_24dp);
                    goBackToHome = true;
                }
                break;
        }
    }

    void init(){
        webView = (WebView)findViewById(webview);
        Intent intent = getIntent();
        urlString = intent.getStringExtra("urlString");
        if (!urlString.contains("http://") && !urlString.contains("https://")){
            urlString ="https://"+urlString;
        }
        webView.loadUrl(urlString);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        WebChromeClient webChromeClient  = new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                urlTextView.setText(title);
            }

            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    swipeRefreshLayout.setRefreshing(true);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
                super.onProgressChanged(view, newProgress);
            }
        };
        webView.setWebChromeClient(webChromeClient);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!swipeRefreshLayout.canChildScrollUp()) {
                    webView.loadUrl(urlString);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}
