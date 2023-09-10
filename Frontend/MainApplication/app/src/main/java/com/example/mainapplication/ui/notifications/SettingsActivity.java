package com.example.mainapplication.ui.notifications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.mainapplication.R;
import com.example.mainapplication.datainterface.DataClasses.Profile;
import com.example.mainapplication.datainterface.MiddleMan;
import com.example.mainapplication.datainterface.volleySimplification.VolleyRespInterface;
import com.example.mainapplication.ui.lists.ReviewList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The SettingsActivity is the activity that is opened when the user clicks on the settings button in the bottom right of the account page.
 * It contains information about the user's account, the cache and the darkmode settings.
 *
 * @author Ryan Leska
 * */
public class SettingsActivity extends AppCompatActivity {

    /**
     * The TextView that displays the account information.
     * */
    private TextView accountInformationText;
    /**
     * The Button that is used to log out or log in.
     * */
    private Button accountButton;
    /**
     * The TextView that displays the cache information.
     * */
    private TextView cacheInformationText;
    /**
     * The Button that is used to clear the cache.
     * */
    private Button cacheButton;
    /**
     * The TextView that displays the darkmode information.
     * */
    private TextView darkmodeInformationText;
    /**
     * The Switch that is used to toggle the darkmode.
     * */
    private Switch darkmodeSwitch;

    /**
     * The Button that is used to go back to the previous activity.
     * */
    private Button backButton;

    /**
     * The boolean that is used to check if the darkmode is on or off.
     * */
    private Boolean isNightModeOn;

    /**
     * The TextView that displays the last updated information.
     * */
    private TextView lastUpdatedText;

    /**
     * The TextView that displays the current user information.
     * */
    private TextView currentUserText;

    /**
     * The Button that is used to get the user reviews.
     * */
    private Button getUserReviews;
    /**
     * The Profile object that is used to store the user's profile.
     * */
    private Profile profile;

    /**
     * The onCreate method is called when the activity is created.
     *
     * @param savedInstanceState The Bundle that is used to store the state of the activity.
     *
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        //get bundle from intent
//        Bundle bundle = getIntent().getBundleExtra("context");
//        //get data from bundle
//        Serializable data = bundle.getSerializable("context");
//        OnServerResponse callback = (OnServerResponse) data;



        accountInformationText = findViewById(R.id.userInformationText);
        accountButton = findViewById(R.id.userInformationTextLogoutButton);

        accountInformationText.setText("CHECKING TOKEN");
        accountButton.setVisibility(Button.GONE);
        getUserReviews = findViewById(R.id.UserReviewListButton);

        MiddleMan middleMan = new MiddleMan(this);
        middleMan.isLoggedIn(new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("true")){
                    accountButton.setVisibility(Button.VISIBLE);
                    String tokenText = ("Logged in as: " + middleMan.getToken());
                    accountInformationText.setText(tokenText);
                    accountButton.setText("Go to Logout Page");
                    getUserReviews.setVisibility(Button.VISIBLE);
                }
                else{
                    accountButton.setVisibility(Button.VISIBLE);
                    accountInformationText.setText("Not logged in");
                    accountButton.setText("Login?");
                    getUserReviews.setVisibility(Button.GONE);
                }
            }
        });

        currentUserText = findViewById(R.id.CurrentUserInformation);
        middleMan.getCurrentPublicUserData(new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("error")){
                    getUserReviews.setVisibility(Button.GONE);
                    currentUserText.setText("No Server Connection");
                }
                else if(result.equals("false")){
                    getUserReviews.setVisibility(Button.GONE);
                    currentUserText.setText("No User Logged In");
                }
                else{
                    getUserReviews.setVisibility(Button.VISIBLE);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        profile = new Profile(jsonObject);
                        currentUserText.setText(profile.toString());
                    } catch (JSONException e) {
                        currentUserText.setText("No User Logged In");
                    }
                }
            }
        });


        lastUpdatedText = findViewById(R.id.LastUpdatedDate);
        if(middleMan.getCacheDate() != null) {
            lastUpdatedText.setText("Date Last updated: " + middleMan.getCacheDate().toStringWithoutTime());
        }
        else{
            lastUpdatedText.setText("Date Last updated: Never");
        }
        accountButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(accountButton.getText().equals("Logout")){

                            accountButton.setVisibility(Button.GONE);
                            accountInformationText.setText("Not logged in");
                        }
                        else{
//                            callback.onSuccess(0);
                            finish();
                        }
                    }
                }
        );


        cacheInformationText = findViewById(R.id.cache_information_text);
        cacheButton = findViewById(R.id.cache_clear_button);

        cacheInformationText.setText("Cache size: " + middleMan.getCacheSize());
        cacheButton.setText("Clear cache");
        cacheButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        middleMan.clearCache();
                        String newCacheSize = "Cache size: " + middleMan.getCacheSize();
                        cacheInformationText.setText(newCacheSize);
                        lastUpdatedText.setText("Date Last updated: " + middleMan.getCacheDate().toStringWithoutTime());
                    }
                });



        darkmodeInformationText = findViewById(R.id.darkmode_text);
        darkmodeSwitch = findViewById(R.id.darkmode_switch);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isNightModeOn = sharedPreferences.getBoolean("DarkmodeState", true);


        darkmodeInformationText.setText("Toggle Darkmode");
//        isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES || AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED;
        if (isNightModeOn) {
            darkmodeSwitch.setText("Disable Dark Mode");
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            darkmodeSwitch.setChecked(true);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


            darkmodeSwitch.setText("Enable Dark Mode");
            darkmodeSwitch.setChecked(false);
        }
        darkmodeSwitch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isNightModeOn) {
                            darkmodeSwitch.setText("Enable Dark Mode");
                            sharedPreferences.edit().putBoolean("DarkmodeState", false).apply();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                            isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
                        } else {
                            darkmodeSwitch.setText("Disable Dark Mode");
                            sharedPreferences.edit().putBoolean("DarkmodeState", true).apply();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                            isNightModeOn = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
                        }
                    }
                }
        );

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }
        );




        getUserReviews.setText("VIEW YOUR REVIEWS");
        getUserReviews.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SettingsActivity.this, ReviewList.class);
                        if(profile == null){
                            return;
                        }
                        intent.putExtra("id",profile.getId());
                        intent.putExtra("isFood",false);
                        startActivity(intent);
                    }
                }
        );



    }
}
