package com.example.yq.ndkdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static Context context;

    static {
        System.loadLibrary("jnidemo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        final JNIDemo jniDemo = new JNIDemo();
        final TextView tv = (TextView) findViewById(R.id.tv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText(jniDemo.getHelloWorldText());
            }
        });
    }

    public static void showToast() {
        Toast.makeText(context, "toast", Toast.LENGTH_LONG).show();
    }
}
