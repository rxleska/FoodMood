package com.example.mainapplication.datainterface.caching;

import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.DataClasses.lDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used for testing purposes only. It is a mock cache that returns a list of restaurants
 * and a map of foods. This is used to test the UI without having to connect to the server or without accessing the cache.
 *
 * @author Ryan Leska
 */
public class MockCache implements StorageInterface{

    /**
     * restaurants is a list of restaurants that is returned when getRestaurants() is called.
     */
    List<Restaurant> restaurants;

    /**
     * The getToken method returns a token that is used to authenticate the user.
     *
     * The mock token is 0.
     *
     * @return 0
     */
    @Override
    public int getToken() {
        return 0;
    }

    /**
     * The getStoredToken method returns a token that is used to authenticate the user.
     *
     * The mock token is 0.
     *
     * @return 0
     */
    @Override
    public int getStoredToken() {
        return 0;
    }

    /**
     * The storeToken method stores a token that is used to authenticate the user.
     *
     * The mock token is 0.
     *
     * @param token the token to be stored
     *
     * @implNote This method does nothing. The mock token is 0.
     */
    @Override
    public void storeToken(int token) {

    }

    /**
     * The getRestaurants method returns a list of restaurants.
     *
     * The mock restaurants are McDonalds, Burger King, and Wendys.
     *
     * @return a list of restaurants
     */
    @Override
    public List<Restaurant> getRestaurants() {
        restaurants = new ArrayList<Restaurant>();
        restaurants.add(new Restaurant(0, "McDonalds","12345 ronald rd","fast food place with burgor and fries and other stuff",
                "Fast food", new String[]{"mc cafe", "1 2 3 dollar"}, new int[]{0, 1, 2}, 45.0005, 45.0005, "8am-10pm"));
        restaurants.add(new Restaurant(1, "Burger King","54321 king rd","fast food place with burgor and fries and other stuff","Fast food", new String[]{"Mac Studios", "Dessert section"}, new int[]{3, 4, 5}, 45.0005, 45.1005, "10am-11pm"));
        restaurants.add(new Restaurant(2, "Wendys","12345 wendy rd","fast food place with burgor and fries and other stuff",
                "Fast food", new String[]{"Salty place", "Frosty place"}, new int[]{6, 7, 8}, 45.0005, 45.2005, "9am-12pm"));
        return restaurants;
    }

    /**
     * The getStoredRestaurants method returns a list of restaurants.
     *
     * This method returns null. For mock cache, use getRestaurants() instead.
     *
     * @return null
     */
    @Override
    public List<Restaurant> getStoredRestaurants() {
        return null;
    }

    /**
     * The storeRestaurants method stores a list of restaurants.
     *
     * This method does nothing. For mock cache, use getRestaurants() instead.
     *
     * @param restaurants the list of restaurants to be stored
     *
     * @implNote This method does nothing. For mock cache, use getRestaurants() instead.
     */
    @Override
    public void storeRestaurants(List<Restaurant> restaurants) {

    }

    /**
     * The addRestaurant method adds a restaurant to the list of restaurants.
     *
     * This method does nothing. For mock cache, use getRestaurants() instead.
     *
     * @param restaurant the restaurant to be added
     *
     * @implNote This method does nothing. For mock cache.
     */
    @Override
    public void addRestaurant(Restaurant restaurant) {

    }

    /**
     * The removeRestaurant method removes a restaurant from the list of restaurants.
     *
     * This method does nothing. For mock cache, use getRestaurants() instead.
     *
     * @param id the id of the restaurant to be removed
     *
     * @implNote This method does nothing. For mock cache.
     */
    @Override
    public void removeRestaurant(int id) {

    }

