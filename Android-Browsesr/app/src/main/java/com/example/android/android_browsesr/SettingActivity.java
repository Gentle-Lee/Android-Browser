package com.example.android.android_browsesr;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Tommy on 2017/6/28.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {


    RelativeLayout cleanCache,deleteHistory,closeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        cleanCache = (RelativeLayout)findViewById(R.id.clean_cache);
        cleanCache.setOnClickListener(this);

        deleteHistory = (RelativeLayout)findViewById(R.id.delete_history);
        deleteHistory.setOnClickListener(this);

        closeButton =(RelativeLayout) findViewById(R.id.close_button);
        closeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.clean_cache:
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setMessage("Clean All Cache？");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        CookieSyncManager.createInstance(getBaseContext());  //Create a singleton CookieSyncManager within a context
                        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
                        cookieManager.removeAllCookie();// Removes all cookies.
                        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now
                        SettingActivity.this.deleteDatabase("WebView.db");
                        SettingActivity.this.deleteDatabase("WebViewCache.db");
                        Toast.makeText(SettingActivity.this,"Cache Cleaned~",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.delete_history:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingActivity.this);
                builder1.setMessage("Delete All History？");
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SQLiteOpenHelper dbHelper = new BrowserDBHelper(SettingActivity.this,"webDB",null,2);
                        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                        sqLiteDatabase.delete("HISTORY_DB",null,null);
                        sqLiteDatabase.close();
                        Toast.makeText(SettingActivity.this,"History Delete~",Toast.LENGTH_SHORT).show();
                    }
                });
                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.create().show();
                break;
            case R.id.close_button:
                finish();
                break;
        }
    }
}