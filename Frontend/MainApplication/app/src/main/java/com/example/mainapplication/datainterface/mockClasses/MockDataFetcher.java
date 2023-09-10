package com.example.mainapplication.datainterface.mockClasses;

import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.DataClasses.Review;
import com.example.mainapplication.datainterface.DataClasses.lDate;
import com.example.mainapplication.datainterface.notinuse.DataFetcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class MockDataFetcher is a mock class that is used to test the app.
 *
 * It is not used in the final application.
 *
 * This classes purpose was replaced with the Middle Man.
 * @see com.example.mainapplication.datainterface.MiddleMan
 *
 * @author Ryan Leska
 * */
@Deprecated
public class MockDataFetcher implements DataFetcher {
    List<Restaurant> restaurants; // Stores the restaurants, uses array because the number of restaurants is small and generally fixed
    Map<Integer, Food> foodMap; // Stores the food items, uses map because the number of food items is large and can change //ie quicker than a single arraylist
    Map<Integer, Review[]> reviewMap; // Stores the reviews, uses map because the number of reviews is large and can change //ie quicker than a single arraylist

    MockDataFetcher(){
        restaurants = new ArrayList<Restaurant>();
        restaurants.add(new Restaurant(0, "McDonalds","12345 ronald rd","fast food place with burgor and fries and other stuff",
                "Fast food", new String[]{"mc cafe", "1 2 3 dollar"}, new int[]{0, 1, 2}, 45.0005, 45.0005, "8am-10pm"));
        restaurants.add(new Restaurant(1, "Burger King","54321 king rd","fast food place with burgor and fries and other stuff","Fast food", new String[]{"Mac Studios", "Dessert section"}, new int[]{3, 4, 5}, 45.0005, 45.1005, "10am-11pm"));
        restaurants.add(new Restaurant(2, "Wendys","12345 wendy rd","fast food place with burgor and fries and other stuff",
                "Fast food", new String[]{"Salty place", "Frosty place"}, new int[]{6, 7, 8}, 45.0005, 45.2005, "9am-12pm"));

        foodMap = new HashMap<Integer, Food>();

        foodMap.put(0,new Food(0, 4.5, "Big Burgor", "Burgor", new lDate(2021, 1, 5, 0, 0)));
        foodMap.put(1,new Food(1, 4.5, "Thin Fries", "Fries", new lDate(2020, 4, 8, 0, 0)));
        foodMap.put(2,new Food(2, 4.5, "Sprite", "Drink", new lDate(2020, 12, 18, 0, 0)));
        foodMap.put(3,new Food(3, 4.5, "Mac Burgor", "Burgor", new lDate(2022, 11, 1, 12, 0)));
        foodMap.put(4,new Food(4, 4.5, "Cardboard Fries", "Fries", new lDate(2020, 1, 1, 0, 0)));
        foodMap.put(5,new Food(5, 4.5, "Diet Coke", "Drink", new lDate(2023, 3, 23, 0, 0)));
        foodMap.put(6,new Food(6, 4.5, "Baconagor", "Burgor", new lDate(2020, 1, 15, 0, 0)));
        foodMap.put(7,new Food(7, 4.5, "Salty Fries", "Fries", new lDate(2022, 7, 22, 12, 0)));
        foodMap.put(8,new Food(8, 4.5, "Dr Pepper", "Drink", new lDate(2021, 4, 10, 0, 0)));


        reviewMap = new HashMap<Integer, Review[]>();

        reviewMap.put(0, new Review[]{new Review(0, 0, 4.5, "This burgor was great", new lDate(2021, 1, 5, 0, 0),0),
                new Review(1, 0, 4.5, "This burgor was great1", new lDate(2021, 1, 5, 0, 0),0),
                new Review(2, 0, 4.5, "This burgor was great2", new lDate(2021, 1, 5, 0, 0),0)});
        reviewMap.put(1, new Review[]{new Review(3, 1, 4.5, "This fries was great", new lDate(2021, 1, 5, 0, 0),0),
                new Review(4, 1, 4.5, "This fries was great1", new lDate(2021, 1, 5, 0, 0),0),
                new Review(5, 1, 4.5, "This fries was great2", new lDate(2021, 1, 5, 0, 0),0)});
        reviewMap.put(2, new Review[]{new Review(6, 2, 4.5, "This drink was great", new lDate(2021, 1, 5, 0, 0),0),
                new Review(7, 2, 4.5, "This drink was great1", new lDate(2021, 1, 5, 0, 0),0),
                new Review(8, 2, 4.5, "This drink was great2", new lDate(2021, 1, 5, 0, 0),0)});
        reviewMap.put(3, new Review[]{new Review(9, 3, 4.5, "This burgor was great", new lDate(2021, 1, 5, 0, 0),0),
                new Review(10, 3, 4.5, "This burgor was great1", new lDate(2021, 1, 5, 0, 0),0),
                new Review(11, 3, 4.5, "This burgor was great2", new lDate(2021, 1, 5, 0, 0),0)});
        reviewMap.put(4, new Review[]{new Review(12, 4, 4.5, "This fries was great", new lDate(2021, 1, 5, 0, 0),0),
                new Review(13, 4, 4.5, "This fries was great1", new lDate(2021, 1, 5, 0, 0),0),
                new Review(14, 4, 4.5, "This fries was great2", new lDate(2021, 1, 5, 0, 0),0)});
        reviewMap.put(5, new Review[]{new Review(15, 5, 4.5, "This drink was great", new lDate(2021, 1, 5, 0, 0),0),
                new Review(16, 5, 4.5, "This drink was great1", new lDate(2021, 1, 5, 0, 0),0),
                new Review(17, 5, 4.5, "This drink was great2", new lDate(2021, 1, 5, 0, 0),0)});
        reviewMap.put(6, new Review[]{new Review(18, 6, 4.5, "This burgor was great", new lDate(2021, 1, 5, 0, 0),0),
                new Review(19, 6, 4.5, "This burgor was great1", new lDate(2021, 1, 5, 0, 0),0),
                new Review(20, 6, 4.5, "This burgor was great2", new lDate(2021, 1, 5, 0, 0),0)});
        reviewMap.put(7, new Review[]{new Review(21, 7, 4.5, "This fries was great", new lDate(2021, 1, 5, 0, 0),0),
                new Review(22, 7, 4.5, "This fries was great1", new lDate(2021, 1, 5, 0, 0),0),
                new Review(23, 7, 4.5, "This fries was great2", new lDate(2021, 1, 5, 0, 0),0)});
        reviewMap.put(8, new Review[]{new Review(24, 8, 4.5, "This drink was great", new lDate(2021, 1, 5, 0, 0),0),
                new Review(25, 8, 4.5, "This drink was great1", new lDate(2021, 1, 5, 0, 0),0),
                new Review(26, 8, 4.5, "This drink was great2", new lDate(2021, 1, 5, 0, 0),0)});
    }

