package com.example.mainapplication.datainterface.caching;

import android.util.Log;

import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.DataClasses.lDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cache Access is a class that implements the StorageInterface and is used to access the cache.
 *
 * Please see the (@link com.example.mainapplication.datainterface.StorageInterface) for more information.
 * @author Ryan Leska
 * */
public class CacheAccess implements StorageInterface{

    /**
     * The token is used to authenticate the user. It is stored in a file called token.txt
     * */
    private String tokenFile = "/token.txt";
    /**
     * The restaurants are stored in a file called restaurants.json
     * */
    private String restaurantsFile = "/restaurants.json";
    /**
     * The foods are stored in a file called foods.json
     * */
    private String foodsFile = "/foods.json";
    /**
     * The lastUpdated date is stored in a file called lastUpdated.json
     * */
    private String lastUpdatedFile = "/lastUpdated.json";

    /**
     * The token is stored in a static variable so that it can be accessed from anywhere in the app.
     *
     * The token is stored a second time in the static class middleman
     * */
    public static int token;
    /**
     * The restaurants are stored in a static variable so that they can be accessed from anywhere in the app.
     *
     * The restaurants are stored a second time in the static class middleman
     * */
    public static List<Restaurant> restaurants;
    /**
     * The foods are stored in a static variable so that they can be accessed from anywhere in the app.
     *
     * The foods are stored a second time in the static class middleman
     * */
    public static Map<Integer, Food> foods;
    /**
     * The lastUpdated date is stored in a static variable so that it can be accessed from anywhere in the app.
     *
     * The lastUpdated date is stored a second time in the static class middleman
     * */
    public static lDate lastUpdated;

    /**
     * The file directory is used to store/access the files in the correct location.
     * */
    private String fileDir;

    /**
     * The constructor for the CacheAccess class.
     *
     * The constructor will attempt to load the token, restaurants, foods, and lastUpdated date from the cache.
     * If the cache is empty, the constructor will set the static variables to null.
     * */
    public CacheAccess() {
        token = getStoredToken();
        if(restaurants == null) {
            restaurants = getStoredRestaurants();
        }
        if(foods == null) {
            foods = getStoredFoods();
        }
        if(lastUpdated == null) {
            lastUpdated = getStoredLastUpdatedDate();
        }
    }

    /**
     * The constructor for the CacheAccess class.
     *
     * The constructor will attempt to load the token, restaurants, foods, and lastUpdated date from the cache.
     * If the cache is empty, the constructor will set the static variables to null.
     *
     * @param fileDir The file directory is used to store/access the files in the correct location.
     * */
    public CacheAccess(String fileDir) {
        this.fileDir = fileDir;
        token = getStoredToken();
        if(restaurants == null) {
            restaurants = getStoredRestaurants();
        }
        if(foods == null) {
            foods = getStoredFoods();
        }
        if(lastUpdated == null) {
            lastUpdated = getStoredLastUpdatedDate();
        }
    }

    /**
     * The getToken method is used to get the token.
     *
     * @return The token.
     * */
    @Override
    public int getToken() {
        return token;
    }

    /**
     * The getStoredToken method is used to get the token from the cache.
     *
     * @return The token.
     * */
    @Override
    public int getStoredToken() {
        try{
            FileInputStream fis = new FileInputStream(this.fileDir + tokenFile);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
//            Log.e("CacheAccess", "Token: " + new String(data));
            return Integer.parseInt(new String(data));
        } catch (FileNotFoundException e) {
            Log.e("CacheAccess", "File Not Found Exception: " + e.getMessage());
            return 0;
        } catch (IOException e) {
            Log.e("CacheAccess", "IOE: " + e.getMessage());
            return 0;
        }
    }

