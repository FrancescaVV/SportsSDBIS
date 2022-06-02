package com.example.sportssdbis;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.sportssdbis.models.ModelLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.sportssdbis.databinding.ActivityMapsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // below are the latitude and longitude
    // of 4 different locations.
    LatLng GymSDBIS = new LatLng(47.1783, 27.56699);
    LatLng BasketballCourt = new LatLng(47.1573, 27.5851);
    LatLng HockeyHall = new LatLng(47.1664120053, 27.5687748745);
    LatLng FootballField = new LatLng(47.1569956, 27.5896853); //statia meteorologica
    LatLng RecreationGround = new LatLng(47.16346, 27.58529);//opera nationala romana

    // creating array list for adding all our locations.
    private ArrayList<LatLng> locationArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // in below line we are initializing our array list.
        locationArrayList = new ArrayList<>();

        // on below line we are adding our
        // locations in our array list.
        locationArrayList.add(GymSDBIS);
        locationArrayList.add(BasketballCourt);
        locationArrayList.add(HockeyHall);
        locationArrayList.add(FootballField);
        locationArrayList.add(RecreationGround);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // inside on map ready method
        // we will be displaying all our markers.
        // for adding markers we are running for loop and
        // inside that we are drawing marker on our map.
        for (int i = 0; i < locationArrayList.size(); i++) {

            // below line is use to add marker to each location of our array list.
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Marker"));

            // below lin is use to zoom our camera on map.
           mMap.animateCamera(CameraUpdateFactory.zoomTo(20.0f));
           // mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
            // below line is use to move our camera to the specific location.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
        }
    }
}