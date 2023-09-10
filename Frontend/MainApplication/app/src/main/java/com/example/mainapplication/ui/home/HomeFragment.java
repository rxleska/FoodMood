package com.example.mainapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapplication.R;
import com.example.mainapplication.databinding.FragmentHomeBinding;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.MiddleMan;
import com.example.mainapplication.datainterface.volleySimplification.VolleyRespInterface;
import com.example.mainapplication.ui.home.restaurant.CourseAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
/**
 * Home FRAGMENT
 * The home fragment is the left most tab of the app
 *
 * In this tab, the list of restaurants is available,
 * each restaurant being a clickable object leading to the food
 * list for the individual restaurants.
 *
 * @author Ryan Ledbetter
 */
public class HomeFragment extends Fragment {

    // creating variables for
    // our ui components.
    /**
     * This is the Recycler View which contains all of the
     * restaurant card objects
     */
    private RecyclerView restaurantRV;
    /**
     * middleMan is the middleman used to communicate with the server and cache.
     * @see com.example.mainapplication.datainterface.MiddleMan
     * */
    private MiddleMan middleMan;
    // variable for our adapter
    // class and array list
    /**
     * The adapter class for the list of restaurant cards
     */
    private CourseAdapter adapter;
    /**
     * Holds the list of restaurants received from Middle Man
     */
    private List<Restaurant> restaurantList;
    /**
     * This contains the view binding for this fragment
     * */
    private FragmentHomeBinding binding;
    /**
     * This is the progress bar that first appears when loading the app
     */
    private ProgressBar progressBar;

    /**
     * This is a floating action button that opens the filter list
     * */
    private FloatingActionButton filterButton;

    /**
     * This is the text at the top of the page
     * */
    private TextView filterText;

    /**
     * This is the text at the top of location filtering section
     * */
    private TextView filterLocationText;

    /**
     * This is the chip group that holds the chips for the restaurant filter by restaurant type
     * */
    private ChipGroup restaurantTypeChipGroup;
    /**
     * Filter booleans for the restaurant types
     * */
    private boolean incGetAndGo, incDiningCenter, incCafe, incConvenienceStore, incFastCasual;

    /**
     * This is the chip group that holds the chips for the restaurant filter by restaurant type
     * */
    private ChipGroup restaurantLocationChipGroup;
    /**
     * Filter booleans for the restaurant locations
     * */
    private boolean incEast, incWest, incNorth, incSouth;

