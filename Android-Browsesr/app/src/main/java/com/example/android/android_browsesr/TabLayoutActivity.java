package com.example.android.android_browsesr;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Tommy on 2017/6/27.
 */

public class TabLayoutActivity extends AppCompatActivity {

    TabLayout tabLayout;
    RelativeLayout closeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark_history_activity);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        closeButton = (RelativeLayout)findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
