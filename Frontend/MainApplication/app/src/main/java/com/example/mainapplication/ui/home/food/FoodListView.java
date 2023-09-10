package com.example.mainapplication.ui.home.food;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapplication.R;
import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.MiddleMan;

import java.util.List;

/**
 * FOOD LIST VIEW
 *
 * Food List view allows for the list of foods to be seen and created
 * and build the recycler view
 *
 * @author Ryan Ledbetter
 */
public class FoodListView extends AppCompatActivity{

    /**
     * The recycler view that will be created to display
     * the food list
     */
    RecyclerView foodRV;
    /**
     * Stores the current ID of the restaurant
     * this is passed through intent
     */
    int currID;
    /**
     * context is the context of the activity containing the
     * list of available foods
     */
    Context context;
    /**
     * Holds the adapter class which is used to shape the food list
     * @see com.example.mainapplication.ui.home.food.FoodAdapter
     */
    FoodAdapter adapter;
    /**
     * foodList is a list of the type Food that holds all the food objects
     */
    List<Food> foodList;
    /**
     * middleMan is the middleman used to communicate with the server and cache.
     * @see com.example.mainapplication.datainterface.MiddleMan
     * */
    private MiddleMan middleMan;

    /**
     * This is the activity that is used to display the food list from
     * within a restaurant
     * @param savedInstanceState Bundle of intent with the required data
     * @implNote
     * The intent that is getting passed carries the ID for
     * the restaurant which defaults to 0 but should always be
     * a value related to the restaurant
     * intent restaurantID: currID
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);
        context = this;
        Intent mIntent = getIntent();
        currID = mIntent.getIntExtra("restaurantID", 0);
        middleMan = new MiddleMan(this);
        foodList = middleMan.getFoods(currID);
        foodRV = findViewById(R.id.idFoodRV);

        buildRecyclerView();
    }

    /**
     * The build recycler view method build the recycler view
     * @implNote This method is called when clicking on a restaurant or navigating
     * to the middle tab of the application
     * */
    private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new FoodAdapter(middleMan.getRestaurant(currID), context);

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(context);
        foodRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        foodRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        foodRV.setAdapter(adapter);
    }
}
