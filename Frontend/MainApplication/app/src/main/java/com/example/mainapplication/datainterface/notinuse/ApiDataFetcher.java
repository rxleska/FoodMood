package com.example.mainapplication.datainterface.notinuse;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.DataClasses.Review;
import com.example.mainapplication.datainterface.volleySimplification.VolleyQuick;
import com.example.mainapplication.datainterface.volleySimplification.VolleyRespInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated
 * The purpose of this class was to fetch data from the api.
 *
 * It is not used in the final application.
 *
 * This classes purpose was replaced with the Middle Man.
 * @see com.example.mainapplication.datainterface.MiddleMan
 *
 * @author Ryan Leska
 *
 * */
@Deprecated
public class ApiDataFetcher implements DataFetcher{
    private VolleyQuick volleyQuick;
    private RequestQueue queue;
    JSONObject allDataResponse;

    ApiDataFetcher(Context context){
        // This class is used to fetch data from the api
        volleyQuick = new VolleyQuick();
        queue = Volley.newRequestQueue(context);
        allDataResponse = null;
    }

    @Override
    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        if(allDataResponse == null){
            getRestarauntAndFoodData();
        }
        try {
            JSONArray rests = allDataResponse.getJSONArray("Restaraunts");
            for(int i = 0; i < rests.length(); i++){
                JSONObject rest = rests.getJSONObject(i);
                restaurants.add(new Restaurant(rest));
            }
            return restaurants;
        } catch (JSONException e) {
            return null;
        }
    }

    @Override
    @Deprecated // This method is deprecated because it is not used
    public Restaurant getRestaurant(int id) {
        return null;
    }

    @Override
    public int[] getFoodList(int id) {
        return new int[0];
    }

    @Override
    public int[] getFoodList(String name) {
        return new int[0];
    }

    @Override
    public Food getFood(int id) {
        return null;
    }

    @Override
    public Food getFood(String name) {
        return null;
    }

    @Override
    public int[] getRecommendations() {
        return new int[0];
    }

    @Override
    public Review[] getReviews(int id) {
        return new Review[0];
    }

    @Override
    public Review[] getReviews(String name) {
        return new Review[0];
    }

    @Override
    public Review getReview(int id) {
        return null;
    }

    @Override
    public boolean userLoggedIn(int token) { // I have a bad feeling about this
        JSONObject body = new JSONObject();
        try {
            body.put("token", token);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        final int[] requestId = {-1};
        queue.add(volleyQuick.vGet("/status", new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject response = new JSONObject(result);
                    if(response.getBoolean("loggedIn")){
                        // The user is logged in
                        requestId[0] = 1;
                    }
                    else {
                        // The user is not logged in
                        requestId[0] = 0;
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, body));
        while(requestId[0] == -1){
            // Wait for the request to finish
            continue;
        }
        if(requestId[0] == 1)
            return true;
        else if(requestId[0] == 0){
            return false;
        }
        else{
            throw new RuntimeException("Something went wrong");
        }
    }

    private void getRestarauntAndFoodData(){
        // This method is used to get the data from the api
        // It will call the methods in the storage interface to store the data
    }
}
