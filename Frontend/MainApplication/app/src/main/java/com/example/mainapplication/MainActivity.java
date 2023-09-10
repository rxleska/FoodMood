package com.example.mainapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mainapplication.databinding.ActivityMainBinding;
import com.example.mainapplication.datainterface.MiddleMan;
import com.example.mainapplication.datainterface.caching.CacheAccess;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
//    private int token;
//    private File tokenCache;
//    private String tokenCachePath = "tokenCache";

    private CacheAccess cacheAccess;
    private MiddleMan mmController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = sharedPreferences.getBoolean("DarkmodeState", true);
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        cacheAccess = new CacheAccess(getFilesDir().getPath());
        mmController = new MiddleMan(cacheAccess, this);

//        List<Food> listname = new ArrayList<>();
//        mmController.getStorageInterface().getFoods().values().forEach(food -> {
//            listname.add(food);
//        });
        //mmController = new MiddleMan(new MockCache(), this);

//        tokenCache = new File(getFilesDir(), tokenCachePath);
//
//        if (tokenCache.exists()) {
//            try (FileInputStream fis = openFileInput(tokenCachePath)) {
//                byte[] bytes = new byte[fis.available()];
//                fis.read(bytes);
//                token = Integer.parseInt(new String(bytes));
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            token = -1;
//        }

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

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

//    public void setToken(int i){
//        token = i;
//        try (FileOutputStream fos = openFileOutput(tokenCachePath, Context.MODE_PRIVATE)) {
//            fos.write((i + "").getBytes());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public int getToken(){
//        return token;
//    }

    public MiddleMan getMiddleMan() {
        return this.mmController;
    }

    public CacheAccess getCacheAccess() {
        return this.cacheAccess;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMiddleMan().closeFoodUpdateSocket();
    }


}