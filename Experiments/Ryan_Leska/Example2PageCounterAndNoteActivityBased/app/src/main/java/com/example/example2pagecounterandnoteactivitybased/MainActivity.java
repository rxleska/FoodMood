package com.example.example2pagecounterandnoteactivitybased;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Button increment;
    Button changeWindow;
    TextView countText;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        count = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            count = extras.getInt("key");
            //The key argument here must match that used in the other activity
        }

        increment = (Button) findViewById(R.id.buttonInc);
        changeWindow = (Button) findViewById(R.id.buttonChangePageF1);
        countText = (TextView) findViewById(R.id.textViewCount);
        countText.setText(Integer.toString(count));

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                countText.setText(Integer.toString(count));
            }
        });

        changeWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String value="it worked";
                Intent i = new Intent(MainActivity.this, Page2.class);
                i.putExtra("key",count);
                startActivity(i);
            }
        });


    }
}