package com.example.mainapplication.datainterface.notinuse;

import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.DataClasses.lDate;
import com.example.mainapplication.datainterface.caching.StorageInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @deprecated
 * This class was used to access the storage of the app.
 *
 * It is not used in the final application.
 *
 * The Storage Access Class had to be replaced because it implemented the DataFetcher and DataSender interfaces.
 * These interfaces were replaced with the middle man.
 * @see com.example.mainapplication.datainterface.MiddleMan
 *
 * This class was replaced with the Cache Access class.
 * @see com.example.mainapplication.datainterface.caching.CacheAccess
 *
 * */
@Deprecated
public class StorageAccess implements StorageInterface {
    // This class is used to access the storage of the app
    // It is used to store and retrieve data from the app's storage
    // It is used to store and retrieve data from the server


    //STORAGE ACCESS
    private String tokenFileName = "token.txt";
    private String restaurantsFileName = "restaurants.json";
    private String foodsFileName = "foods.json";
    private String lastUpdatedFileName = "lastUpdated.json";
    //End of STORAGE ACCESS


    // Fields
    private int token;
    private List<Restaurant> restaurants;
    private Map<Integer, Food> foods;
    private lDate lastUpdated;

    private DataFetcher dataFetcher;
    private DataSender dataSender;

    // Constructor
    public StorageAccess(DataFetcher dataFetcher, DataSender dataSender){
        // Initialize the fields

        // assign the dataFetcher and dataSender
        this.dataFetcher = dataFetcher;
        this.dataSender = dataSender;

        // get the token from the storage
        // check if the token is valid
        // if the token is valid set the token field
        // if the token is not valid set the token field to -1
        // if the token is not in the storage set the token field to -2
        this.token = getStoredToken();
        if(this.token > 0){
            // check if the token is valid
            if(!this.dataFetcher.userLoggedIn(this.token)){
                // the token is not valid
                this.token = -1;
            }
        }
        else{
            // the token is not in the storage
            this.token = -2;
        }

        // get the restaurants from the storage
        // if the restaurants are not in the storage set the restaurants fetch the restaurants from the server
        this.restaurants = getStoredRestaurants();
        if(this.restaurants == null || this.restaurants.size() == 0){
            // the restaurants are not in the storage
            // fetch the restaurants from the server
            this.restaurants = this.dataFetcher.getRestaurants();
            storeRestaurants(this.restaurants);
        }

        // get the foods from the storage
        // if the foods are not in the storage set the foods fetch the foods from the server
        this.foods = getStoredFoods();
        if(this.foods == null || this.foods.size() == 0){
            // the foods are not in the storage
            // fetch the foods from the server
            this.foods = this.getFoods();
            storeFoods(this.foods);
        }

        // get the lastUpdated from the storage
        // if the lastUpdated is not in the storage set it to 1969-12-31
        this.lastUpdated = getStoredLastUpdatedDate();
        if(this.lastUpdated == null){
            // the lastUpdated is not in the storage
            // set it to 1969-12-31
            this.lastUpdated = new lDate(1969, 12, 31);
        }
    }

