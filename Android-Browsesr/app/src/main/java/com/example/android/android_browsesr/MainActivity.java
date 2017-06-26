package com.example.android.android_browsesr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView browser_icon,more_icon,menu_icon,exit_icon;
    RelativeLayout download_icon,mail_icon,settings_icon;
    EditText editText;
    Boolean canExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        editText = (EditText)findViewById(R.id.url_editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String urlString = editText.getText().toString();
                editText.setText("");
                if (urlString.equals("")) {
                    Toast.makeText(getBaseContext(),"error: url",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (keyEvent.getKeyCode()==keyEvent.KEYCODE_ENTER ||keyEvent.getAction() == KeyEvent.ACTION_DOWN ){
                    Toast.makeText(getBaseContext(),"enter: url",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,Activity_webView.class);
                    intent.putExtra("urlString",urlString);
                    MainActivity.this.startActivity(intent);
                    return true;
                }
                return false;
            }
        });


        browser_icon = (ImageView)findViewById(R.id.browser_icon);
        browser_icon.setOnClickListener(this);

        more_icon = (ImageView)findViewById(R.id.more_icon);
        more_icon.setOnClickListener(this);

        menu_icon = (ImageView)findViewById(R.id.menu_icon);
        menu_icon.setOnClickListener(this);

        exit_icon = (ImageView)findViewById(R.id.exit_icon);
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
        switch (view.getId()){
            case R.id.browser_icon:
                makeText(getBaseContext(),"version.1.0",Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit_icon:
                if (canExit){
                    canExit =false;
                    android.os.Process.killProcess(android.os.Process.myPid());
                    return ;
                }
                canExit = true;
                Toast.makeText(getBaseContext(),"Oops..exit? click one more time~",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
