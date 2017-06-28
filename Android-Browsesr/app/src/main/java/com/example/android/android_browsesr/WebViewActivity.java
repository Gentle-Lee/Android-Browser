package com.example.android.android_browsesr;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.*;
import android.webkit.DownloadListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.android.android_browsesr.R.id.webview;

/**
 * Created by Tommy on 2017/6/26.
 */

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener{

    private static String urlString ;
    private static String urlName;
    WebView webView;
    ImageView backButton,bookmarkButton;
    TextView urlTextView;
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
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                Intent intent = new Intent(WebViewActivity.this,DownLoadManagerActivity.class);
                intent.putExtra("urlString",s);
                WebViewActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.back_icon:
                if (webView.canGoBack()){
                    webView.goBack();
                }else{
                    finish();
                }
                break;
            case R.id.bookmark_icon:
                if (checkIfExit(urlString)){
                    return;
                }
                SQLiteOpenHelper dbHelper = new BrowserDBHelper(WebViewActivity.this,"webDB",null,2);
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("BookmarkName",urlName);
                contentValues.put("BookmarkUrl",urlString);
                sqLiteDatabase.insertOrThrow("BOOKMARK_DB",null,contentValues);
                sqLiteDatabase.close();
                Toast.makeText(getBaseContext(), "Mark!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.url_TextView:
                Intent intent = new Intent(WebViewActivity.this,MainActivity.class);
                intent.putExtra("urlString",urlString);
                WebViewActivity.this.startActivity(intent);
        }
    }

    void init(){
        webView = (WebView)findViewById(webview);
        Intent intent = getIntent();
        urlString = intent.getStringExtra("urlString");
        if (urlString.contains("www") && urlString.contains(".com")){
            if (!urlString.contains("http://") && !urlString.contains("https://")){
                urlString ="https://"+urlString;
            }
            webView.loadUrl(urlString);
        }else{
            webView.loadUrl("https://www.baidu.com/s?ie=utf-8&wd="+urlString);
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void  onPageStarted(WebView view, String url, Bitmap favicon) {
                urlString = view.getUrl();
                urlName = view.getTitle();
                SQLiteOpenHelper dbHelper = new BrowserDBHelper(WebViewActivity.this,"webDB",null,2);
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("HistoryName",view.getTitle());
                contentValues.put("HistoryUrl",view.getUrl());
                sqLiteDatabase.insertOrThrow("HISTORY_DB",null,contentValues);
                sqLiteDatabase.close();
            }
        });
        WebChromeClient webChromeClient  = new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                urlTextView.setText(view.getTitle());
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
        WebSettings settings = webView.getSettings();
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);//开启DOM缓存，关闭的话H5自身的一些操作是无效的
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
    }
    Boolean checkIfExit(String url){
        BrowserDBHelper dbHelper = new BrowserDBHelper(getBaseContext(),"webDB",null,2);

        SQLiteDatabase sqliteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.query("BOOKMARK_DB",null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String BookmarkUrl = cursor.getString(cursor.getColumnIndex("BookmarkUrl"));
            if (url.equals(BookmarkUrl)){
                Toast.makeText(getBaseContext(),"Already Exit!",Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        cursor.close();
        //关闭数据库
        sqliteDatabase.close();
        return false;
    }

}
