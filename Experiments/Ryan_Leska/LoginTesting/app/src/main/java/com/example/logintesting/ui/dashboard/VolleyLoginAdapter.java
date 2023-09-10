package com.example.logintesting.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.logintesting.MainActivity;
import com.example.logintesting.R;

//bad
import javax.net.ssl.TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;
//bad


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VolleyLoginAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{

    private String results[];
    private static final String url = "https://10.0.2.2:7157/api/UserItems";

    private RequestQueue queue;

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_textview;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        queue = Volley.newRequestQueue(parent.getContext());//MainActivity.this
        results = new String[10];
        for(int i = 0; i < 10; i++){
            results[i] = "" + i;
        }
        callList();
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.getView().setText(String.valueOf(results[position]));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public void callList(){
        results[0] = "SENT";
//        notifyItemChanged(0);


        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);


        StringRequest request = new StringRequest(url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    System.out.println(response);
                    JSONArray items = new JSONArray(response);

//                    JSONArray items = resObj.getJSONArray("");

                    results[0] = "Received";
                    int i = 1;
                    while(items.length() > 0){
                        results[i] = items.remove(0).toString();
                        i++;
                    }
                    notifyItemRangeChanged(0,i);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                System.out.println(String.valueOf(volleyError));
                results[0] = "error";
                        notifyItemChanged(0);


            }
        });
        queue.add(request);
    }
}