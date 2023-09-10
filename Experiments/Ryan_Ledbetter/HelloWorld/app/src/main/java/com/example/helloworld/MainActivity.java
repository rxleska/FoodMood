package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    Button simplebutton1;
    TextView simpleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simplebutton1 = (Button) findViewById(R.id.simplebutton1);
        simpleText = (TextView) findViewById(R.id.textView);
        simplebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                int tsize = (int) simpleText.getTextSize();
                tsize++;
                simpleText.setTextSize(tsize);
            }
            });
        }





    }

