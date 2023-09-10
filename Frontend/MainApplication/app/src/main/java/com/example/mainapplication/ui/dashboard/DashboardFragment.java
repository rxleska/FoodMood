package com.example.mainapplication.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainapplication.R;
import com.example.mainapplication.datainterface.DataClasses.Food;
import com.example.mainapplication.datainterface.DataClasses.Restaurant;
import com.example.mainapplication.datainterface.MiddleMan;
import com.example.mainapplication.datainterface.volleySimplification.VolleyRespInterface;
import com.example.mainapplication.ui.home.food.FoodAdapter;
import com.example.mainapplication.ui.home.food.FoodListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainapplication.databinding.FragmentDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
/**
 * DASHBOARD FRAGMENT
 * The dashboard fragment is the center tab of the app
 * Within this tab you will find the list of "Recommended Foods" and a map that
 * depicts all the restaurants on campus. This map is accessible via pushing the button
 * object in the bottom right. The Recommended food list recommends foods to the user based
 * on the average rating that the food receives from other users of the app as well as based on
 * current availability.
 *
 * @author Ryan Ledbetter
 */
public class DashboardFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    /**
     * This contains the view binding for this fragment
     * */
    private FragmentDashboardBinding binding;
    /**
     * This is the button that when pressed, switches the list with the mapView
     */
    private FloatingActionButton mapActivation;
    /**
     * This is the map object, it holds the current instance of the map of campus and is powered using
     * the Google Maps API
     */
    private GoogleMap mMap;
    /**
     * View which displays the map when called
     */
    private MapView mapView;
    /**
     * middleMan is the middleman used to communicate with the server and cache.
     * @see com.example.mainapplication.datainterface.MiddleMan
     * */
    private MiddleMan middleMan;
    /**
     * Holds the list of restaurants received from Middle Man
     */
    private List<Restaurant> restaurantList;
    /**
     * Holds the list of foods received from each restaurant
     */
    private List<Food> foodList;
    /**
     * The adapter class for the list of food cards
     * @see com.example.mainapplication.ui.home.food.FoodAdapter
     */
    private FoodAdapter adapter;
    /**
     * This is the Recycler View which contains all of the
     * food card objects
     */
    private RecyclerView foodRV;
    /**
     * Used to mark color for Cafes
     */
    public static final float CAFE = 270;//color for cafe
    /**
     * Used to mark color for Dining Halls
     */
    public static final float DINING = 160;//color for dining centers
    /**
     * Used to mark color for Fast Casual
     */
    public static final float FAST = 0;//color for fast casual

    /**
     * This fragment is used to view the center tab of the application which
     * houses recommendations and also contains a map from which you will be able
     * to see the locations of restaurants
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        super.onCreate(savedInstanceState);
        mapActivation = root.findViewById(R.id.mapLauncher);
        mapView = (MapView) root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::onMapReady);
        middleMan = new MiddleMan(this.getContext());

        foodRV = root.findViewById(R.id.idRVFoods);

        //foodList = (List<Food>) middleMan.getStorageInterface().getFoods().values();
        foodList = new ArrayList<>();
        middleMan.getStorageInterface().getFoods().values().forEach(food -> {
            foodList.add(food);
        });

        buildRecyclerView();

        mapActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mapView.getVisibility() == View.GONE)
                    mapView.setVisibility(View.VISIBLE);
                else
                    mapView.setVisibility(View.GONE);
            }
        });

        return root;
    };

    /**
     *  This creates the map object and within centers it and calls mapPins
     *  to generate the markers
     * @param googleMap this is the map object that the API uses to
     *                  create and display the map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        LatLng mapCenter = new LatLng(42.02818168247894, -93.6489022697704);

        mapPins();
        //sets zoom levels and starting location
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, zoomLevel));
    }

    /**
     * Map Pins creates the pins that appear on the map
     * @implNote
     * This functions by calling the middle man to get teh restaurant data and
     * if it fails, it requests the data and then recursively calls itself to generate
     * the pins
     *
     * It sets the restaurant, id, latitude, and longitude as well as using zIndex as a way
     * to pass restaurant IDs so that onClick functionality works
     */
    private void mapPins() {
        if (middleMan.getRestaurants() == null || middleMan.getRestaurants().size() == 0) {
            middleMan.getRestaurantsAsync(new VolleyRespInterface() {
                @Override
                public void onSuccess(String result) {
                    if (result.equals("error")) {
                        Toast.makeText(getView().getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1500);
                            mapPins();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                    } else if (result.equals("true")) {
                        mapPins();
                    } else if (result.equals("false")) {
                        Toast.makeText(getView().getContext(), "Incomplete Data", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1500);
                            mapPins();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }

                    }
                }
            });
        } else {
            for (int i = 0; i < middleMan.getRestaurants().size(); i++) {
                restaurantList = middleMan.getRestaurants();
                Log.e("Restaurant", " " + restaurantList.get(i).getName());
                Log.e("ID", " " + restaurantList.get(i).getId());
                Log.e("Latitude", " " + restaurantList.get(i).getLatitude());
                Log.e("Longitude", " " + restaurantList.get(i).getLongitude());
                //zIndex is being used to store the ID for the restaurant
                mMap.addMarker(new MarkerOptions().position(new LatLng(restaurantList.get(i).getLatitude(), restaurantList.get(i).getLongitude())).title(restaurantList.get(i).getName()).icon(BitmapDescriptorFactory.defaultMarker(CAFE)).zIndex(restaurantList.get(i).getId()));
            }
        }

    }

    /**
     * This just builds the recycler view
     * @implNote This method is called when on the center tab
     */
    private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new FoodAdapter(foodList, this.getContext());

        // adding layout manager
        // to our recycler view.
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext());
        foodRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        foodRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        foodRV.setAdapter(adapter);
    }

    /**
     *
     * @param marker this is the marker that is clicked
     * @return returns true
     * @implNote Clicking on the marker takes user to the
     * page for the restaurant that the marker is linked to
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.e("ERROR", " " + marker.getZIndex());
        Intent myIntent = new Intent(this.getContext(), FoodListView.class);
        myIntent.putExtra("restaurantID", (int) marker.getZIndex());
        this.getContext().startActivity(myIntent);
        //Toast.makeText(getContext(), "Test " + marker.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }

}