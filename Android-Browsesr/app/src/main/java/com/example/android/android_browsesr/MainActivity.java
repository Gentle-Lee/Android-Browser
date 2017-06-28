package com.example.android.android_browsesr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView browser_icon, more_icon, menu_icon, exit_icon;
    RelativeLayout download_icon, mail_icon, settings_icon;
    AutoCompleteTextView editText;
    Boolean canExit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        Intent intent = getIntent();
        String intentUrlString = intent.getStringExtra("urlString");
        editText = (AutoCompleteTextView) findViewById(R.id.url_editText);

        initAutoComplete("history",editText);
        editText.setText(intentUrlString);
        saveHistory("history",editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                saveHistory("history",editText);
                String urlString = editText.getText().toString();
                editText.setText("");
                if (urlString.equals("")) {
                    return false;
                }
                if (keyEvent.getKeyCode() == keyEvent.KEYCODE_ENTER || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (!NetWorkUtils.isNetworkConnected(getBaseContext())){
                        Toast.makeText(getBaseContext(),"Network unConnected",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                    intent.putExtra("urlString", urlString);
                    MainActivity.this.startActivity(intent);

                    return true;
                }
                return false;
            }
        });


        browser_icon = (ImageView) findViewById(R.id.browser_icon);
        browser_icon.setOnClickListener(this);

        more_icon = (ImageView) findViewById(R.id.more_icon);
        more_icon.setOnClickListener(this);

        menu_icon = (ImageView) findViewById(R.id.menu_icon);
        menu_icon.setOnClickListener(this);

        exit_icon = (ImageView) findViewById(R.id.exit_icon);
        exit_icon.setOnClickListener(this);

        download_icon = (RelativeLayout) findViewById(R.id.download);
        download_icon.setOnClickListener(this);

        mail_icon = (RelativeLayout) findViewById(R.id.mail);
        mail_icon.setOnClickListener(this);

        settings_icon = (RelativeLayout) findViewById(R.id.settings);
        settings_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.browser_icon:
                makeText(getBaseContext(), "version.1.0", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit_icon:
                if (canExit) {
                    canExit = false;
                    android.os.Process.killProcess(android.os.Process.myPid());
                    return;
                }
                canExit = true;
                Toast.makeText(getBaseContext(), "Oops..exit? click one more time~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_icon:
                Intent intent = new Intent(MainActivity.this, UpTabLayoutActivity.class);
                MainActivity.this.startActivity(intent);
                break;
            case R.id.download:
                Intent intent1 = new Intent(MainActivity.this, DownLoadTabViewLayout.class);
                MainActivity.this.startActivity(intent1);
                break;
            case R.id.mail:
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:2015150060@email.szu.edu.cn"));
                startActivity(data);
                break;

        }
    }


    private void initAutoComplete(String field, AutoCompleteTextView auto) {
        Log.i("", "initAutoComplete: --------------------------------------------init------------------");
        SharedPreferences sp = getSharedPreferences("network_url", 0);
        String longhistory = sp.getString("history", "nothing");
        String[] thisArrays = longhistory.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, thisArrays);
        //只保留最近的50条的记录
        if (thisArrays.length > 50) {
            String[] newArrays = new String[50];
            System.arraycopy(thisArrays, 0, newArrays, 0, 50);
            adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, newArrays);
        }
        auto.setAdapter(adapter);
        auto.setDropDownHeight(700);
        auto.setThreshold(1);
        auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });
    }

    private void saveHistory(String field, AutoCompleteTextView auto) {
        Log.i("", "saveHistory: --------------------------------------------save history------------------");
        String text = auto.getText().toString();
        SharedPreferences sp = getSharedPreferences("network_url", 0);
        String longhistory = sp.getString(field, "nothing");
        if (!longhistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).commit();
        }
    }
}