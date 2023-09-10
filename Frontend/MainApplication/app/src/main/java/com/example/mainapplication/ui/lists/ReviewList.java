package com.example.mainapplication.ui.lists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapplication.R;
import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Review;
import com.example.mainapplication.datainterface.DataClasses.lDate;
import com.example.mainapplication.datainterface.MiddleMan;
import com.example.mainapplication.datainterface.volleySimplification.VolleyRespInterface;
import com.example.mainapplication.ui.home.ReviewSend;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity is used to display the reviews for a food item or the reviews that a user wrote
 *
 * @author Ryan Leska
 * */
public class ReviewList extends AppCompatActivity {


    //UI elements
    /**
     * reviewRV is the recycler view that displays the reviews
     * */
    private RecyclerView reviewRV;

    /**
     * addReview is the floating action button that allows the user to add a review
     * */
    private FloatingActionButton addReview;

    /**
     * The review list instance of the Middle Man class
     * */
    private MiddleMan middleMan;

    // variable for our adapter
    // class and array list
    /**
     * adapter is the adapter for the recycler view
     * */
    private ReviewCourseAdapter adapter;
    /**
     * reviewList is the list of reviews that is displayed in the recycler view
     * */
    private List<Review> reviewList;

    /**
     * progressBar is the progress bar that is displayed while the reviews are being loaded
     * */
    private ProgressBar progressBar;
    /**
     * context is the context of the application
     * */
    private Context context;

    /**
     * id is the id of the food item or user
     * */
    private int id;

    /**
     * isFood is a boolean indicating whether the reviews are for a food item or a user
     * */
    private boolean isFood; //true if food, false if user

    private static WebSocketClient reviewsSocket = null;

    /**
     * This activity is used to display the reviews for a food item or a user
     * @param savedInstanceState Bundle of intent with needed data
     * @implNote
     *  In the intent for this activity, the id of the food item or user is passed in
     *  as well as a boolean indicating whether it is a food item or a user
     *  The id is used to get the reviews from the database
     *  intent id: id
     *  intent isFood: isFood (true if food, false if user)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        context = getApplicationContext();

        if(reviewsSocket == null){
            Draft[] drafts = {
                    new Draft_6455()
            };

            try{
                reviewsSocket = new WebSocketClient(new java.net.URI("ws://10.90.74.141:8080" + "/socket/review"), (Draft) drafts[0]) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Log.e("WEBROCKET", "OPEN");
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.e("WEBROCKET", "MESSAGE: " + message);

                            String[] splitMessage = message.split(":");
                            if(isFood = true){
                                if(id == Integer.parseInt(splitMessage[0])){
                                    getData();
                                }
                            }
                            else{
                                if(id == Integer.parseInt(splitMessage[1])){
                                    getData();
                                }
                            }
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.e("WEBROCKET", "CLOSE");
                        Log.e("WEBROCKET", "CODE: " + code);
                        Log.e("WEBROCKET", "REASON: " + reason);
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.e("WEBSOCKET", "ERROR");
                    }
                };
                reviewsSocket.connect();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        //unpack the intent
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        Log.e("FoodID", " " + id);
        isFood = intent.getBooleanExtra("isFood", false);

        middleMan = new MiddleMan((this.getApplicationContext()));

        // initializing our variables.
        reviewRV = findViewById(R.id.idRVCourses);
        progressBar = findViewById(R.id.idPB);
        addReview = findViewById(R.id.fab);

        middleMan.isLoggedIn(result -> {
           if(!isFood){
               addReview.setVisibility(View.GONE);
               return;
           }
            if(result.equals("error") || result.equals("false")){
               addReview.setVisibility(View.GONE);
           }
           else{
                addReview.setVisibility(View.VISIBLE);
           }
        });


        if(!isFood){
            addReview.setVisibility(View.GONE);
        }
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                addReview();
            }
        });

        getData();

        buildRecyclerView();

    }

    /**
     * The build recycler view method is used to build the recycler view (duh)
     * @implNote This method is called after the data is loaded from the database
     * */
    private void buildRecyclerView() {
//        Log.e("buildRecyclerView", "size: " + reviewList.size());
//        for(Review r : reviewList){
//            Log.e("buildRecyclerView", r.toString());
//        }
        // initializing our adapter class.
        adapter = new ReviewCourseAdapter(reviewList, this);

        // setting layout manager to our recycler view.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reviewRV.setLayoutManager(linearLayoutManager);
        reviewRV.setHasFixedSize(true);

        // setting adapter to our recycler view.
        reviewRV.setAdapter(adapter);
    }

