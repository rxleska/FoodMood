package com.example.mainapplication.datainterface.notinuse;

import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.DataClasses.Review;

import java.util.List;

/**
 * @deprecated
 * This interface was used to fetch data from the database.
 *
 * It is not used in the final application.
 *
 * This interfaces purpose was replaced with the Middle Man.
 * @see com.example.mainapplication.datainterface.MiddleMan
 *
 * @author Ryan Leska
 * */
@Deprecated
public interface DataFetcher {
    //TODO: Add methods to fetch data from the database and from local storage

    // Restaurant methods
    List<Restaurant> getRestaurants();
    Restaurant getRestaurant(int id);

    // Food methods
    int[] getFoodList(int id); // id is the restaurant id //returns an array of food ids
    int[] getFoodList(String name); // name is the restaurant name //returns an array of food ids
    Food getFood(int id); // id is the food id
    Food getFood(String name); // name is the food name

    int[] getRecommendations(); //returns an array of food ids


    // Review methods
    Review[] getReviews(int id); // id is the food id
    Review[] getReviews(String name); // name is the food name
    Review getReview(int id); // id is the review id

    // User methods
    boolean userLoggedIn(int token); // token is the user's token
}