    /**
     * This fragment is used to view the left tab of the application which
     * displays the list of restaurants where each restaurant connects to
     * the list of foods associated with itself
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return root returns the view that this fragment is a part of
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        middleMan = new MiddleMan((this.getContext()));

        // initializing our variables.
        restaurantRV = root.findViewById(R.id.idRVRestaurants);
        progressBar = root.findViewById(R.id.idPB);


        // ----------------- FILTERING -----------------

        // --------------------------------------------------
        // ----------------- TYPE FILTERING -----------------
        // --------------------------------------------------

        filterButton = root.findViewById(R.id.filterFloatingActionButton);

        incGetAndGo = true;
        incDiningCenter  = true;
        incCafe = true;
        incConvenienceStore  = true;
        incFastCasual = true;


        // Create the text at the top of the page
        filterText = root.findViewById(R.id.filterTitle);

        filterText.setText("Filter Restaurant Type");

        // Create the chip group for the restaurant type filter
        restaurantTypeChipGroup = root.findViewById(R.id.restaurantTypeChipGroup);

        filterText.setVisibility(View.GONE);
        restaurantTypeChipGroup.setVisibility(View.GONE);

        for(int i = 0; i < restaurantTypeChipGroup.getChildCount(); i++){
            ((Chip) restaurantTypeChipGroup.getChildAt(i)).setChecked(true);
        }


        ((Chip) restaurantTypeChipGroup.getChildAt(5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < restaurantTypeChipGroup.getChildCount(); i++){
                    ((Chip) restaurantTypeChipGroup.getChildAt(i)).setChecked(true);
                }
                    incGetAndGo = incDiningCenter = incCafe = incConvenienceStore = incFastCasual = true;

            }
        });

        restaurantTypeChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                Log.d("HomeFragment", "onCheckedChanged: " + checkedIds.toString());
                // Not sure why adding 6 is necessary but it works
                incGetAndGo = checkedIds.contains(0+12);
                incDiningCenter = checkedIds.contains(1+12);
                incCafe = checkedIds.contains(2+12);
                incConvenienceStore = checkedIds.contains(3+12);
                incFastCasual = checkedIds.contains(4+12);
                //log current state
//                Log.d("HomeFragment", "incGetAndGo: " + incGetAndGo);
//                Log.d("HomeFragment", "incDiningCenter: " + incDiningCenter);
//                Log.d("HomeFragment", "incCafe: " + incCafe);
//                Log.d("HomeFragment", "incConvenienceStore: " + incConvenienceStore);
//                Log.d("HomeFragment", "incFastCasual: " + incFastCasual);
//                if(!incGetAndGo && !incDiningCenter && !incCafe && !incConvenienceStore && !incFastCasual){
//                    //if all are false, set all to true
//                    incGetAndGo = incDiningCenter = incCafe = incConvenienceStore = incFastCasual = true;
//                    for(int i = 0; i < restaurantTypeChipGroup.getChildCount(); i++){
//                        ((Chip) restaurantTypeChipGroup.getChildAt(i)).setChecked(true);
//                    }
//                }
            }
        });

        // ---------------------------------------------------------
        // ------------------ LOCATIONS FILTERING ------------------
        // ---------------------------------------------------------

        // Create the text at the top of the section
        filterLocationText = root.findViewById(R.id.filterTitlelocation);

        filterLocationText.setText("Filter Locations");

        // Create the chip group for the restaurant location filter
        restaurantLocationChipGroup = root.findViewById(R.id.restaurantLocationChipGroup);

        //set text and chip group to gone
        filterLocationText.setVisibility(View.GONE);
        restaurantLocationChipGroup.setVisibility(View.GONE);

        // init the boolean states of the filters
        incWest = incEast = incNorth = incSouth = true;

        //Set all chips to checked
        for(int i = 0; i < restaurantLocationChipGroup.getChildCount(); i++){
            ((Chip) restaurantLocationChipGroup.getChildAt(i)).setChecked(true);
        }

        // set clear filter onclick action
        ((Chip) restaurantLocationChipGroup.getChildAt(4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < restaurantLocationChipGroup.getChildCount(); i++){
                    ((Chip) restaurantLocationChipGroup.getChildAt(i)).setChecked(true);
                }
                incWest = incEast = incNorth = incSouth = true;
            }
        });

        // set onclick action for the chip group
        restaurantLocationChipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
//                Log.d("HomeFragment", "onCheckedChanged: " + checkedIds.toString());
                // Not sure why adding 5 is necessary but it works
                incWest = checkedIds.contains(0+18);
                incEast = checkedIds.contains(1+18);
                incNorth = checkedIds.contains(2+18);
                incSouth = checkedIds.contains(3+18);
                    //log current state
            }});

        // ---------------------------------------------------------
        // ------------------ FILTER BUTTON -------------------
        // ---------------------------------------------------------
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //toggle visibility of filter text and chip group and recycler view
                if (filterText.getVisibility() == View.GONE) {
                    filterText.setVisibility(View.VISIBLE);
                    filterLocationText.setVisibility(View.VISIBLE);
                    restaurantTypeChipGroup.setVisibility(View.VISIBLE);
                    restaurantLocationChipGroup.setVisibility(View.VISIBLE);
                    restaurantRV.setVisibility(View.GONE);
                } else {
                    filterText.setVisibility(View.GONE);
                    filterLocationText.setVisibility(View.GONE);
                    restaurantTypeChipGroup.setVisibility(View.GONE);
                    restaurantLocationChipGroup.setVisibility(View.GONE);
                    restaurantRV.setVisibility(View.VISIBLE);
                    getData();
                    buildRecyclerView();
                }
            }
        });

        // below line we are creating a new array list
        getData();



        // calling method to
        // build recycler view.
        buildRecyclerView();

        return root;
    }

    /**
     * Connects with Middle Man to verify data and then creates the recycler view
     *
     * @implNote Starts by connecting to Middle Man to verify data, once verified
     * removing progress bar and displays the recycler view
     *
     */
    private void getData() {
        if (middleMan.getRestaurants() == null || middleMan.getRestaurants().size() == 0) {
            middleMan.getRestaurantsAsync(new VolleyRespInterface() {
                @Override
                public void onSuccess(String result) {
                    if (result.equals("error")) {
                        Toast.makeText(getView().getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1500);
                            getData();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                    } else if (result.equals("true")) {
                        getData();
                    } else if (result.equals("false")) {
                        Toast.makeText(getView().getContext(), "Incomplete Data", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1500);
                            getData();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                    }
                    middleMan.getHoursListing(result2 -> {
                        result2 = result2.replace("null", "\"\""); // this does nothing on purpose
                    });
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            restaurantRV.setVisibility(View.VISIBLE);
            List<Restaurant> unfilteredRestaurantList = middleMan.getRestaurants();
            restaurantList = new ArrayList<>();
            for(Restaurant r : unfilteredRestaurantList){
//                restaurantList.add(r);
                boolean add = false;
                if(r.getType().equals("get-go") && incGetAndGo){
                    add = true;
                } else if(r.getType().equals("dining-center") && incDiningCenter){
                    add = true;
                } else if(r.getType().equals("cafe") && incCafe){
                    add = true;
                } else if(r.getType().equals("convenience-store") && incConvenienceStore){
                    add = true;
                } else if(r.getType().equals("fast-casual") && incFastCasual){
                    add = true;
                }

                if(r.getLongitude() > -93.6471){ // east
                    if(r.getLatitude() > 42.0261){ // north
                        //north east
                        if(!incNorth || !incEast){
                            add = false;
                        }
                    }
                    else{ // south
                        //south east
                        if(!incSouth || !incEast){
                            add = false;
                        }
                    }
                }
                else{ // west
                    if(r.getLatitude() > 42.0261){ // north
                        //north west
                        if(!incNorth ||  !incWest){
                            add = false;
                        }
                    }
                    else{ // south
                        //south west
                        if(!incSouth || !incWest){
                            add = false;
                        }
                    }
                }

//
//                if(r.getLongitude() > -93.6471 && !incEast){
//                    add = false;
//                }
//                if(r.getLongitude() < -93.6471 && !incWest){
//                    add = false;
//                }
//                if(r.getLatitude() < 42.0261 && !incSouth){
//                    add = false;
//                }
//                if(r.getLatitude() > 42.0261 && !incNorth){
//                    add = false;
//                }
////
                if(add){
                    restaurantList.add(r);
                }
            }
            //filter the list
        }
    }

    /**
     * This just builds the recycler view
     * @implNote This method is called when on the left tab
     * to build the restaurant list
     */
        private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new CourseAdapter(restaurantList, this.getContext());

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        restaurantRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        restaurantRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        restaurantRV.setAdapter(adapter);
    }

    /**
     * Destroys the view and removes the binding
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}