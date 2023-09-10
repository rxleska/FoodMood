package com.example.mainapplication.ui.home.restaurant;

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
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.ui.home.food.FoodListView;

import java.util.List;
/**
 * COURSE ADAPTER
 *
 * The Course Adapter class acts as an intermediary class between the Recycler View and the
 * restaurant list received from the Middle Man
 *
 * It is used to create the cards that are used by the Recycler View from
 * the list of restaurants
 *
 * @author Ryan Ledbetter
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    // creating a variable for array list and context.
    /**
     * List of restaurants
     */
    private List<Restaurant> restaurantList;
    /**
     * This is the context
     */
    private Context context;

    /**
     * Constructor for the Course Adapter which creates a new instance of
     * the Course Adapter when called
     *
     * @param restaurantList the list of restaurants that exist within the
     *                       Iowa State API
     * @param context this is the context of the application
     */
    public CourseAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    /**
     * Creates the View Holder for the restaurant list
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return ViewHolder(view) this is the view holder which contains the view for the list
     */
    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Creates each restaurant card, formats text, and sets onClick listener
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param i The position of the item within the adapter's data set.
     *
     * @implNote The garble 'â' represents the way Iowa State's API
     * displays apostrophes so we have to search for it and change it
     */
    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int i) {
        // setting data to our views of recycler view.
        Restaurant curr = restaurantList.get(i);
        int currID = restaurantList.get(i).getId();
        holder.restaurantID = curr.getId();
        String temp = curr.getName();
        if(curr.getName().contains("â"));
        {
            temp = temp.replace("â\u0080\u0099", "'");
        }
        holder.restaurantName.setText(temp);
        String type = curr.getType();
        type = type.replace('-',' ');
        holder.restaurantType.setText(type);
        String hours = curr.getHours();
        if(hours.isBlank()){
            hours = "Hours not available";
        }
        holder.restaurantDescription.setText(hours); //
//        Log.e("HOURS: ",curr.getHours());
        //Picasso.get().load(modal.getFoodImg()).into(holder.courseIV);
        holder.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Test "+ holder.restaurantName.getText(), Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(context, FoodListView.class);
                myIntent.putExtra("restaurantID", currID);
                context.startActivity(myIntent);
            }
        });
    }

    /**
     * getItemCount is used to get the size of the restaurant list
     * @return size of the restaurant list: returns 0 if the list is null
     */
    @Override
    public int getItemCount() {
        // returning the size of array list.
        if(restaurantList == null)
            return 0;
        else
            return restaurantList.size();
    }
    /**
     * The class ViewHolder is used to hold the views of the recycler view.
     * */
    public class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our views.
        private int restaurantID;
        private TextView restaurantName, restaurantDescription, restaurantType;
        private Button enter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our views with their ids.
            restaurantName = itemView.findViewById(R.id.idRestaurantName);
            restaurantDescription = itemView.findViewById(R.id.idRestaurantDescription);
            restaurantType = itemView.findViewById(R.id.idType);
            enter = itemView.findViewById(R.id.enterRestaurant);
        }
    }

}