    // Restaurant methods
    public List<Restaurant> getRestaurants(){
        return  restaurants;
    }
    public Restaurant getRestaurant(int id){
        return restaurants.get(id);
    }

    // Food methods
    public int[] getFoodList(int id) {
//        Food [] foodList = new Food[restaurants[id].getAssociatedFoodIds().length];
//        int a = 0;
//        for(int i : restaurants[id].getAssociatedFoodIds()){
//            foodList[a] = foodMap.get(i);
//        }
//        return foodList;

        return restaurants.get(id).getAssociatedFoodIds();
    } // id is the restaurant id
    public int[] getFoodList(String name){
        for(Restaurant r : restaurants){
            if(r.getName().equals(name)){
                return getFoodList(r.getId());
            }
        }
        return null;
    } // name is the restaurant name
    public Food getFood(int id){
        return foodMap.get(id);
    } // id is the food id
    public Food getFood(String name){
        for(Food f : foodMap.values()){
            if(f.getName().equals(name)){
                return f;
            }
        }
        return null;
    } // name is the food name

    public int[] getRecommendations(){
        return new int[]{1,5,6};
    }


    // Review methods
    public Review[] getReviews(int id){
        return reviewMap.get(id);
    } // id is the food id
    public Review[] getReviews(String name){
        for(Food f : foodMap.values()){
            if(f.getName().equals(name)){
                return getReviews(f.getId());
            }
        }
        return null;
    } // name is the food name
    public Review getReview(int id){
        for(Review[] r : reviewMap.values()){
            for(Review review : r){
                if(review.getId() == id){
                    return review;
                }
            }
        }
        return null;
    } // id is the review id

    @Override
    public boolean userLoggedIn(int token) {
        return false;
    }
}
