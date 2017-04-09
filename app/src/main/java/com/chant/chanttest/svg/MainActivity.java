package com.chant.chanttest.svg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.chant.chanttest.R;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.icon_svg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IconSVGActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.icon_vector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IconVectorActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.icon_png).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IconPNGActivity.class);
                startActivity(intent);
            }
        });
    }

}