    /**
     * The storeToken method is used to store the token in the cache.
     *
     * @param token The token to be stored.
     * */
    @Override
    public void storeToken(int token) {
        this.token = token;
        try {
            FileOutputStream fos = new FileOutputStream(this.fileDir + tokenFile, false); // false = overwrite, I don't know the default
            Log.e("CacheAccess", "TokenStored?: " + this.token);
            fos.write(String.valueOf(token).getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The getFoods method is used to get the foods.
     *
     * @return The foods.
     * */
    @Override
    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    /**
     * The getStoredRestaurants method is used to get the restaurants from the cache.
     *
     * @return The restaurants.
     * */
    @Override
    public List<Restaurant> getStoredRestaurants() {

        try {
            FileInputStream fis = new FileInputStream(this.fileDir + restaurantsFile);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            JSONObject json = new JSONObject(new String(data));
            JSONArray jsonRestaurants = json.getJSONArray("restaurants");
            List<Restaurant> retRestaurants = new ArrayList<>();
            for(int i = 0; i < jsonRestaurants.length(); i++) {
                JSONObject jsonRestaurant = jsonRestaurants.getJSONObject(i);
                retRestaurants.add(new Restaurant(jsonRestaurant));
            }
            return retRestaurants;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        } catch (JSONException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * The storeRestaurants method is used to store the restaurants in the cache.
     *
     * @param restaurants The restaurants to be stored.
     * */
    @Override
    public void storeRestaurants(List<Restaurant> restaurants) {
        FileOutputStream fos = null; // false = overwrite, I don't know the default
        try {
            fos = new FileOutputStream(this.fileDir + restaurantsFile, false);
            JSONObject json = new JSONObject();
            JSONArray jsonRestaurants = new JSONArray();
            for(Restaurant restaurant : restaurants) {
                jsonRestaurants.put(restaurant.toJsonObject());
            }
            json.put("restaurants", jsonRestaurants);
            fos.write(json.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        } catch (JSONException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }

    }

    /**
     * The addRestaurant method is used to add a restaurant to the restaurants list.
     *
     * @param restaurant The restaurant to be added.
     *
     * */
    @Override
    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
        storeRestaurants(restaurants);
    }


    /**
     * The removeRestaurant method is used to remove a restaurant from the restaurants list.
     *
     * @param id The id of the restaurant to be removed.
     *
     * @implNote USES ID NOT INDEX
     * */
    @Override
    // NOTE USES ID NOT INDEX
    public void removeRestaurant(int id) {
        for(int i = 0; i < restaurants.size(); i++) {
            if(restaurants.get(i).getId() == id) {
                restaurants.remove(i);
                break;
            }
        }
        storeRestaurants(restaurants);
    }

    /**
     * The getFoods method is used to get the foods.
     *
     * @return The foods as a Map of id to Food.
     * */
    @Override
    public Map<Integer, Food> getFoods() {
        return foods;
    }

    /**
     * The getStoredFoods method is used to get the foods from the cache.
     *
     * @return The foods as a Map of id to Food.
     * */
    @Override
    public Map<Integer, Food> getStoredFoods() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(this.fileDir + foodsFile);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            JSONObject json = new JSONObject(new String(data));
            JSONArray jsonFoods = json.getJSONArray("foods");
            Map<Integer, Food> retFoods = new HashMap<>();
            for(int i = 0; i < jsonFoods.length(); i++) {
                try{
                    JSONObject jsonFood = jsonFoods.getJSONObject(i);
                    retFoods.put(jsonFood.getInt("id"), new Food(jsonFood));
                } catch (JSONException e) {
                    Log.e("CacheAccess", "Error parsing food: " + e.getMessage());
                }
            }
            return retFoods;
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        } catch (JSONException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * The storeFoods method is used to store the foods in the cache.
     *
     * @param foods The foods to be stored as Map of id to Food.
     * */
    @Override
    public void storeFoods(Map<Integer, Food> foods) {
        try {
            FileOutputStream fos = new FileOutputStream(this.fileDir + foodsFile, false);// false = overwrite, I don't know the default
            JSONObject json = new JSONObject();
            JSONArray jsonFoods = new JSONArray();
            for(Food food : foods.values()) {
                jsonFoods.put(food.toJsonObject());
            }
            json.put("foods", jsonFoods);
            fos.write(json.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }

    }

    /**
     * The addFood method is used to add a food to the foods map.
     *
     * @param food The food to be added.
     *
     * */
    @Override
    public void addFood(Food food) {
        foods.put(food.getId(), food);
        storeFoods(foods);
    }

    /**
     * The removeFood method is used to remove a food from the foods map.
     *
     * @param foodId The id of the food to be removed.
     *
     * @implNote USES ID NOT INDEX, Maps don't have direct indexes
     * */
    @Override
    public void removeFood(int foodId) {
        foods.remove(foodId);
        storeFoods(foods);
    }

    /**
     * The getLastUpdatedDate method is used to get the last updated date.
     *
     * @return The last updated date.
     * */
    @Override
    public lDate getLastUpdatedDate() {
        return lastUpdated;
    }

    /**
     * The getStoredLastUpdatedDate method is used to get the last updated date from the cache.
     *
     * @return The last updated date.
     * */
    @Override
    public lDate getStoredLastUpdatedDate() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(this.fileDir + lastUpdatedFile);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            JSONObject json = new JSONObject(new String(data));
            return new lDate(json);
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        } catch (JSONException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * The storeLastUpdatedDate method is used to store the last updated date in the cache.
     *
     * @param lastUpdated The last updated date to be stored.
     * */
    @Override
    public void storeLastUpdatedDate(lDate lastUpdated) {
        CacheAccess.lastUpdated = lastUpdated;
        try {
            FileOutputStream fos = new FileOutputStream(this.fileDir + lastUpdatedFile, false);// false = overwrite, I don't know the default
            JSONObject json = lastUpdated.toJsonObject();
            fos.write(json.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }

    /**
     * The clearCache method is used to clear the cache.
     *
     * @implNote This method does not clear the cached token.
     * */
    @Override
    public void clearCache(){
        try{
            FileOutputStream fos = new FileOutputStream(this.fileDir + restaurantsFile, false);
            fos.write("{}".getBytes());
            fos.close();
            fos = new FileOutputStream(this.fileDir + foodsFile, false);
            fos.write("{}".getBytes());
            fos.close();
            fos = new FileOutputStream(this.fileDir + lastUpdatedFile, false);
            fos.write("{}".getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * The getCacheSize method is used to get the size of the cache.
     *
     * @return The size of the cache. (formatted as string with units)
     * */
    @Override
    public String getCacheSize(){
        File restaurants = new File(this.fileDir + restaurantsFile);
        File foods = new File(this.fileDir + foodsFile);
        File lastUpdated = new File(this.fileDir + lastUpdatedFile);
        //add all the file sizes together
        long size = restaurants.length() + foods.length() + lastUpdated.length();
        if(size < 32){
            return "Empty";
        }

        if(size < 1024){
            return size + " bytes";
        }
        else if(size < 1024 * 1024){
            return size / 1024 + " KB";
        }
        else if(size < 1024 * 1024 * 1024){
            return size / (1024 * 1024) + " MB";
        }
        else{
            return size / (1024 * 1024 * 1024) + " GB";
        }
    }
}
