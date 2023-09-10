package com.example.mainapplication.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mainapplication.R;
import com.example.mainapplication.databinding.FragmentNotificationsBinding;
import com.example.mainapplication.datainterface.MiddleMan;
import com.example.mainapplication.datainterface.volleySimplification.VolleyRespInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

/**
 * A placeholder fragment containing a simple view.
 * Created through the Android Studio UI
 *
 * @author Ryan Leska
 * */
public class NotificationsFragment extends Fragment {

    /**
     * t is a macro for true
     * */
    private static final boolean t = true;
    /**
     * f is a macro for false
     * */
    private static final boolean f = false;
    /**
     * no is a macro for null String
     * */
    private static final String no = null;
//    public byte responseChecker;
    /**
     * responseText is the TextView that displays the current mode of the login screen
     * */
    TextView responseText;
    /**
     * loginSuccess is the TextView that displays the current status of the login
     * */
    TextView loginSuccess;
    /**
     * Uname is the EditText that holds the username
     * */
    EditText Uname;
    /**
     * Upass is the EditText that holds the password
     * */
    EditText Upass;
    /**
     * UpassConf is the EditText that holds the password confirmation
     * */
    EditText UpassConf;
    /**
     * Accept is the Button that is used to accept the current mode
     * */
    Button Accept;
    /**
     * ChangeModes is the Button that is used to change the current mode
     * */
    Button ChangeModes;
    /**
     * inRegMode is a boolean that is true if the current mode is registration, false if it is login
     * */
    Boolean inRegMode;
    /**
     * url is the url of the server
     * */
    private static final String url = "http://10.90.74.141:8080/";//"http://coms-309-012.class.las.iastate.edu:8080/"; //"https://10.0.2.2:7157/api/UserItems";
    /**
     * queue is the RequestQueue that is used to send requests to the server
     * */
    private RequestQueue queue;
    /**
     * binding is the binding for the fragment
     * */
    private FragmentNotificationsBinding binding;
    /**
     * loggedIn is a boolean that is true if the user is logged in, false if they are not
     * */
    private boolean loggedIn = false;
    /**
     * middleMan is the MiddleMan that is used to communicate with the server
     * */
    private MiddleMan middleMan;
    /**
     * settingsFab is the FloatingActionButton that is used to go to the settings screen
     * */
    private FloatingActionButton settingsFab;
    /**
     * The switch that allows a user to become an admin user upon signing up, mostly for demonstration purposes
     */
    private Switch adminController;

    /**
     * onCreate is called when the fragment is created
     * @param inflater is the inflater for the fragment
     * @param container is the container for the fragment
     * @param savedInstanceState is the saved instance state
     * @return the view for the fragment
     * */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        middleMan = new MiddleMan(this.getContext());



        //Add Elements
        responseText = (TextView) root.findViewById(R.id.Login_Response);
        loginSuccess = (TextView) root.findViewById(R.id.Login_Success_Text);
        Uname = (EditText) root.findViewById(R.id.Login_User_Name);
        Upass = (EditText) root.findViewById(R.id.Login_User_Pass);
        UpassConf = (EditText) root.findViewById(R.id.REG_USER_PASS_CONF);
        Accept = (Button) root.findViewById(R.id.Accept_Button);
        ChangeModes = (Button) root.findViewById(R.id.Change_To_Reg_Mode);
        inRegMode = false;
        adminController = (Switch) root.findViewById(R.id.adminSwitch);

        loginSuccess.setVisibility(View.INVISIBLE);
        queue = Volley.newRequestQueue(this.getContext());



        responseText.setText("LOGIN:");

        // if MainActivity.token is not -1, then the user is already logged in
        if(middleMan.getToken() > 0){
            loggedIn = true;
            everythingVisiblityChanger(t,f,f,f,f,t,f);
            everythingChangeText("You are already logged in", "", f,f,f,"LOGOUT",no);
        }

