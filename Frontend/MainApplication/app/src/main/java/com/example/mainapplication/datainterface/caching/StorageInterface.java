package com.example.mainapplication.datainterface.caching;

import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.DataClasses.lDate;

import java.util.List;
import java.util.Map;

/**
 * The interface Storage interface is used to store data in the cache.
 * The cache is used to store data that is used in the app.
 *
 * @author Ryan Leska
 */
public interface StorageInterface {

    /**
     * The method getToken returns the token that is stored in the storage interface.
     * @return int token
     */
    int getToken(); //calls getStoredToken if token is not set
    /**
     * The method getStoredToken returns the token that is stored in the cache.
     * @return int token
     */
    int getStoredToken(); // <= 0 if token is not set
    /**
     * The method storeToken stores the token in the cache.
     * @param token the token of the user to be stored
     */
    void storeToken(int token);

    /**
     * The method getRestaurants returns the restaurants that are stored in the storage interface.
     * @return List of type Restaurant restaurants
     */
    List<Restaurant> getRestaurants(); // calls getStoredRestaurants if restaurants is not set
    /**
     * The method getStoredRestaurants returns the restaurants that are stored in the cache.
     * @return List of type Restaurant restaurants
     */
    List<Restaurant> getStoredRestaurants();
    /**
     * The method storeRestaurants stores the restaurants in the cache.
     * @param restaurants list of restaurants to be stored
     */
    void storeRestaurants(List<Restaurant> restaurants);
    /**
     * The method addRestaurant adds a restaurant to the cache and the storage interface local copy.
     * @param restaurant restaurant to be added to the restaurant list
     */
    void addRestaurant(Restaurant restaurant);
    /**
     * The method removeRestaurant removes a restaurant from the cache and the storage interface local copy.
     * @param id id of the restaurant to remove from the restaurant list
     */
    void removeRestaurant(int id);

    /**
     * The method getFoods returns the foods that are stored in the storage interface.
     * @return Map of Integer to type Food foods
     */
    Map<Integer, Food> getFoods(); // calls getStoredFoods if foods is not set
    /**
     * The method getStoredFoods returns the foods that are stored in the cache.
     * @return Map of Integer to type Food foods
     */
    Map<Integer, Food> getStoredFoods();
    /**
     * The method storeFoods stores the foods in the cache and in the storage interface local copy.
     * @param foods (Map from Integer to type Food)
     */
    void storeFoods(Map<Integer, Food> foods);
    /**
     * The method addFood adds a food to the cache and the storage interface local copy.
     * @param food food to be added to the food map
     */
    void addFood(Food food);
    /**
     * The method removeFood removes a food from the cache and the storage interface local copy.
     * @param foodId id of the food to remove from the food map
     */
    void removeFood(int foodId);

    /**
     * The method getLastUpdatedDate returns the lastUpdated that is stored in the storage interface.
     * @return lDate lastUpdated
     */
    lDate getLastUpdatedDate(); // calls getStoredLastUpdatedDate if lastUpdated is not set
    /**
     * The method getStoredLastUpdatedDate returns the lastUpdated that is stored in the cache.
     * @return lDate lastUpdated
     */
    lDate getStoredLastUpdatedDate();
    /**
     * The method storeLastUpdatedDate stores the lastUpdated in the cache.
     * @param lastUpdated date the data was last updated at
     */
    void storeLastUpdatedDate(lDate lastUpdated);

    /**
     * The method clearCache clears the cache.
     */
    void clearCache();

    /**
     * The method getCacheSize returns the size of the cache.
     * @return String cacheSize
     */
    String getCacheSize();

}