    /**
     * The getData method is used to get the data from the database
     * @implNote This method is called when the activity is created,
     * the data is load from an asyn call to the middle man class
     * */
    private void getData() {
        if(id == 0){ // for mock testing
//            Log.e("mock data call", "data getting");
            reviewList = new ArrayList<>();
            reviewList.add(new Review(1, 1,4.5, "user1", "review1", new lDate(1955,4,15), 1));
            reviewList.add(new Review(2, 1,4.5, "user2", "review2", new lDate(1955,4,15), 1));
            reviewList.add(new Review(3, 1,4.5, "user3", "review3", new lDate(1955,4,15), 1));
            reviewList.add(new Review(4, 1,4.5, "user4", "review4", new lDate(1955,4,15), 1));
            reviewList.add(new Review(5, 1,4.5, "user5", "review5", new lDate(1955,4,15), 1));
            reviewList.add(new Review(6, 1,4.5, "user6", "review6", new lDate(1955,4,15), 1));
            reviewList.add(new Review(7, 1,4.5, "user7", "review7", new lDate(1955,4,15), 1));
            reviewList.add(new Review(8, 1,4.5, "user8", "review8", new lDate(1955,4,15), 1));
            reviewList.add(new Review(9, 1,4.5, "user9", "review9", new lDate(1955,4,15), 1));
            reviewList.add(new Review(10, 1,4.5, "user10", "review10", new lDate(1955,4,15), 1));
            reviewList.add(new Review(11, 1,4.5, "user11", "review11", new lDate(1955,4,15), 1));
            reviewList.add(new Review(12, 1,4.5, "user12", "review12", new lDate(1955,4,15), 1));
            reviewList.add(new Review(13, 1,4.5, "user13", "review13", new lDate(1955,4,15), 1));
            reviewList.add(new Review(14, 1,4.5, "user14", "review14", new lDate(1955,4,15), 1));
            reviewList.add(new Review(15, 1,4.5, "user15", "review15", new lDate(1955,4,15), 1));
            reviewList.add(new Review(16, 1,4.5, "user16", "review16", new lDate(1955,4,15), 1));

            progressBar.setVisibility(View.GONE);
            reviewRV.setVisibility(View.VISIBLE);

            buildRecyclerView();
            return;
        }

        if(isFood){ // FOOD REVIEW LIST
            middleMan.getReviewsByFood(id, new VolleyRespInterface() {
                @Override
                public void onSuccess(String result) {
                    if(result.equals("error") || result.equals("[]")){
                        progressBar.setVisibility(View.VISIBLE);
                        reviewRV.setVisibility(View.GONE);
                        getData();
                        return;
                    }
                    JSONObject json = null;
                    JSONArray jsonArray = null;
                    try {
//                        Log.e("result", result);
//                        json = new JSONObject(result);
//                        jsonArray = json.getJSONArray("reviews");
                        jsonArray = new JSONArray(result);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    reviewList = new ArrayList<>();
                    for(int i = 0; i < jsonArray.length(); i++){
                        try {
                            reviewList.add(new Review(jsonArray.getJSONObject(i)));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    reviewRV.setVisibility(View.VISIBLE);
                    buildRecyclerView();
                }
            });
        }
//        result -> {
//            if(result.equals("error") || result.equals("[]")){
//                progressBar.setVisibility(View.VISIBLE);
//                reviewRV.setVisibility(View.GONE);
//                getData();
//                return;
//            }
//            JSONObject json = null;
//            JSONArray jsonArray = null;
//            try {
//                json = new JSONObject(result);
//                jsonArray = json.getJSONArray("reviews");
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//            reviewList = new ArrayList<>();
//            for(int i = 0; i < jsonArray.length(); i++){
//                try {
//                    reviewList.add(new Review(jsonArray.getJSONObject(i)));
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            progressBar.setVisibility(View.GONE);
//            reviewRV.setVisibility(View.VISIBLE);
//            buildRecyclerView();
//        }

        else{ // USER REVIEW LIST
            middleMan.getReviewsByUserId(id, result -> {
                if(result.equals("error") || result.equals("[]")){
                    progressBar.setVisibility(View.VISIBLE);
                    reviewRV.setVisibility(View.GONE);
                    getData();
                    return;
                }
                JSONObject json = null;
                JSONArray jsonArray = null;
                try {
//                    json = new JSONObject(result);
//                    jsonArray = json.getJSONArray("reviews");
                    jsonArray = new JSONArray(result);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                reviewList = new ArrayList<>();
                for(int i = 0; i < jsonArray.length(); i++){
                    try {
                        reviewList.add(new Review(jsonArray.getJSONObject(i)));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                progressBar.setVisibility(View.GONE);
                reviewRV.setVisibility(View.VISIBLE);
                buildRecyclerView();
            });
        }
    }

    /**
     * The add review button launches the ReviewSend activity to add a review,
     * passing the food id and a boolean to indicate that it is a food review
     * @see com.example.mainapplication.ui.home.ReviewSend
     * */
    private void addReview(){
        Intent myIntent = new Intent(this, ReviewSend.class); // was context instead of this
        myIntent.putExtra("foodId", id);
        myIntent.putExtra("isFood", true);
        this.startActivity(myIntent); // was context instead of this
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        reviewsSocket.close();
    }
}