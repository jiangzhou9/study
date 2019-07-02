package com.sohu.changeface.opengldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_first_opengl).setOnClickListener(this);
        findViewById(R.id.btn_air_hockey).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_first_opengl:
                startActivity(FirstOpenGLProjectActivity.class);
                break;
            case R.id.btn_air_hockey:
                startActivity(AirHockeyActivity.class);
                break;
        }
    }

    private void startActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }
}
