package com.example.mainapplication.datainterface;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.DataClasses.RestaurantHours;
import com.example.mainapplication.datainterface.DataClasses.Review;
import com.example.mainapplication.datainterface.DataClasses.lDate;
import com.example.mainapplication.datainterface.caching.StorageInterface;
import com.example.mainapplication.datainterface.volleySimplification.VolleyQuick;
import com.example.mainapplication.datainterface.volleySimplification.VolleyRespInterface;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * THE MIDDLE MAN
 *
 * This class is used to get data from the api and from the cache
 * It is also used to store data in the cache
 * ie the middle man between the api and the cache and you.
 * all variables are static so that they can be accessed from anywhere
 * the only thing that is not static is the queue, which is used to make requests in the current context
 * the queue is passed to the constructor, so that it can be used in the current context
 * the main activity is the only class that should be calling the constructor with the storage interface
 *
 * @author Ryan Leska
 */
public class MiddleMan {
    /**
     * The token is used to authenticate the user, it is stored in the cache and checked on app open to see if the user is still logged in
     * */
    private static int token;
    /**
     * List of restaurants, stored in the cache and updated at different times
     * */
    private static List<Restaurant> restaurants;
    /**
     * Map of foods, stored in the cache and updated at different times
     * */
    private static Map<Integer, Food> foods;
    /**
     * VolleyQuick is a class that simplifies the use of volley
     * @see com.example.mainapplication.datainterface.volleySimplification.VolleyQuick
     * */
    private static VolleyQuick volleyQuick = new VolleyQuick();

    /**
     * Volley queue, used to make requests in the current context
     * @implNote  changes with new instances of the class
     * */
    private RequestQueue queue;

    /**
     * The storage interface is used to store and retrieve data from the cache
     * @see com.example.mainapplication.datainterface.caching
     * */
    private static StorageInterface storageInterface;

    /**
     * Websocket for live food score updating
     * */
    private static WebSocketClient foodUpdateSocket = null;

