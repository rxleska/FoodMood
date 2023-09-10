package com.example.apitesting;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView hw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hw = (TextView) findViewById(R.id.hwText);

        String url = "https://93ee1c56-fb69-43bf-81dc-d02ab97f66ff.mock.pstmn.io/APItest";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    System.out.println(response);
                    JSONObject resObj = new JSONObject(response);
                    String title = resObj.getString("text");

                    hw.setText(title);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
//                System.out.println(String.valueOf(volleyError));

            }
        });
        hw.setText("sending request");
        queue.add(request);
    }
}