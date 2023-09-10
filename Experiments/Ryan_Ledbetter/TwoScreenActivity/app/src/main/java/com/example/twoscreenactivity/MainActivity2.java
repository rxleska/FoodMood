package com.example.twoscreenactivity;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;

public class MainActivity2 extends AppCompatActivity {

    public Button go21;
    public Button sub;
    public TextView number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent1 = getIntent();
        String text = intent1.getStringExtra(MainActivity.EXTRAVALUE);

        number = (TextView) findViewById(R.id.edittext2);
        number.setText(text);
        go21 = (Button) findViewById(R.id.goto1);
        sub = (Button) findViewById(R.id.subbutt);
        go21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
             {
               Intent intent = new Intent(MainActivity2.this,MainActivity.class);
               String text = number.getText().toString();
               intent.putExtra(MainActivity.EXTRAVALUE,text);
               startActivity(intent);
              }

              }


        );
        sub.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    int test = Integer.parseInt((String) number.getText());
                    test--;
                    number.setText(Integer.toString(test));
                }
        });
    }
}