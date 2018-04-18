package com.example.tarun.assigment6final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button mclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mclose = (Button)findViewById(R.id.buttonclose);
        mclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void downloadpage(View v) {
        Intent intent = new Intent(MainActivity.this, Downloadpdf.class);
        startActivity(intent);
    }

}
