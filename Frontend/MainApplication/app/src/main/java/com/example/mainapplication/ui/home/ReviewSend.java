package com.example.mainapplication.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mainapplication.R;
import com.example.mainapplication.datainterface.MiddleMan;
import com.example.mainapplication.ui.lists.ReviewList;

/**
 * REVIEW SEND
 *
 * This is the activity that allows you to send reviews
 * It is able to know which food you came from so that
 * you are able to send a review for the specific food you
 * just had.
 *
 * You are able to write a short description of the food as well
 * as score the food on a 5 star scale
 *
 * @author Ryan Ledbetter
 */
public class ReviewSend extends AppCompatActivity {
    /**
     * This is the button that when clicked cancels
     * the review activity
     */
    private Button cancel;
    /**
     * This is the button that when clicked
     * sends the review to the server to be posted
     */
    private Button send;
    /**
     * This stores the current ID of the food you are viewing
     */
    private int currID;
    /**
     * This is the context
     */
    Context context;
    /**
     * This is the Rating bar that allows for the food
     * to be graded on a 5 star scale
     */
    private RatingBar stars;
    /**
     * The text box that allows the user to write
     * a review about the current food
     */
    private TextView reviewT;
    /**
     * Displays current food name
     */
    private TextView foodName;
    /**
     * middleMan is the middleman used to communicate with the server and cache.
     * @see com.example.mainapplication.datainterface.MiddleMan
     * */
    private MiddleMan middleMan;

    /**
     * This activity is how reviews get sent to the server
     * @param savedInstanceState Bundle of intent with data
     * @implNote Within the passed intent, we get the foodID
     * which the review will be for. This allows us to display
     * the food's name as well as associate the review to the food
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_review);
        context = getApplicationContext();

        Intent mIntent = getIntent();
        currID = mIntent.getIntExtra("foodId", 0);
        Log.e("FoodID"," " + currID);

        middleMan = new MiddleMan(getApplicationContext());


        cancel = findViewById(R.id.cancelButton);
        send = findViewById(R.id.sendButton);
        stars = findViewById(R.id.ratingBar);
        reviewT = findViewById(R.id.editReview);
        foodName = findViewById(R.id.currFood);

        foodName.setText(middleMan.getFood(currID).getName());

        cancel.setOnClickListener(v -> finish());

        context = this;
        send.setOnClickListener(v -> {
            postData();
            finish();
        });


    }

    /**
     * Calls Middle Man command to send review data as JSON Object
     */
       private void postData()
    {
        middleMan.addReview((double) stars.getRating(), reviewT.getText().toString(),currID);
    }
}

