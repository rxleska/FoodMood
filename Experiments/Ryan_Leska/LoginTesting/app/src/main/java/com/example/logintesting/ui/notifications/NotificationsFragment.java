package com.example.logintesting.ui.notifications;

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
import com.example.logintesting.databinding.FragmentNotificationsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NotificationsFragment extends Fragment {

    private static final boolean t = true;
    private static final boolean f = false;
    private static final String no = null;
//    public byte responseChecker;
    TextView responseText, loginSuccess;
    EditText Uname, Upass, UpassConf;
    Button Accept, ChangeModes;
    Boolean inRegMode;
    private static final String url = "https://10.0.2.2:7157/api/UserItems";
    private RequestQueue queue;
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Add Elements
        responseText = (TextView) root.findViewById(R.id.Login_Response);
        loginSuccess = (TextView) root.findViewById(R.id.Login_Success_Text);
        Uname = (EditText) root.findViewById(R.id.Login_User_Name);
        Upass = (EditText) root.findViewById(R.id.Login_User_Pass);
        UpassConf = (EditText) root.findViewById(R.id.REG_USER_PASS_CONF);
        Accept = (Button) root.findViewById(R.id.Accept_Button);
        ChangeModes = (Button) root.findViewById(R.id.Change_To_Reg_Mode);
        inRegMode = false;

        queue = Volley.newRequestQueue(this.getContext());



        responseText.setText("LOGIN:");


        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inRegMode){
                    registerUser(new OnServerResponse(){
                        @Override
                        public void onSuccess(int result) {
                            switch(result){
                                case 0:
                                    //User registered clear change to login and clear fields maybe
                                    everythingVisiblityChanger(f,t,f,f,f,f,f); // only login Text visible
                                    everythingChangeText("","Registration completed", t,t,t,no,no);
                                    break;
                                case 1:
                                    //User not registered show error
                                    everythingVisiblityChanger(t,f,t,t,t,t,t);
                                    everythingChangeText("Registration failed: Please try again?", "", f,f,f,no,no);
                                    break;
                                case 2:
                                    //User Already registered show error
                                    everythingVisiblityChanger(t,f,t,t,f,t,t);
                                    everythingChangeText("User Already Registered, please login", "", f,f,t,no,no);
                                    inRegMode = false;
                                    break;
                                case 3:
                                    //Password Mismatch
                                    everythingVisiblityChanger(t,f,t,t,t,t,t);
                                    everythingChangeText("Passwords Do not Match please re-enter password", "", f,f,t,no,no);
                                    break;
                                default:
                                    //shouldn't happen
                                    everythingVisiblityChanger(f,t,f,f,f,f,f);
                                    everythingChangeText(no,"Unknown Error: #HDWGH",t,t,t,no,no);
                                    break;
                            }
                        }
                    });
                }
                else{
                    loginUser(new OnServerResponse() {
                        @Override
                        public void onSuccess(int result) {
                            if(result != -1){
                                //User Logged in take note of ID, display success
                                everythingVisiblityChanger(f,t,f,f,f,f,f); // only login Text visible
                                ((MainActivity) getActivity()).setToken(result);
                                everythingChangeText("","Login Sucessful for id: " + result , t,t,t,no,no);
                            }
                            else{
                                //Unknown User, prompt to register
                                everythingVisiblityChanger(t, f, t, t, f,t, t); // only login Text visible
                                everythingChangeText("User not found, please try again\n later or register account",no,f,f,t,no,no);
                            }
                        }
                    });

                }
            }
        });

        ChangeModes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inRegMode){
                    everythingVisiblityChanger(t,f,t,t,f,t,t);
                    everythingChangeText("Login","",t,t,t,"Login","Click here to Regster");
//                    UpassConf.setVisibility(View.INVISIBLE);
//                    responseText.setText("Login");
//                    Accept.setText("Login");
//                    ChangeModes.setText("Click here to register");
                    inRegMode = false;
                }
                else{
                    everythingVisiblityChanger(t,f,t,t,t,t,t);
                    everythingChangeText("Please enter a username and password, \nthen retype the password", "",f,f,t,"Register", "Already have an account");
//                    UpassConf.setVisibility(View.VISIBLE);
//                    responseText.setText("Please enter a username and password, then retype the password");
//                    Accept.setText("Register");
//                    ChangeModes.setText("Already have an account?");
                    inRegMode = true;

                }
            }
        });

//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void registerUser(final OnServerResponse onServerResponse) {
        String un = Uname.getText().toString();
        String up = Upass.getText().toString();
        String upc = UpassConf.getText().toString();
        if(!up.equals(upc)){
            onServerResponse.onSuccess(3);
            return;
        }

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
        try{
            jsonBody.put("id",0);
            jsonBody.put("username",un);
            jsonBody.put("password",up);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String requestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("respGot","it got");
                onServerResponse.onSuccess(0);
//                responseChecker = 0;
                Log.i("VOLLEY", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onServerResponse.onSuccess( 1);
//                responseChecker = 1;
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

//        responseChecker = -1;
        queue.add(postRequest);
    }

    private void everythingVisiblityChanger(boolean isResTextVisible, boolean isLoginTextVisible, boolean isUnameVisible, boolean isPassVisible, boolean isPassConfVisible, boolean isAcceptButtonVisible, boolean isRegButtonVisible){
        responseText.setVisibility(isResTextVisible?View.VISIBLE:View.INVISIBLE);
        loginSuccess.setVisibility(isLoginTextVisible?View.VISIBLE:View.INVISIBLE);
        Uname.setVisibility(isUnameVisible?View.VISIBLE:View.INVISIBLE);
        Upass.setVisibility(isPassVisible?View.VISIBLE:View.INVISIBLE);
        UpassConf.setVisibility(isPassConfVisible?View.VISIBLE:View.INVISIBLE);
        Accept.setVisibility(isAcceptButtonVisible?View.VISIBLE:View.INVISIBLE);
        ChangeModes.setVisibility(isRegButtonVisible?View.VISIBLE:View.INVISIBLE);
    }

    private void everythingChangeText(String resText,String loginText, boolean clearUname, boolean clearPass, boolean clearPassConf, String acceptButtonText, String regButtonText){
        if(resText != null){
            responseText.setText(resText);
        }
        if(loginText != null){
            loginSuccess.setText(loginText);
        }
        if(clearUname){
            Uname.setText("");
        }
        if(clearPass){
            Upass.setText("");
        }
        if(clearPassConf){
            UpassConf.setText("");
        }
        if(acceptButtonText != null){
            Accept.setText(acceptButtonText);
        }
        if(regButtonText != null){
            ChangeModes.setText(regButtonText);
        }
    }

    private void loginUser(final OnServerResponse onServerResponse){
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

        StringRequest postRequest = new StringRequest(url + "/" + un + "," + up, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("respGot","it got");
                Log.i("VOLLEY", response);
                try {
                    JSONObject resp = new JSONObject(response);
                    int id = resp.getInt("id");
                    ((MainActivity) getActivity()).setToken(id);
                    onServerResponse.onSuccess(id);
                } catch (JSONException e) {
                    onServerResponse.onSuccess( -1);
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onServerResponse.onSuccess( -1);
//                responseChecker = 1;
                Log.e("VOLLEY",error.toString());
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }};

//        responseChecker = -1;
        queue.add(postRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}