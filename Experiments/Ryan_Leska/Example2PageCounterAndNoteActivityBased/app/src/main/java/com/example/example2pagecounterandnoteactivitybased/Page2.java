package com.example.example2pagecounterandnoteactivitybased;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Page2 extends AppCompatActivity {

    public TextView pageText;
    int value;
    Button changeWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

         value = 0;
//        value = "failed";
        pageText = (TextView) findViewById(R.id.textHere);
        changeWindow = (Button) findViewById(R.id.buttonChangePageF2);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getInt("key");
            //The key argument here must match that used in the other activity
        }


        pageText.setText(Integer.toString(value));

        changeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String value="it worked";
                Intent i = new Intent(Page2.this, MainActivity.class);
                i.putExtra("key",value);
                startActivity(i);
            }
        });



    }
}