        adminController.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (adminController.isChecked())
                {
                    adminController.setChecked(true);
                }
                else
                {
                    adminController.setChecked(false);
                }

            }
        });
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminController.setVisibility(View.INVISIBLE);
                if(loggedIn){
                    middleMan.logout(new VolleyRespInterface() {
                        @Override
                        public void onSuccess(String resultString) {
                            Log.e("LOGOUT", resultString);
                            if(resultString.equals("error")){
                                everythingVisiblityChanger(t, f, f, f, f,t, f);
                                everythingChangeText("Server Error Not Logged out, try again later", "", f,f,f,"Logout",no);
                            }
                            else if(resultString.equals("true")){
                                //User Logged out
                                everythingVisiblityChanger(t,t,t,t,f,t,t);
                                everythingChangeText("You have been logged out", "Please Login:", f,f,f,"LOGIN","Register?");
                                loggedIn = false;
                            }
                            else if(resultString.equals("false")){
                                //user not logged out
                                everythingVisiblityChanger(t, f, f, f, f,t, f);
                                everythingChangeText("User not logged out, try again later", "", f,f,f,"Logout",no);
                            }
                            else {
                                everythingVisiblityChanger(t, f, f, f, f,t, f);
                                everythingChangeText("Server Error Not Logged out, try again later", "", f,f,f,"Logout",no);
                            }
                        }
                    });


                    //__________OLD METHOD_____________
//                    logOutUser(new OnServerResponse() {
//                        @Override
//                        public void onSuccess(int result) {
//                            if(result == 0){
//                                //User Logged out
//                                everythingVisiblityChanger(t,t,t,t,f,t,t);
//                                everythingChangeText("You have been logged out", "Please Login:", f,f,f,"LOGIN","Register");
//                                ((MainActivity) getActivity()).setToken(-1);
//                                loggedIn = false;
//                            }
//                            else if(result == -1){
//                                //user not logged out
//                                everythingVisiblityChanger(t, f, f, f, f,t, f);
//                                everythingChangeText("User not logged out", "", f,f,f,no,no);
//                            }
//                            else{
//                                everythingVisiblityChanger(t, f, f, f, f,t, f);
//                                everythingChangeText("Server Error Not Logged out", "", f,f,f,no,no);
//                            }
//                        }
//                    });
                }
                else{
                    if(inRegMode){
                        // first check if the passwords match
                        if(Upass.getText().toString().equals(UpassConf.getText().toString())) {
                            Log.e("ADMIN"," " + adminController.isChecked());
                            middleMan.register(Uname.getText().toString(), Upass.getText().toString(), adminController.isChecked(), new VolleyRespInterface() {
                                @Override
                                public void onSuccess(String resultString) {
                                    if(resultString.equals("error")){
                                        everythingVisiblityChanger(t, f, t, t, t, t, t);
                                        everythingChangeText("Server Error Not Registered", "Try again?", f, f, f, no, no);
                                    }
                                    else{
                                        int result = Integer.parseInt(resultString);
                                        if (result > 0) {
                                            //User registered
                                            everythingVisiblityChanger(t, f, f, f, f, f, f);
                                            everythingChangeText("You have been registered", no, t, t, t, no, no);
                                            inRegMode = false;
                                        } else if (result == -1) {
                                            //User not registered show error
                                            everythingVisiblityChanger(t, f, t, t, t, t, t);
                                            everythingChangeText("Registration failed: Please try again?", "", f, f, f, no, no);
                                        } else if (result == -2) {
                                            //User Already registered show error
                                            everythingVisiblityChanger(t, t, t, t, f, t, t);
                                            everythingChangeText("User Already Registered, please login", "Login:", f, f, t, no, no);
                                            inRegMode = false;
                                        }  else {
                                            everythingVisiblityChanger(t, f, t, t, t, t, t);
                                            everythingChangeText("Server Error Not Registered", "", f, f, f, no, no);
                                        }
                                    }
                                }
                            });
                        }else {
                            //Password Mismatch
                            everythingVisiblityChanger(t, f, t, t, t, t, t);
                            everythingChangeText("Password Mismatch", "", f, f, t, no, no);
                        }
                            //__________OLD METHOD_____________



                        //__________OLD METHOD_____________
//                        registerUser(new OnServerResponse(){
//                            @Override
//                            public void onSuccess(int result) {
//                                switch(result){
//                                    case 0:
//
//                                    case -1:
//                                        //User not registered show error
//                                        everythingVisiblityChanger(t,f,t,t,t,t,t);
//                                        everythingChangeText("Registration failed: Please try again?", "", f,f,f,no,no);
//                                        break;
//                                    case -2:
//                                        //User Already registered show error
//                                        everythingVisiblityChanger(t,f,t,t,f,t,t);
//                                        everythingChangeText("User Already Registered, please login", "", f,f,t,no,no);
//                                        inRegMode = false;
//                                        break;
//                                    case -3:
//                                        //Password Mismatch
//                                        everythingVisiblityChanger(t,f,t,t,t,t,t);
//                                        everythingChangeText("Passwords Do not Match please re-enter password", "", f,f,t,no,no);
//                                        break;
//                                    default:
//                                        //User registered clear change to login and clear fields maybe
//                                        everythingVisiblityChanger(f,t,f,f,f,f,f); // only login Text visible
//                                        everythingChangeText("","Registration completed: " + "token: " + result, t,t,t,no,no);
//                                        ((MainActivity) getActivity()).setToken(result);
//                                        loggedIn = true;
//                                        break;
////                                    //shouldn't happen
////                                    everythingVisiblityChanger(f,t,f,f,f,f,f);
////                                    everythingChangeText(no,"Unknown Error: #HDWGH",t,t,t,no,no);
////                                    break;
//                                }
//                            }
//                        });
                    }
                    else{
                        middleMan.login(Uname.getText().toString(), Upass.getText().toString(), new VolleyRespInterface() {
                            @Override
                            public void onSuccess(String resultString) {
                                if(resultString.equals("error")){
                                    everythingVisiblityChanger(t, t, t, t, f, t, t);
                                    everythingChangeText("Server Error Not Logged in", "Try Again?", f, f, f, "Login","Register");
                                }
                                else{
                                    int result = Integer.parseInt(resultString);
                                    if(result > 0){
                                        //User Logged in
//                                        everythingVisiblityChanger(t,f,f,f,f,f,f);
//                                        everythingChangeText("You have been logged in", "", f,f,f,no,no);
//                                        loggedIn = true;
                                        loggedIn = true;
                                        everythingVisiblityChanger(t,f,f,f,f,t,f);
                                        everythingChangeText("You are already logged in", "", f,f,f,"LOGOUT",no);
                                    }
                                    else if(result < 0){
                                        //user not logged in
                                        everythingVisiblityChanger(t, t, t, t, f,t, t);
                                        everythingChangeText("User not logged in", "Try Again?", f,f,f,"Login","Register");
                                    }
                                    else{
                                        everythingVisiblityChanger(t, t, t, t, f, t, t);
                                        everythingChangeText("Server Error Not Logged in", "Try Again?", f, f, f, "Login","Register");
                                    }
                                }
                            }
                        });
//                        loginUser(new OnServerResponse() {
//                            @Override
//                            public void onSuccess(int result) {
//                                if(result == -3){
//                                    //User Logged in take note of ID, display success
//                                    everythingVisiblityChanger(f,t,f,f,f,f,f); // only login Text visible
////                                    ((MainActivity) getActivity()).setToken(result);
////                                    loggedIn = true;
//                                    everythingChangeText("","User Already Logged In: " + result , t,t,t,no,no);
//                                }
//                                else if(result != -1){
//                                    //User Logged in take note of ID, display success
//                                    everythingVisiblityChanger(f,t,f,f,f,f,f); // only login Text visible
//                                    ((MainActivity) getActivity()).setToken(result);
//                                    loggedIn = true;
//                                    everythingChangeText("","Login Sucessful for id: " + result , t,t,t,no,no);
//                                }
//                                else{
//                                    //Unknown User, prompt to register
//                                    everythingVisiblityChanger(t, f, t, t, f,t, t); // only login Text visible
//                                    everythingChangeText("User not found, please try again\n later or register account",no,f,f,t,no,no);
//                                }
//                            }
//                        });

                    }
                }
            }
        });

        ChangeModes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                middleMan.clearCache();
                if(inRegMode){
                    everythingVisiblityChanger(t,f,t,t,f,t,t);
                    everythingChangeText("Login","",t,t,t,"Login","Click here to Regster");
                    adminController.setVisibility(View.INVISIBLE);
//                    UpassConf.setVisibility(View.INVISIBLE);
//                    responseText.setText("Login");
//                    Accept.setText("Login");
//                    ChangeModes.setText("Click here to register");
                    inRegMode = false;
                }
                else{
                    everythingVisiblityChanger(t,f,t,t,t,t,t);
                    everythingChangeText("Please enter a username and password, \nthen retype the password", "",f,f,t,"Register", "Already have an account");
                    adminController.setVisibility(View.VISIBLE);
//                    UpassConf.setVisibility(View.VISIBLE);
//                    responseText.setText("Please enter a username and password, then retype the password");
//                    Accept.setText("Register");
//                    ChangeModes.setText("Already have an account?");
                    inRegMode = true;

                }
            }
        });

        //Floating Action Button
        settingsFab = root.findViewById(R.id.settingsFloatingActionButton);

        settingsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                middleMan.clearCache();
                //TODO: Add settings menu activity
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);

            }
        });



