package com.example.logintesting.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.logintesting.MainActivity;
import com.example.logintesting.R;
import com.example.logintesting.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HomeFragment extends Fragment {

    TextView errRes;
    EditText Uname,Upass;
    Button addU, checkU, deleteU;

    private static final String url = "https://10.0.2.2:7157/api/UserItems";
    private RequestQueue queue;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        //get bindings and root
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //define elements
        Uname = (EditText) root.findViewById(R.id.input_Username);
        Upass = (EditText) root.findViewById(R.id.input_Pass);
        addU = (Button) root.findViewById(R.id.addToListButton);
        checkU = (Button) root.findViewById(R.id.check_user);
        deleteU = (Button) root.findViewById(R.id.delete_user);
        errRes = (TextView) root.findViewById(R.id.error_text);
        errRes.setText("" + ((MainActivity) getActivity()).getToken());

        //create request queue
        queue = Volley.newRequestQueue(this.getContext());

        addU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(Uname.getText().toString().isEmpty()) && !(Upass.getText().toString().isEmpty())){
                    try {
                        addUserToList();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        checkU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(Uname.getText().toString().isEmpty()) && !(Upass.getText().toString().isEmpty())){
                    checkIfUserExists();
                }
            }
        });

        deleteU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
            }
        });

        return root;
    }

    private void checkIfUserExists(){
        String un = Uname.getText().toString();
        String up = Upass.getText().toString();



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


        StringRequest checkUser = new StringRequest(url + "/" + un + "," + up, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    int x = res.getInt("id");
                    errRes.setText("id:" + x);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errRes.setText("User not found/Internal Server Error");
            }
        });

        /** {
         protected Map<String, String> getParams() throws AuthFailureError {
         Map<String, String> params = new HashMap<String, String>();
         params.put("name", un);
         params.put("pass", up);
         return params;
         };
         }*/
        queue.add(checkUser);
    }
    private void addUserToList() throws JSONException {
        String un = Uname.getText().toString();
        String up = Upass.getText().toString();

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

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("id",0);
        jsonBody.put("username",un);
        jsonBody.put("password",up);
        String requestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY",error.toString());
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }};

//        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Uname.setText("");
//                Upass.setText("");
//
//                try {
//                    // on below line we are parsing the response
//                    // to json object to extract data from it.
//                    JSONObject respObj = new JSONObject(response);
//
//                    // below are the strings which we
//                    // extract from our json object.
////                    String name = respObj.getString("name");
////                    String job = respObj.getString("job");
//
//                    // on below line we are setting this string s to our text view.
////                    responseTV.setText("Name : " + name + "\n" + "Job : " + job);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
        /**{
        @Override
        protected Map<String, String> getParams() {
        // below line we are creating a map for
        // storing our values in key and value pair.
        Map<String, String> params = new HashMap<String, String>();

        // on below line we are passing our key
        // and value pair to our parameters.
        params.put("id","0");
        params.put("username", un);
        params.put("password", up);

        // at last we are
        // returning our params.
        return params;
        }
        }*/
        queue.add(postRequest);
    }

    private void deleteUser(){
        String un = Uname.getText().toString();
        String up = Upass.getText().toString();

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url + "/" + un + "," + up, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                errRes.setText("Removed: " + un + ":" + up);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errRes.setText("User Not Found/Internal Server Error");
            }
        });

        queue.add(deleteRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}