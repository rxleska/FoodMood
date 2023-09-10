package com.example.mainapplication.ui.home.food;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapplication.R;
import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.MiddleMan;
import com.example.mainapplication.ui.lists.ReviewList;

import java.util.List;

/**
 * FOOD ADAPTER
 *
 * The Food Adapter class acts as an intermediary class between the Recycler View and the
 * restaurant list received from the Middle Man
 *
 * It is used to create the cards that are used by the Recycler View from the foods
 * listed in each restaurant
 *
 * @author Ryan Ledbetter
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{
    // creating a variable for array list and context.
    /**
     * This is the current restaurant from which we will get the food
     * that are available
     */
    private Restaurant currRestaurant;
    /**
     * This is the list of foods
     */
    private List<Food> foodList;
    /**
     * This is the context
     */
    private Context context;
    /**
     * middleMan is the middleman used to communicate with the server and cache.
     * @see com.example.mainapplication.datainterface.MiddleMan
     * */
    MiddleMan middleMan;


    /**
     * Constructs a Food Adapter which creates a new instance
     * of the Food Adapter when called
     * @param currRestaurant current restaurant from which the list of available foods
     *                       will come from
     * @param context context of the activity containing the food list
     */
    public FoodAdapter(Restaurant currRestaurant, Context context) {
        middleMan = new MiddleMan(context);
        this.currRestaurant = currRestaurant;
        Log.e("OOF", " " + currRestaurant.getAssociatedFoodIds().length);
        this.foodList = middleMan.getFoods(currRestaurant.getId());
        this.context = context;
    }

    /**
     * The constructor of the FoodAdapter creates a new instance of the FoodAdapter
     * this one is used for the center tab as it does not require a restaurant
     * @param foodList the list of all foods available
     * @param context context of the activity containing the food list
     */
    public FoodAdapter(List<Food> foodList, Context context) {
        middleMan = new MiddleMan(context);
        this.foodList = foodList;
        this.context = context;
    }
    /**
     * onCreateViewHolder creates a view holder for the recycler view.
     * @param parent parent view group
     * @param viewType view type
     * */
    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_card, parent, false);
        return new FoodAdapter.ViewHolder(view);
    }
    /**
     * onBindViewHolder is used to bind the data to our views of recycler view.
     * @param holder view holder
     * @param i position of the item in recycler view
     * */
    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder holder, int i) {
        // setting data to our views of recycler view.
        Food curr = foodList.get(i);
        int FoodID = foodList.get(i).getId();
//        Log.e("FoodName", curr.getName());
//        Log.e("FoodID", " "+ FoodID);
//        Log.e("FoodTypes" , " " + curr.getType().length);
        holder.foodName.setText(curr.getName());
        if(curr.getType() != null && curr.getType().length > 0){
            String ret = "";
            for(String type : curr.getType()){
                ret += type + ", ";
            }
            Log.e("FoodTypes" , " " + ret);
//            holder.foodType.setText(ret.substring(0, ret.length() - 2));
        }
        else {
            holder.foodType.setText("");
        }
//        holder.foodType.setText(curr.getCategory());
        holder.foodStars.setText(" " + curr.getAverageNumberOfStars());
        //Picasso.get().load(modal.getFoodImg()).into(holder.courseIV);
        holder.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Test "+ holder.restaurantName.getText(), Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(context, ReviewList.class);
                myIntent.putExtra("id", FoodID);
                myIntent.putExtra("isFood", true);
                context.startActivity(myIntent);
            }
        });


    }

    /**
     * getItemCount is used to get the size of the food list
     * @return size of the food list: returns 0 if the list is null
     */
    @Override
    public int getItemCount() {
        if(this.foodList == null){
            return 0;
        }
        return foodList.size();
    }

    /**
     * The class ViewHolder is used to hold the views of the recycler view.
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private TextView foodName, foodStars, foodType;
        private Button enter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // initializing our views with their ids.
            foodName = itemView.findViewById(R.id.idFoodName);
            foodStars = itemView.findViewById(R.id.idStars);
            foodType = itemView.findViewById(R.id.idFoodType);
            enter = itemView.findViewById(R.id.enterFood);
        };
    }
}