    /**
     * The getFoods method returns a map of foods.
     *
     * The mock foods are Big Burgor, Thin Fries, Sprite, Mac Burgor, Cardboard Fries, Diet Coke, Baconagor, and
     * Cheese Fries.
     *
     * @return a map of foods
     */
    @Override
    public Map<Integer, Food> getFoods() {
        Map<Integer, Food> foodMap = new HashMap<Integer, Food>();

        foodMap.put(0,new Food(0, 4.5, "Big Burgor", "Burgor", new lDate(2021, 1, 5, 0, 0)));
        foodMap.put(1,new Food(1, 4.5, "Thin Fries", "Fries", new lDate(2020, 4, 8, 0, 0)));
        foodMap.put(2,new Food(2, 4.5, "Sprite", "Drink", new lDate(2020, 12, 18, 0, 0)));
        foodMap.put(3,new Food(3, 4.5, "Mac Burgor", "Burgor", new lDate(2022, 11, 1, 12, 0)));
        foodMap.put(4,new Food(4, 4.5, "Cardboard Fries", "Fries", new lDate(2020, 1, 1, 0, 0)));
        foodMap.put(5,new Food(5, 4.5, "Diet Coke", "Drink", new lDate(2023, 3, 23, 0, 0)));
        foodMap.put(6,new Food(6, 4.5, "Baconagor", "Burgor", new lDate(2020, 1, 15, 0, 0)));
        foodMap.put(7,new Food(7, 4.5, "Salty Fries", "Fries", new lDate(2022, 7, 22, 12, 0)));
        foodMap.put(8,new Food(8, 4.5, "Dr Pepper", "Drink", new lDate(2021, 4, 10, 0, 0)));

        return foodMap;
    }

    /**
     * The getStoredFoods method returns a map of foods.
     *
     * This method returns null. For mock cache, use getFoods() instead.
     *
     * @return null
     *
     * @implNote This method returns null. For mock cache, use getFoods() instead.
     */
    @Override
    public Map<Integer, Food> getStoredFoods() {
        return null;
    }

    /**
     * The storeFoods method stores a map of foods.
     *
     * This method does nothing. For mock cache, use getFoods() instead.
     *
     * @param foods the map of foods to be stored
     *
     * @implNote This method does nothing. For mock cache
     */
    @Override
    public void storeFoods(Map<Integer, Food> foods) {

    }

    /**
     * The addFood method adds a food to the map of foods.
     *
     * This method does nothing. For mock cache, use getFoods() instead.
     *
     * @param food the food to be added
     *
     * @implNote This method does nothing. For mock cache.
     */
    @Override
    public void addFood(Food food) {

    }

    /**
     * The removeFood method removes a food from the map of foods.
     *
     * This method does nothing. For mock cache, use getFoods() instead.
     *
     * @param foodId the id of the food to be removed
     *
     * @implNote This method does nothing. For mock cache.
     */
    @Override
    public void removeFood(int foodId) {

    }

    /**
     * The getLastUpdatedDate method returns the last updated date.
     *
     * This method returns null. For mock cache, do not use this method.
     *
     * @return null
     */
    @Override
    public lDate getLastUpdatedDate() {
        return null;
    }

    /**
     * The getStoredLastUpdatedDate method returns the last updated date.
     *
     * This method returns null. For mock cache, do not use this method.
     *
     * @return null
     */
    @Override
    public lDate getStoredLastUpdatedDate() {
        return null;
    }

    /**
     * The storeLastUpdatedDate method stores the last updated date.
     *
     * This method does nothing. For mock cache, do not use this method.
     *
     * @param lastUpdated the last updated date to be stored
     *
     * @implNote This method does nothing. For mock cache.
     */
    @Override
    public void storeLastUpdatedDate(lDate lastUpdated) {

    }

    /**
     * The clearCache method clears the cache.
     *
     * This method does nothing. For mock cache, do not use this method.
     *
     * @implNote This method does nothing. For mock cache.
     */
    @Override
    public void clearCache() {

    }

    /**
     * The getCacheSize method returns the size of the cache.
     *
     * This method returns null. For mock cache, do not use this method.
     *
     * @return null
     */
    @Override
    public String getCacheSize() {
        return null;
    }
}