//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

//    private void logOutUser(final OnServerResponse onServerResponse) {
//        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//            public X509Certificate[] getAcceptedIssuers() {
//                return null;
//            }
//            public void checkClientTrusted(X509Certificate[] certs, String authType) {
//            }
//            public void checkServerTrusted(X509Certificate[] certs, String authType) {
//            }
//        } };
//        SSLContext sc = null;
//        try {
//            sc = SSLContext.getInstance("SSL");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        // Create all-trusting host name verifier
//        HostnameVerifier allHostsValid = new HostnameVerifier() {
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        };
//        // Install the all-trusting host verifier
//        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//
//        JSONObject jsonBody = new JSONObject();
//        try{
////            jsonBody.put("id",0);
//            jsonBody.put("token",((MainActivity) getActivity()).getToken());
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        String requestBody = jsonBody.toString();
//
//
//        StringRequest logoutPut = new StringRequest(Request.Method.PUT, url + "logout", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //Convert string response to JSON object
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if(jsonObject.getBoolean("loggedIn") == false){
//                        onServerResponse.onSuccess(0);
//                    }
//                    else{
//                        onServerResponse.onSuccess(-2);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    onServerResponse.onSuccess(-1);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                onServerResponse.onSuccess(-1);
//            }
//        }){
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                return requestBody.getBytes();
//            }
//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }
//        };
//
//        queue.add(logoutPut);
//    }
//
//    private void registerUser(final OnServerResponse onServerResponse) {
//        String un = Uname.getText().toString();
//        String up = Upass.getText().toString();
//        String upc = UpassConf.getText().toString();
//        if(!up.equals(upc)){
//            onServerResponse.onSuccess(-3);
//            return;
//        }
//
//        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//            public X509Certificate[] getAcceptedIssuers() {
//                return null;
//            }
//            public void checkClientTrusted(X509Certificate[] certs, String authType) {
//            }
//            public void checkServerTrusted(X509Certificate[] certs, String authType) {
//            }
//        } };
//        SSLContext sc = null;
//        try {
//            sc = SSLContext.getInstance("SSL");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        // Create all-trusting host name verifier
//        HostnameVerifier allHostsValid = new HostnameVerifier() {
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        };
//        // Install the all-trusting host verifier
//        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//
//        JSONObject jsonBody = new JSONObject();
//        try{
////            jsonBody.put("id",0);
//            jsonBody.put("username",un);
//            jsonBody.put("password",up);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        String requestBody = jsonBody.toString();
//
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url + "registration", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("respGot","it got");
//                JSONObject resp = null;
//                int id = -1;
//                try {
//                    resp = new JSONObject(response);
//                    id = resp.getInt("token");
//
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//                onServerResponse.onSuccess(id);
////                responseChecker = 0;
//                Log.i("VOLLEY", response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                onServerResponse.onSuccess(-1);
////                responseChecker = 1;
//                Log.e("VOLLEY",error.toString());
//            }
//        }){
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    return requestBody == null ? null : requestBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
//                    return null;
//                }
//            }};
//
////        responseChecker = -1;
//        queue.add(postRequest);
//    }
    /**
     * This method is used to change the visibility of the elements on the screen
     * @param isResTextVisible is the response text visible
     * @param isLoginTextVisible is the login success text visible
     * @param isUnameVisible is the username field visible
     * @param isPassVisible is the password field visible
     * @param isPassConfVisible is the password confirmation field visible
     * @param isAcceptButtonVisible is the accept button visible
     * @param isRegButtonVisible is the register button visible
     * */
    private void everythingVisiblityChanger(boolean isResTextVisible, boolean isLoginTextVisible, boolean isUnameVisible, boolean isPassVisible, boolean isPassConfVisible, boolean isAcceptButtonVisible, boolean isRegButtonVisible){
        responseText.setVisibility(isResTextVisible?View.VISIBLE:View.INVISIBLE);
        loginSuccess.setVisibility(isLoginTextVisible?View.VISIBLE:View.INVISIBLE);
        Uname.setVisibility(isUnameVisible?View.VISIBLE:View.INVISIBLE);
        Upass.setVisibility(isPassVisible?View.VISIBLE:View.INVISIBLE);
        UpassConf.setVisibility(isPassConfVisible?View.VISIBLE:View.INVISIBLE);
        Accept.setVisibility(isAcceptButtonVisible?View.VISIBLE:View.INVISIBLE);
        ChangeModes.setVisibility(isRegButtonVisible?View.VISIBLE:View.INVISIBLE);
    }

    /**
     * This method is used to change the text of the elements on the screen
     * @param resText is the response text
     * @param loginText is the login success text
     * @param clearUname is the username field cleared
     * @param clearPass is the password field cleared
     * @param clearPassConf is the password confirmation field cleared
     * @param acceptButtonText is the accept button text
     * @param regButtonText is the register button text
     * */
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

    /**
     * The loginUser method is used to login the user
     * @param onServerResponse is the callback interface
     * This method is deprecated because it is no longer used, replaced with the middleman
     * @see com.example.mainapplication.datainterface.MiddleMan
     * */
    @Deprecated
    private void loginUser(final OnServerResponse onServerResponse){
        String un = Uname.getText().toString();
        String up = Upass.getText().toString();

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
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
//            jsonBody.put("id",0);
            jsonBody.put("username",un);
            jsonBody.put("password",up);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String requestBody = jsonBody.toString();

        StringRequest postRequest = new StringRequest(Request.Method.PUT,url + "login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("respGot","it got");
                Log.i("VOLLEY", response);
                try {
                    JSONObject resp = new JSONObject(response);
                    int id = resp.getInt("token");
                    onServerResponse.onSuccess(id);
                } catch (JSONException e) {
                    onServerResponse.onSuccess( -1);
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onServerResponse.onSuccess((byte) -1);
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

    /**
     * onDestroyView is used to clear the binding
     * */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}