    /**
     * The constructor is used to initialize the queue and the storage interface
     * the constructor also checks if the user is logged in and updates the restaurants and foods lists
     *
     * @param x the storage interface to be used to access the cache, can be mocked for testing
     * @param context the current context
     *
     * @implNote this constructor is only called by the main activity, and only once
     * for other instances of the class, use the constructor without the storage interface parameter
     * */
    public MiddleMan(StorageInterface x, Context context){
        storageInterface = x;
        queue = Volley.newRequestQueue(context);
        if(token == 0){
            token = storageInterface.getToken();
        }
        if(restaurants == null){
            restaurants = storageInterface.getRestaurants();
            if(restaurants == null || restaurants.size() == 0){
                getRestaurantsAsync();
            }
        }
        if(foods == null) {
            foods = storageInterface.getFoods();
            if(foods == null || foods.size() == 0){
                getAllFoodDataAsync();
            }
            else {
                Log.e("FOODS", "FOOD SIZE: " + foods.size());
            }
        }
        this.isLoggedIn(new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("error")){
                    token = 0;
                }
                else if(result.equals("false")){
                    token = 0;
                }
                else{
                    // DO NOTHING
                }

            }
        });

        if(foodUpdateSocket == null){
            Draft[] drafts = {
                    new Draft_6455()
            };

            try{
                foodUpdateSocket = new WebSocketClient(new java.net.URI("ws://10.90.74.141:8080" + "/socket/food"), (Draft) drafts[0]) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        Log.e("WEBSOCKET", "OPEN");
                    }

                    @Override
                    public void onMessage(String message) {
                        Log.e("WEBSOCKET", "MESSAGE: " + message);
                        try {
                            JSONObject messageJson = new JSONObject(message);
                            Food food = new Food(messageJson);
                            foods.put(food.getId(), food);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {
                        Log.e("WEBSOCKET", "CLOSE");
                        Log.e("WEBSOCKET", "CODE: " + code);
                        Log.e("WEBSOCKET", "REASON: " + reason);
                        storageInterface.storeFoods(foods);
                    }

                    @Override
                    public void onError(Exception ex) {
                        Log.e("WEBSOCKET", "ERROR");
                    }
                };
                foodUpdateSocket.connect();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        updateAllData(storageInterface.getLastUpdatedDate(), new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                result = result.replace("null", "\"\""); // this does nothing on purpose
                getHoursListing(result2 -> {
                    result2 = result2.replace("null", "\"\""); // this does nothing on purpose
                });
            }
        });


    }

    /**
     * The constructor is used to initialize the queue tied to the current context
     *
     * @param context the current context
     * */
    public MiddleMan(Context context){
        queue = Volley.newRequestQueue(context);
    }

    //LOGIN PAGE METHODS

    /**
     * LOGIN
     * Asynchronous method that calls the callback when the login is complete
     * the callback will be passed the token or a negative number if the login failed
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param callback the callback to be called when the login is complete
     *
     * @implNote the callback is returned as a string, so it must be parsed to an int
     */
    public void login(String username, String password, VolleyRespInterface callback){
        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue.add(volleyQuick.vPut("/login", (new VolleyRespInterface () {
            @Override
            public void onSuccess(String response) {
                Log.e("VOLLEY:RES:", response);
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    int resToken = responseJson.getInt("token");
                    token = resToken;
                    storageInterface.storeToken(resToken);
                    callback.onSuccess(""   + resToken);
                } catch (JSONException e) {
                    token = -2;
                    callback.onSuccess("-2");
                }
            }
        }), body));
    }
    /**
     * NOCALLBACKLOGIN
     * void method that logs in without a callback
     * generally don't use this, but it's here if you must for some reason ie testing
     * @param username the username of the user
     * @param password the password of the user
     */
    public void login(String username, String password){
        login(username, password, (response) -> {});
    }


    /**
     * LOGOUT
     * Asynchronous method that calls the callback when the logout is complete
     *
     * @param callback the callback to be called when the logout is complete
     *
     * @implNote the callback is returned as a string boolean, true if the logout was successful
     */
    public void logout(VolleyRespInterface callback){
        JSONObject body = new JSONObject();
        try {
            Log.e("VOLLEY:LOGOUT", "token: " + token);
            body.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue.add(volleyQuick.vPut("/logout", (new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                Log.e("VOLLEY:RES:", result);
                if(result.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try {
                    JSONObject responseJson = new JSONObject(result);
                    String status = responseJson.getString("status");
//                    boolean success = responseJson.getBoolean("loggedIn");
                    if(status.equals("logged out")){
                        token = -100;
                        storageInterface.storeToken(-100);
                        callback.onSuccess("true");
                        return;
                    }
                    else{
                        callback.onSuccess("false");
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                callback.onSuccess("error");
            }
        }), body));
    }
    /**
     * NOCALLBACKLOGOUT
     * void method that logs out without a callback
     * generally don't use this, but it's here if you must for some reason ie testing
     */
    public void logout(){
        logout((response) -> {});
    }

    /**
     * REGISTER
     * Asynchronous method that calls the callback when the register is complete
     * the callback will be passed the token or a negative number if the register failed
     * @param username the username of the user
     * @param password the password of the user
     * @param callback the callback to be called when the register is complete
     * @callback the callback is returned as a string, so it must be parsed to an int
     * @implNote  MAKE SURE TO CHECK THAT BOTH PASSWORD FIELDS MATCH BEFORE CALLING THIS METHOD
     */
    public void register(String username, String password, boolean isAdmin, VolleyRespInterface callback){


        JSONObject body = new JSONObject();
        try {
            body.put("username", username);
            body.put("password", password);
            Log.e("ADMIN" , " "+ isAdmin);
            body.put("isAdmin", isAdmin);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue.add(volleyQuick.vPost("/registration", (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                Log.e("VOLLEY:RES:", response);
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    int resToken = responseJson.getInt("token");
                    token = resToken;
                    storageInterface.storeToken(resToken);
                    callback.onSuccess(""   + resToken);
                } catch (JSONException e) {
                    token = -1;
                    callback.onSuccess("-1");
                }
            }
        }), body));
    }
    /**
     * NOCALLBACKREGISTER
     * void method that registers without a callback
     * generally don't use this, but it's here if you must for some reason ie testing
     * @param username the username of the user
     * @param password the password of the user
     */
    public void register(String username, String password, boolean isAdmin){
        register(username, password, isAdmin, (response) -> {});
    }

    /**
     * IS LOGGED IN
     * Asynchronous method that calls the callback when the is logged in is complete
     * @implNote the callback is returned as a string, so it must be parsed to a boolean
     * @param callback the callback to be called when the is logged in is complete
     * @callback the callback will be passed true if the user is logged in, false otherwise
     */
    public void isLoggedIn(VolleyRespInterface callback){
        // old code used json body
//        JSONObject body = new JSONObject();
//        try {
//            body.put("token", token);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String urlExtension = "/status?token=" + token;
        queue.add(volleyQuick.vGet(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    boolean resToken = responseJson.getBoolean("loggedIn");
                    callback.onSuccess(""   + resToken);
                } catch (JSONException e) {
                    callback.onSuccess("false");
                }
            }
        }))); //, body
    }
    //NOCALLBACKISLOGGEDIN DOES NOT EXIST BECAUSE IT IS NOT NEEDED, what would this even do?


    /**
     * Get Current Public User Data
     * Asynchronous method that calls the callback when the get current public user data is complete
     * @param callback the callback to be called when the get current public user data is complete
     * @callback the callback will be passed a Json String that can be parsed into a Profile object
     */
    public void getCurrentPublicUserData(VolleyRespInterface callback){
        if(token <= 0){
            callback.onSuccess("false");
            return;
        }

        String urlExtension = "/status/info?token=" + token;
        queue.add(volleyQuick.vGet(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    callback.onSuccess(responseJson.toString());
                } catch (JSONException e) {
                    callback.onSuccess("error");
                }
            }
        })));
    }


    /**
     * GET TOKEN
     * Synchronous method that returns the token
     * @return the token of the user
     */
    public int getToken(){
        return token;
    }

    /**
     * SET TOKEN
     * Synchronous method that sets the token
     * @param token the token of the user
     */
    public void setToken(int token){
        this.token = token;
        storageInterface.storeToken(token);
    }


    //______________________________________________________________________________________________
    //______________________________________________________________________________________________

    //RESTAURANT LIST PAGE METHODS

    /**
     * GET RESTAURANTS
     * Semi Synchronous method that returns the restaurants,
     * @implNote Restaurant list is Asyncronously built when the class is initialized,
     * so this method will return null if the list is not yet built
     * @return the restaurants list
     */
    public List<Restaurant> getRestaurants(){
        return restaurants;
    }

    /**
     * GET RESTAURANTS ASYNC
     *
     * @param callback the callback to be called when the restaurants is complete
     *
     * @callback the callback will be passed a String: true if the data was successfully retrieved, false otherwise
     *
     * @implNote
     * Call if get restaurants returns null
     * Asynchronous method that calls the callback when the restaurants is complete
     * Once the callback returns true, the restaurants list can be accessed, using getRestaurants()
     */
    public void getRestaurantsAsync(VolleyRespInterface callback){
//        getAllDataAsync(callback); // REPLACED WITH TYPE BASED CALL
        getAllFoodDataAsync();
        queue.add(volleyQuick.vGet("/restaurants", (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    response = response.replaceAll("’", "'");
                    JSONObject responseJson = new JSONObject(response);

                    JSONArray responseArray = responseJson.getJSONArray("Restaurants");
                    lDate newEstDate = storageInterface.getLastUpdatedDate();
                    if(newEstDate == null){
                        newEstDate = new lDate(0, 0 ,0 ,0 ,0);
                    }
                    restaurants = new ArrayList<>();
                    for(int i = 0; i < responseArray.length(); i++){
                        JSONObject restaurant = responseArray.getJSONObject(i);
                        restaurants.add(new Restaurant(restaurant));
                        if(newEstDate.compareTo(restaurants.get(i).getLastUpdated())){
                            newEstDate = restaurants.get(i).getLastUpdated();
                        }
                        Log.e("RESTAURANTS", "DATE: " +  restaurants.get(i).getLastUpdated().toString());
//                        Log.e("RESTAURANTS", restaurants.get(i).getLatitude() + "");
//                        Log.e("RESTAURANTS", restaurants.get(i).getLongitude() + "");
//                        Log.e("RESTAURANTS", restaurants.get(i).getAssociatedFoodIds().length + "");
                    }
                    storageInterface.storeLastUpdatedDate(newEstDate);


                    storageInterface.storeRestaurants(restaurants);
                    callback.onSuccess("true");
                } catch (JSONException e) {
                    callback.onSuccess("false");
                }
            }
        })));
    }
    /**
     * NOCALLBACKGETRESTAURANTSASYNC
     * void method that gets restaurants async without a callback
     * generally don't use this, but it's here if you must // only use this if you need to load restaurants data manually
     */
    public void getRestaurantsAsync(){
        getRestaurantsAsync((response) -> {});
    }


    /**
     * GET ALL FOOD DATA FROM SERVER
     * Asynchronous method that calls the callback when the all food data is complete
     * @param callback the callback to be called when the all food data is complete
     * @callback the callback will be passed the String true if the data was successfully retrieved, false otherwise
     */
    public void getAllFoodDataAsync(VolleyRespInterface callback){
        queue.add(volleyQuick.vGet("/foods", (new VolleyRespInterface() {
                    @Override
                    public void onSuccess(String response) {
                        if (response.equals("error")) {
                            callback.onSuccess("error");
                            return;
                        }
                        try {
                            response = response.replaceAll("’", "'");
                            JSONObject responseJson = new JSONObject(response);
                            JSONArray responseArray = responseJson.getJSONArray("Foods");
                            lDate newEstDate = storageInterface.getLastUpdatedDate();
                            if (newEstDate == null) {
                                newEstDate = new lDate(0, 0, 0, 0, 0);
                            }
                            foods = new HashMap<Integer,Food>();
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject food = responseArray.getJSONObject(i);
                                Food newFood = new Food(food);
                                foods.put(newFood.getId(),newFood);
                                if (newEstDate.compareTo(newFood.getDate())) {
                                    newEstDate = foods.get(i).getDate();
                                }
//                                Log.e("FOODS", "DATE: " + foods.get(i).getDate().toString());
                            }
                            storageInterface.storeLastUpdatedDate(newEstDate);
                            storageInterface.storeFoods(foods);
                            Log.e("FOODS", "#:" + foods.size());
                            callback.onSuccess("true");

                        } catch (JSONException e) {
                            callback.onSuccess("false");
                        }
                    }
                })));
    }
    /**
     * NOCALLBACKGETALLFOODDATAASYNC
     * void method that gets all food data async without a callback
     * generally don't use this, but it's here if you must // only use this if you need to load food data manually
     */
    public void getAllFoodDataAsync(){
        getAllFoodDataAsync((response) -> {});
    }




    /**
     * GET RESTAURANTS OPEN NOW
     * Asynchronous method that calls the callback when the restaurants open now is complete
     * @implNote  the callback is returned as a string, so it must be parsed to an int array
     * @recommendation I recommend removing the surrounding brackets and splitting on the commas ie "[1,2,3]" to "1,2,3" then "1,2,3".split(","),
     *  I also recommend removing all whitespace from the string using .replaceAll("\\s+","")
     * @callback the callback will be passed an empty array if there are no restaurants open
     * @param callback the callback to be called when the restaurants open now is complete
     */
    public void getRestaurantsOpenNow(VolleyRespInterface callback){
        queue.add(volleyQuick.vGet("/restaurants/open", (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONArray responseJson = new JSONArray(response);
                    callback.onSuccess(responseJson.toString());
                } catch (JSONException e) {
                    callback.onSuccess("[]");
                }
            }
        })));
    }

    //GET RESTAURANTS OPEN AT TIME (tobeimplemented at a later date)
    // Asynchronous method that calls the callback when the restaurants open at time is complete


    //______________________________________________________________________________________________
    //______________________________________________________________________________________________

    //RESTAURANT PAGE METHODS

    /**
     * GET RESTAURANT
     * Semi Synchronous method that returns the restaurant,
     * Restaurant list is Asyncronously built when the class is initialized (see getRestaurantsAsync)
     * @param id the id of the restaurant to be returned
     * @return the restaurant with the given id, null if the restaurant does not exist or if the restaurant list has not been built yet
     */
    public Restaurant getRestaurant(int id){
        for(Restaurant restaurant : restaurants){
            if(restaurant.getId() == id){
                return restaurant;
            }
        }
        return null;
    }

    /**
     * GET FOODS
     * Semi Synchronous method that returns the foods,
     * Food Map is Asyncronously built when the class is initialized
     * @param restaurantId the id of the restaurant whos food list is to be returned
     * @return the list of foods associated with the given restaurant, null if the restaurant does not exist or if the food list has not been built yet
     */
    public List<Food> getFoods(int restaurantId){
        List<Food> returned = new ArrayList<>();
        for(int food : getRestaurant(restaurantId).getAssociatedFoodIds()){
            Food food1 = getFood(food);
            if(food1 != null){
                returned.add(food1);
            }
        }
        return returned;
    }

    /**
     * GET FOODS CURRENTLY AVAILABLE
     * Asynchronous method that calls the callback when the foods currently available is complete
     *
     * @param restaurantId the id of the restaurant whos current food list is to be returned
     * @param callback the callback to be called when the foods currently available is complete
     *
     * @implNote I recommend removing the surrounding brackets and splitting on the commas ie "[1,2,3]" to "1,2,3" then "1,2,3".split(","),
     * I also recommend removing all whitespace from the string using .replaceAll("\\s+","")
     *
     * @callback the callback is returned as a string, so it must be parsed to an int array,
     * the callback will be passed an empty array if there are no foods currently available
     */
    public void getFoodsCurrentlyAvailable(int restaurantId, VolleyRespInterface callback){
        //old code using json body
//        JSONObject body = new JSONObject();
//        try {
//            body.put("restaurantId", restaurantId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String urlExtension = "/cfoods?restaurantId=" + restaurantId;

        queue.add(volleyQuick.vGet(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray responseJsonArr = responseJson.getJSONArray("Foods");
                    callback.onSuccess(responseJsonArr.toString());
                } catch (JSONException e) {
                    callback.onSuccess("[]");
                }
            }
        }))); //, body
    }


    //GET FOODS AVAILABLE AT TIME (tobeimplemented at a later date)
    // Asynchronous method that calls the callback when the foods available at time is complete

    /**
     * GET HOURS LISTING
     * Asynchronous method that calls the callback when the hours listing is complete
     *
     * @param restaurantId the id of the restaurant whos hours listing is to be returned
     * @param callback the callback to be called when the hours listing is complete
     *
     * @callback the callback is returned as a string, either a formatted string of the hours listing or false if there is no hours listing or error if there is an error
     */
    public void getHoursListing(int restaurantId, VolleyRespInterface callback){
//        JSONObject body = new JSONObject();
//        try {
//            body.put("restaurantId", restaurantId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        queue.add(volleyQuick.vGet("/restaurants/today", (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray responseJsonArr = responseJson.getJSONArray("restarauntHours");
                    String returned = "false";
                    for(int i = 0; i < responseJsonArr.length(); i++){
                        JSONObject restaurant = responseJsonArr.getJSONObject(i);
                        int id = restaurant.getInt("id");
                        if(id == restaurantId){
                            returned = (new RestaurantHours(restaurant)).toString();
                        }
                        for(Restaurant rest : restaurants){
                            if(rest.getId() == id){
                                rest.setHours((new RestaurantHours(restaurant)).toString());
                                break;
                            }
                        }
                    }
                    callback.onSuccess(returned);
//                    callback.onSuccess(responseJson.toString());
                } catch (JSONException e) {
                    callback.onSuccess("error");
                }
            }
        }))); //, body
    }

    /**
     * GET HOURS LISTING
     * Asynchronous method that calls the callback when the hours listing is complete
     * @param callback the callback to be called when the hours listing is complete
     * @callback the callback is returned as a string,
     * the callback is returned true if the hours listing was successfully updated. false if interupted, and error if error
     */
    public void getHoursListing(VolleyRespInterface callback){
        queue.add(volleyQuick.vGet("/restaurants/hours", (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray responseJsonArr = responseJson.getJSONArray("restaurantHours");
                    String returned = "";
                    for(int i = 0; i < responseJsonArr.length(); i++){
                        JSONObject restaurant = responseJsonArr.getJSONObject(i);
                        int id = restaurant.getInt("id");
                        String hours = (new RestaurantHours(restaurant)).toString();
                        for(Restaurant rest : restaurants){
                            if(rest.getId() == id){
                                rest.setHours(hours);
                            }
                        }
                    }
                    callback.onSuccess("true");
//                    callback.onSuccess(responseJson.toString());
                } catch (JSONException e) {
                    callback.onSuccess("false");
                }
            }
        }))); //, body
    }

    //______________________________________________________________________________________________
    //______________________________________________________________________________________________

    //FOOD PAGE METHODS

    /**
     * GET FOOD
     * Semi Synchronous method that returns the food,
     * Food Map is Asyncronously built when the class is initialized, so this method will return null if the food map has not been built yet
     * @param id the id of the food to be returned
     * @return the food with the given id, or null if the food map has not been built yet
     */
    public Food getFood(int id){
        return foods.get(id);
    }

    /**
     * GET REVIEWS FOR FOOD
     * Asynchronous method that calls the callback when the reviews is complete
     * @callback the callback is returned as a string, I have included a method to parse it to a list of reviews see parseReviews(String reviewArrayAsString) below
     * @param foodId the id of the food whos reviews are to be returned
     * @param callback the callback to be called when the reviews are complete
     * @callback the callback is returned a string that is a json Array of reviews,
     * see parseReviews(String reviewArrayAsString) below,
     * on error the callback is returned an empty array
     */
    public void getReviewsByFood(int foodId, VolleyRespInterface callback){
        //old code using json body
//        JSONObject body = new JSONObject();
//        try {
//            body.put("food", foodId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String urlExtension = "/review/food?food=" + foodId  + "&index=0&size=0";
        queue.add(volleyQuick.vGet(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray responseJsonArr = responseJson.getJSONArray("reviews");
                    callback.onSuccess(responseJsonArr.toString());
                } catch (JSONException e) {
                    callback.onSuccess("[]");
                }
            }
        }))); //, body
    }

        /**
         * GET REVIEWS PARTIAL FOR FOOD
         * Asynchronous method that calls the callback when the reviews partial is complete
         * @implNote the index and size are used to get a partial list of reviews ie index 0, size 10 will get the first 10 reviews, index 10, size 10 will get reviews 11-20
         * @callback the callback is returned as a string, I have included a method to parse it to a list of reviews see parseReviews(String reviewArrayAsString) below
         * @param foodId the id of the food whos reviews are to be returned
         * @param index the index of the first review to be returned
         * @param size the number of reviews to be returned
         * @param callback the callback to be called when the reviews are complete
         */
        public void getReviewsByFoodPartial(int foodId, int index, int size, VolleyRespInterface callback){
        //old code using json body
//        JSONObject body = new JSONObject();
//        try {
//            body.put("food", foodId);
//            body.put("index", index);
//            body.put("size", size);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String urlExtension = "/review/food?food=" + foodId + "&index=" + index + "&size=" + size;
        queue.add(volleyQuick.vGet(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray responseJsonArr = responseJson.getJSONArray("reviews");
                    callback.onSuccess(responseJsonArr.toString());
                } catch (JSONException e) {
                    callback.onSuccess("[]");
                }
            }
        }))); //, body
    }

    /**
     * ADD REVIEW
     * Asynchronous method that calls the callback when the add review is complete
     * @param score the score of the review
     * @param reviewBody the body of the review
     * @param foodId the id of the food the review is for
     * @param callback the callback to be called when the add review is complete
     * @callback callback is returned a boolean indicating if the review was added as a string
     */
    public void addReview(double score, String reviewBody, int foodId, VolleyRespInterface callback){
        JSONObject body = new JSONObject();
        try {
            body.put("score", score);
            body.put("body", reviewBody);
            body.put("food", foodId);
            body.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queue.add(volleyQuick.vPost("/review", (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    boolean success = responseJson.getBoolean("success");
                    callback.onSuccess(success ? "true" : "false");
                } catch (JSONException e) {
                    callback.onSuccess("false");
                }
            }
        }), body));
    }
    /**
     * NOCALLBACK ADD REVIEW
     * Asynchronous method that doens't call the callback when the add review is complete
     * @param score the score of the review
     * @param reviewBody the body of the review
     * @param foodId the id of the food the review is for
     * @implNote this method is bad practice,
     * but I have included it for convenience and testing,
     * it is deprecated and should not be used,
     * review adding status should alway be checked
     */
    public void addReview(double score, String reviewBody, int foodId){
        addReview(score, reviewBody, foodId, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                //Do nothing
            }
        }));
    }

    /**
     * ADD REVIEW
     * Asynchronous method that calls the callback when the add review is complete
     * @param review the review to be added
     * @param callback the callback to be called when the add review is complete
     * @deprecated This method is deprecated, use the ones above instead,
     * the review shouldn't be created outside of the database like this
     * Reason: review objects are too verbose for the request,
     * much of the data is created on the server
     */
    @Deprecated
    public void addReview(Review review, VolleyRespInterface callback){
        addReview(review.getRating(), review.getReviewBody(), review.getAssociatedFoodId(), callback);
    }



    //______________________________________________________________________________________________
    //______________________________________________________________________________________________

    //USER PAGE METHODS

    /**
     * GET USER REVIEWS
     * Asynchronous method that calls the callback when the user reviews is complete
     * @callback the callback is returned as a string, I have included a method to parse it to a list of reviews see parseReviews(String reviewArrayAsString) below
     * @param userId the id of the user whos reviews are to be returned
     * @param callback the callback to be called when the user reviews are complete
     */
    public void getReviewsByUserId(int userId, VolleyRespInterface callback){
        //old code using json body
//        JSONObject body = new JSONObject();
//        try {
//            body.put("userId", userId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String urlExtension = "/review/user?userId=" + userId + "&index=0&size=0";
        queue.add(volleyQuick.vGet(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray responseJsonArr = responseJson.getJSONArray("reviews");
                    callback.onSuccess(responseJsonArr.toString());
                } catch (JSONException e) {
                    callback.onSuccess("[]");
                }
            }
        }))); //, body
    }

    /**
     *  USER REVIEWS PARTIAL
     * Asynchronous method that calls the callback when the user reviews partial is complete
     * @implNote the index and size are used to get a partial list of reviews ie index 0, size 10 will get the first 10 reviews, index 10, size 10 will get reviews 11-20
     * @callback the callback is returned as a string, I have included a method to parse it to a list of reviews see parseReviews(String reviewArrayAsString) below
     * @param userId the id of the user whos reviews are to be returned
     * @param index the index of the first review to be returned
     * @param size the number of reviews to be returned
     * @param callback the callback to be called when the user reviews partial is complete
     */
    public void getReviewsByUserIdPartial(int userId, int index, int size, VolleyRespInterface callback){
        //old code using json body
//        JSONObject body = new JSONObject();
//        try {
//            body.put("userId", userId);
//            body.put("index", index);
//            body.put("size", size);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String urlExtension = "/review/user?userId=" + userId + "&index=" + index + "&size=" + size;
        queue.add(volleyQuick.vGet(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    JSONArray responseJsonArr = responseJson.getJSONArray("reviews");
                    callback.onSuccess(responseJsonArr.toString());
                } catch (JSONException e) {
                    callback.onSuccess("[]");
                }
            }
        })));//, body
    }

    /**
     * Delete USER REVIEW
     * Asynchronous method that calls the callback when the delete user review is complete
     * @callback the callback is returned boolean indicating if the review was deleted as a string
     * @param reviewId the id of the review to be deleted
     * @param callback the callback to be called when the delete user review is complete
     */
    public void deleteReview(int reviewId, VolleyRespInterface callback){
        //old code using json body
//        JSONObject body = new JSONObject();
//        try {
//            body.put("reviewId", reviewId);
//            body.put("token", token);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String urlExtension = "/review?reviewId=" + reviewId;// + "&token=" + token;
        queue.add(volleyQuick.vDelete(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                if(response.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    JSONObject responseJson = new JSONObject(response);
                    Log.e("deleteReview", responseJson.toString());
                    boolean success = responseJson.getBoolean("success");
                    callback.onSuccess(success ? "true" : "false");
                } catch (JSONException e) {
                    callback.onSuccess("false");
                }
            }
        })));//, body
    }
    /**
     * NOCALLBACK DELETE USER REVIEW
     * Asynchronous method that doens't call the callback when the delete user review is complete,
     * this should not be used, it is only here for testing, always check if the review was deleted
     * @param reviewId the id of the review to be deleted
     */
    public void deleteReview(int reviewId){
        deleteReview(reviewId, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                //Do nothing
            }
        }));
    }

    //______________________________________________________________________________________________
    //______________________________________________________________________________________________

    /**
     * GET ALL DATA ASYNC
     * Asynchronous method that calls the callback when the get all data is complete
     * This method is used to get all the data at once, and is used to populate the database
     * @callback the callback is returned a string containing either "error" or true or false depending on if the data was retrieved and parsed correctly
     * @param callback the callback to be called when the get all data is complete
     */
    public void getAllDataAsync(VolleyRespInterface callback){
        //old code using json body
//        //needs json body with nothing in it
//        JSONObject body = new JSONObject();

        queue.add(volleyQuick.vGet("/data", (new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
                    result = result.replaceAll("’", "'");
                    restaurants = new ArrayList<Restaurant>();
                    foods = new HashMap<Integer, Food>();
                    JSONObject responseJson = new JSONObject(result);
                    JSONArray restaurantsJson = responseJson.getJSONArray("Restarants");
                    JSONArray foodsJson = responseJson.getJSONArray("Foods");
                    for(int i = 0; i < restaurantsJson.length(); i++){
                        restaurants.add(new Restaurant(restaurantsJson.getJSONObject(i)));
                    }
                    for(int i = 0; i < foodsJson.length(); i++) {
                        Food food = new Food(foodsJson.getJSONObject(i));
                        foods.put(food.getId(), food);
                    }
                    Log.e("FOOD MAP SIZE", foods.size() + "");
                    storageInterface.storeFoods(foods);
                    storageInterface.storeRestaurants(restaurants);
                    callback.onSuccess("true");
                } catch (JSONException e) {
                    callback.onSuccess("false");
                }
            }
        })));//, body
    }
    /**
     * NOCALLBACK GET ALL DATA ASYNC
     * Asynchronous method that doens't call the callback when the get all data is complete
     * could be used to manually populate the database
     */
    public void getAllDataAsync(){
        getAllDataAsync((new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                //Do nothing
            }
        }));
    }

    /**
     * PARSE REVIEWS
     * Synchronous method that returns a list of reviews from a string
     * @param reviewsArr the string containing the JSONArray of reviews
     * @return a list of reviews
     */
    public List<Review> parseReviews(String reviewsArr){
        List<Review> returned = new ArrayList<>();
        try {
            JSONArray reviewsJson = new JSONArray(reviewsArr);
            for(int i = 0; i < reviewsJson.length(); i++){
                returned.add(new Review(reviewsJson.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returned;
    }

    //______________________________________________________________________________________________
    //______________________________________________________________________________________________

    //UPDATE DATABASE

    /**
     * UPDATE ALL DATA
     * Asynchronous method that calls the callback when the update all data is complete
     * @param date is the ldate of the last update
     * @param callback the callback to be called when the update all data is complete
     * @callback the callback is returned as a string, it will be a json object with 2 arrays of restaurants and foods as json objects
     * on failure the callback will return an empty {} json object
     */
    public void updateAllData(lDate date, VolleyRespInterface callback){
        if(restaurants == null || foods == null|| date == null){ // if the database is empty, get all the data
            getAllDataAsync(callback);
            return;
        }
//        JSONObject body = date.toJSONMDY();
        String urlExtension = "/data/new?" + date.toUrlAttribute();
        Log.e("urlExtension", urlExtension);
        queue.add(volleyQuick.vGet(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {

                if(result.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try{
//                    restaurants = new ArrayList<Restaurant>();
//                    foods = new HashMap<Integer, Food>();
                    JSONObject responseJson = new JSONObject(result);
                    JSONArray restaurantsJson = responseJson.getJSONArray("Restarants");
                    JSONArray foodsJson = responseJson.getJSONArray("Foods");
                    lDate newestDate = storageInterface.getLastUpdatedDate();
                    if(newestDate == null){
                        newestDate = new lDate(0, 0, 0);
                    }
//                    Log.e("newestDate", newestDate.toString());


                    for(int i = 0; i < restaurantsJson.length(); i++){
                        //Look through Restaurant list to see if the restaurant is already in the list/
                        // if so update the variables, if not add it to the list
//                        Log.e("DATE UPDATED", "here:" +  new Restaurant(restaurantsJson.getJSONObject(i)).getLastUpdated().toStringWithoutTime());


                        boolean found = false;
                        if(restaurants != null){
                        for(Restaurant restaurant : restaurants){
                            if(restaurant.getId() == restaurantsJson.getJSONObject(i).getInt("id")){
                                restaurant = new Restaurant(restaurantsJson.getJSONObject(i));
                                if(newestDate.compareTo(restaurant.getLastUpdated())){
                                    newestDate = restaurant.getLastUpdated();
//                                    Log.e("newestDate", newestDate.toString());
                                }
                                found = true;
                                break;
                            }
                        }
                        }
                        else{
                            restaurants = new ArrayList<Restaurant>();
                        }

                        if(!found){
                            restaurants.add(new Restaurant(restaurantsJson.getJSONObject(i)));
                            if(newestDate.compareTo(restaurants.get(restaurants.size() - 1).getLastUpdated())){
                                newestDate = restaurants.get(restaurants.size() - 1).getLastUpdated();
//                                Log.e("newestDate", newestDate.toString());

                            }
                        }
                    }
                    for(int i = 0; i < foodsJson.length(); i++) {
                        Food food = new Food(foodsJson.getJSONObject(i));
//                        Log.e("FOOD PRE-PARSE", "" + foodsJson.getJSONObject(i).toString());
//                        Log.e("NEW FOOD:", food.getName() + " " + food.getAverageNumberOfStars());
                        foods.put(food.getId(), food);
                        if(newestDate.compareTo(food.getDate())){
                            newestDate = food.getDate();
//                            Log.e("newestDate", newestDate.toString());
                        }
                    }
                    storageInterface.storeLastUpdatedDate(newestDate);
                    storageInterface.storeFoods(foods);
                    storageInterface.storeRestaurants(restaurants);
                    callback.onSuccess(result);
                } catch (JSONException e) {
                    callback.onSuccess("{}");
                }
            }
        })));//, body
    }
    /**
     * NO CALLBACK UPDATE ALL DATA
     * Asynchronous method that doens't call the callback when the update all data is complete
     * @param date is the ldate of the last update
     */
    public void updateAllData(lDate date){
        updateAllData(date, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                //Do nothing
            }
        }));
    }

    /**
     * UPDATE ALL RESTAURANTS
     * Asynchronous method that calls the callback when the update all restaurants is complete
     * @param date is the ldate of the last update
     * @param callback the callback to be called when the update all restaurants is complete
     * @callback the callback is returned as a string, it will be a json array of restaurants as json objects
     * on failure the callback will return an empty [] json array
     * @implNote this method is the same as updateAllData, but it only returns the restaurants that were updated
     */
    @Deprecated
    public void updateAllRestaurants(lDate date, VolleyRespInterface callback){
        updateAllData(date, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                try{
                    JSONObject responseJson = new JSONObject(result);
                    JSONArray restaurantsJson = responseJson.getJSONArray("Restaurants");
                    callback.onSuccess(restaurantsJson.toString());
                } catch (JSONException e) {
                    callback.onSuccess("[]");
                }
            }
        }));
    }
    //NO CALLBACK UPDATE ALL RESTAURANTS is the same as updateAllData

    /**
     * UPDATE ALL FOODS
     * Asynchronous method that calls the callback when the update all foods is complete
     * @param date is the ldate of the last update
     * @param callback the callback to be called when the update all foods is complete
     * @callback the callback is returned as a string, it will be a json array of foods as json objects
     * on failure the callback will return an empty [] json array
     * @implNote this method is the same as updateAllData, but it only returns the foods that were updated
     */
    @Deprecated
    public void updateAllFoods(lDate date, VolleyRespInterface callback){
        updateAllData(date, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                try{
                    JSONObject responseJson = new JSONObject(result);
                    JSONArray foodsJson = responseJson.getJSONArray("Foods");
                    callback.onSuccess(foodsJson.toString());
                } catch (JSONException e) {
                    callback.onSuccess("[]");
                }
            }
        }));
    }