    //TOKEN METHODS
    public int getToken(){
        // returns the token
        return this.token;
    }
    public int getStoredToken(){
        // Open the file tokenFileName
        // Read the token from the file
        // Close the file
        // return the token
        try {
            FileInputStream fileInputStream = new FileInputStream(this.tokenFileName);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            int token = Integer.parseInt(new String(bytes));
            return token;
        } catch (FileNotFoundException e) {
            return -1;
        } catch (IOException e) {
            return -1;
        }

    }
    public void storeToken(int token){
        // Open the file tokenFileName
        // Write the token to the file
        // Close the file
        // set the token field to the token
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(this.tokenFileName);
            fileOutputStream.write(token);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //RESTAURANTS METHODS
    public List<Restaurant> getRestaurants(){
        // returns the restaurants
        return this.restaurants;
    }
    public List<Restaurant> getStoredRestaurants(){
        // Open the file restaurantsFileName
        // Read the restaurants from the file
        // Close the file
        // return the restaurants
        try {
            FileInputStream fileInputStream = new FileInputStream(this.restaurantsFileName);
            List<Restaurant> newrestaurants = new ArrayList<>();
            byte[] bytes = new byte[fileInputStream.available()];
            String line = new String(bytes);
            JSONObject jsonObject = new JSONObject(line);
            JSONArray jsonArray = jsonObject.getJSONArray("restaurants");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject restaurantObject = jsonArray.getJSONObject(i);
                Restaurant restaurant = new Restaurant(restaurantObject);
                newrestaurants.add(restaurant);
            }
            fileInputStream.close();
            return newrestaurants;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            return null;
        }
    }
    public void storeRestaurants(List<Restaurant> restaurants){
        // Open the file restaurantsFileName
        // Write the restaurants to the file
        // Close the file
        // set the restaurants field to the restaurants
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(this.restaurantsFileName);
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for(Restaurant restaurant : restaurants){
                JSONObject restaurantObject = restaurant.toJsonObject();
                jsonArray.put(restaurantObject);
            }
            jsonObject.put("restaurants", jsonArray);
            fileOutputStream.write(jsonObject.toString().getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void addRestaurant(Restaurant restaurant){
        // add the restaurant to the restaurants
        // store the restaurants
        this.restaurants.remove(restaurant.getId());
        this.restaurants.add(restaurant.getId(), restaurant);
    }
    public void removeRestaurant(int id){
        // remove the restaurant at id:id from the restaurants
        // store the restaurants
        this.restaurants.remove(id);
    }


    //FOODS METHODS
    public Map<Integer, Food> getFoods(){
        // returns the foods
        return this.foods;
    }
    public Map<Integer, Food> getStoredFoods(){
        // Open the file foodsFileName
        // Read the foods from the file
        // Close the file
        // return the foods
        try{
            FileInputStream fileInputStream = new FileInputStream(this.foodsFileName);
            Map<Integer, Food> newfoods = new HashMap<>();
            byte[] bytes = new byte[fileInputStream.available()];
            String line = new String(bytes);
            JSONObject jsonObject = new JSONObject(line);
            JSONArray jsonArray = jsonObject.getJSONArray("foods");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject foodObject = jsonArray.getJSONObject(i);
                Food food = new Food(foodObject);
                newfoods.put(food.getId(), food);
            }
            fileInputStream.close();
            return newfoods;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            return null;
        }
    }
    public void storeFoods(Map<Integer, Food> foods){
        // Open the file foodsFileName
        // Write the foods to the file
        // Close the file
        // set the foods field to the foods
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(this.foodsFileName);
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for(Food food : foods.values()){
                JSONObject foodObject = food.toJsonObject();
                jsonArray.put(foodObject);
            }
            jsonObject.put("foods", jsonArray);
            fileOutputStream.write(jsonObject.toString().getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void addFood(Food food){
        // add the food to the foods
        // store the foods
        this.foods.put(food.getId(), food);
    }
    public void removeFood(int id){
        // remove the food at id:id from the foods
        // store the foods
        this.foods.remove(id);
    }

    // DATE METHODS
    public lDate getLastUpdatedDate(){
        // returns the lastUpdated
        return this.lastUpdated;
    }
    public lDate getStoredLastUpdatedDate(){
        // Open the file lastUpdatedFileName
        // Read the lastUpdated from the file
        // Close the file
        // return the lastUpdated
        try {
            FileInputStream fileInputStream = new FileInputStream(this.lastUpdatedFileName);
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            String line = new String(bytes);
            JSONObject jsonObject = new JSONObject(line);
            lDate lastUpdated = new lDate(jsonObject);
            return lastUpdated;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (JSONException e) {
            return null;
        }
    }
    public void storeLastUpdatedDate(lDate lastUpdated){
        // Open the file lastUpdatedFileName
        // Write the lastUpdated to the file
        // Close the file
        // set the lastUpdated field to the lastUpdated
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(this.lastUpdatedFileName);
            JSONObject jsonObject = lastUpdated.toJsonObject();
            fileOutputStream.write(jsonObject.toString().getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearCache() {

    }

    @Override
    public String getCacheSize() {
        return null;
    }

}
