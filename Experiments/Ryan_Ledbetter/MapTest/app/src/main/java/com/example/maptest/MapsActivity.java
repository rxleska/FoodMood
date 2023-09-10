package com.example.maptest;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maptest.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final float CAFE = 270;//color for cafe
    public static final float DINING = 160;//color for dining centers
    public static final float FAST = 0;//color for fast casual
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Adds locations
        LatLng bookends = new LatLng(42.02818168247894, -93.6489022697704);
        LatLng business = new LatLng(42.02537716952394, -93.64449025987044);
        LatLng whirlybirds = new LatLng(42.024730569095006, -93.65383428870582);
        LatLng friley = new LatLng(42.023912998249024, -93.65045685213424);
        LatLng hawthorn = new LatLng(42.033921177986215, -93.64295561943727);

        // Adds markers on map
        mMap.addMarker(new MarkerOptions().position(bookends).title("Bookends Cafe").icon(BitmapDescriptorFactory.defaultMarker(CAFE)));
        mMap.addMarker(new MarkerOptions().position(business).title("Business Cafe").icon(BitmapDescriptorFactory.defaultMarker(CAFE)));
        mMap.addMarker(new MarkerOptions().position(whirlybirds).title("Whirlybird's").icon(BitmapDescriptorFactory.defaultMarker(CAFE)));
        mMap.addMarker(new MarkerOptions().position(friley).title("Friley Windows").icon(BitmapDescriptorFactory.defaultMarker(DINING)));
        mMap.addMarker(new MarkerOptions().position(hawthorn).title("Hawthorn").icon(BitmapDescriptorFactory.defaultMarker(FAST)));

        //sets zoom levels and starting location
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bookends, zoomLevel));
    }
}