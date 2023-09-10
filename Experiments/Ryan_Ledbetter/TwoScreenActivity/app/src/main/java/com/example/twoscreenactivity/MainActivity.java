package com.example.twoscreenactivity;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRAVALUE = "numVal";
    public Button go22;
    public Button add;
    public TextView num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String text = intent.getStringExtra(EXTRAVALUE);


        num = (TextView) findViewById(R.id.edittext1);
        if(text != null)
            num.setText(text);
        go22 = (Button) findViewById(R.id.goto2);
        add = (Button) findViewById(R.id.plusbutt);
        go22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent1 = new Intent(MainActivity.this,MainActivity2.class);
                String text = num.getText().toString();
                intent1.putExtra(EXTRAVALUE,text);
                startActivity(intent1);
            }
        }
        );
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                int test = Integer.parseInt((String) num.getText());
                test++;
                num.setText(Integer.toString(test));
            }
        }
        );
    }
}