//    NO CALLBACK UPDATE ALL FOODS is the same as updateAllData

    //______________________________________________________________________________________________
    //______________________________________________________________________________________________


    //ADMIN METHODS

    /**
     * REVIEW DELETE
     * Asynchronous method that calls the callback when the review delete is complete
     * @param reviewId is the id of the review to delete
     * @param callback the callback to be called when the review delete is complete
     * @callback the callback is returned as a string, true if the review was deleted, false if it wasn't
     */
    public void reviewDelete(int reviewId, VolleyRespInterface callback){
        //old code using a body
//        JSONObject body = new JSONObject();
//        try {
//            body.put("reviewId", reviewId);
//            body.put("token", 0);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        Log.e("reviewDelete", "reviewId: " + reviewId);
        String urlExtension = "/review?reviewId=" + reviewId;

        queue.add(volleyQuick.vDelete(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(result);
                    callback.onSuccess(responseJson.getString("success"));
                } catch (JSONException e) {
                    callback.onSuccess("false");
                }
            }
        })));//, body
    }
    /**
     * NO CALLBACK REVIEW DELETE
     * Asynchronous method that doens't call the callback when the review delete is complete,
     * Genrally don't use this method, it is only here for testing, always check if review was deleted
     * @param reviewId is the id of the review to delete
     */
    @Deprecated
    public void reviewDelete(int reviewId){
        reviewDelete(reviewId, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                //Do nothing
            }
        }));
    }

    /**
     * USER BLACKLIST
     * Asynchronous method that calls the callback when the user blacklist is complete
     * @param userId is the id of the user to blacklist
     * @param callback the callback to be called when the user blacklist is complete
     * @callback the callback is returned as a string, true if the user was blacklisted, false if it wasn't
     */
    public void userBlacklist(int userId, VolleyRespInterface callback){
        JSONObject body = new JSONObject();
        try {
            body.put("userId", userId);
            body.put("token", getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        queue.add(volleyQuick.vPut("/ban", (new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(result);
                    callback.onSuccess(responseJson.getString("success"));
                } catch (JSONException e) {
                    callback.onSuccess("false");
                }
            }
        }), body));
    }
    /**
     * NO CALLBACK USER BLACKLIST
     * Asynchronous method that doens't call the callback when the user blacklist is complete,
     * Genrally don't use this method, it is only here for testing, always check if user was blacklisted
     * @param userId is the id of the user to blacklist
     */
    public void userBlacklist(int userId){
        userBlacklist(userId, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                //Do nothing
            }
        }));
    }

    /**
     * USER DELETE
     * Asynchronous method that calls the callback when the user delete is complete
     * @param userId is the id of the user to delete
     * @param callback the callback to be called when the user delete is complete
     * @callback the callback is returned as a string, true if the user was deleted, false if it wasn't
     */
    public void userDelete(int userId, VolleyRespInterface callback){
        //old code using a body
//        JSONObject body = new JSONObject();
//        try {
//            body.put("userId", userId);
//            body.put("token", 0);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String urlExtension = "/del/user?userId=" + userId + "&token=0";

        queue.add(volleyQuick.vDelete(urlExtension, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(result);
                    callback.onSuccess(responseJson.getString("success"));
                } catch (JSONException e) {
                    callback.onSuccess("false");
                }
            }
        })));//, body
    }
    /**
     * NO CALLBACK USER DELETE
     * Asynchronous method that doens't call the callback when the user delete is complete
     * Genrally don't use this method, it is only here for testing, always check if user was deleted
     * @param userId is the id of the user to delete
     */
    public void userDelete(int userId){
        userDelete(userId, (new VolleyRespInterface() {
            @Override
            public void onSuccess(String response) {
                //Do nothing
            }
        }));
    }

    //______________________________________________________________________________________________
    //______________________________________________________________________________________________

    // ALWAYS ASYNC API CALLS


    /**
     * GET RECOMMENDED FOODS
     * Asynchronous method that calls the callback when the recommended foods are complete
     * @param callback the callback to be called when the recommended foods are complete
     * @callback the callback is returned as a string, it will be a json array of food ids
     * on failure the callback will return an empty [] json array
     * @implNote
     * I recommend removing the surrounding brackets and splitting on the commas ie "[1,2,3]" to "1,2,3" then "1,2,3".split(","),
     * I also recommend removing all whitespace from the string using .replaceAll("\\s+","")
     */
    public void getRecommendedFoods(VolleyRespInterface callback){
        queue.add(volleyQuick.vGet("/recon", (new VolleyRespInterface() {
            @Override
            public void onSuccess(String result) {
                if(result.equals("error")){
                    callback.onSuccess("error");
                    return;
                }
                try {
                    JSONObject responseJson = new JSONObject(result);
                    JSONArray foodsJson = responseJson.getJSONArray("foods");
                    callback.onSuccess(foodsJson.toString());
                } catch (JSONException e) {
                    callback.onSuccess("[]");
                }
            }
        })));
    }



    //______________________________________________________________________________________________
    //______________________________________________________________________________________________


    /**
     * GET STORAGE INTERFACE
     * FOR DIRECT ACCESS TO THE CACHE OBJECT
     * USE WITH CAUTION
     * @return the storage interface object
     */
    public StorageInterface getStorageInterface(){
        //set the instance storage to null
        return storageInterface;
    }

    /**
     * CLEAR CACHE
     * Clears the cache and the storage interface and the restaurants and foods
     */
    public void clearCache(){
        restaurants = null;
        foods = null;

        storageInterface.clearCache();
    }

    /**
     * STATIC CLEAR CACHE also DELETES TOKEN
     * */
    public static void clearCache(boolean x){
        restaurants = null;
        foods = null;
        token = 0;

        if(storageInterface == null){
            return;
        }
        if(x){
            storageInterface.clearCache();
            storageInterface.storeToken(-1);
        }
        else{
            storageInterface.clearCache();
            storageInterface.storeToken(-1);
        }
    }


    /**
     * GET CACHE SIZE
     * @return the size of the cache as a string formatted to the nearest scalar size ie 1.2kb, 1.2mb, 1.2gb
     * */
    public String getCacheSize(){
        if(storageInterface == null){
            return "null";
        }
        return storageInterface.getCacheSize();
    }

    /**
     * GET CACHE DATE
     * @return the date the cache was last updated
     */
    public lDate getCacheDate(){
        if(storageInterface == null){
            return null;
        }
        return storageInterface.getLastUpdatedDate();
    }


    /**
     * CLOSE FOOD UPDATE SOCKET
     * */
    public void closeFoodUpdateSocket(){
       if(foodUpdateSocket != null && !foodUpdateSocket.isClosed()){
           foodUpdateSocket.close();
       }
    }

}
