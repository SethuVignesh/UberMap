package com.newtra.locationsdk;

import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ademar.phasedseekbar.PhasedInteractionListener;
import ademar.phasedseekbar.PhasedListener;
import ademar.phasedseekbar.PhasedSeekBar;
import ademar.phasedseekbar.SimplePhasedAdapter;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {



    private GoogleMap mMap;

    protected PhasedSeekBar psbLike, psbStar, psbNoImages;
    CameraUpdate cu;
    LatLngBounds.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Gets the RangeBar

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PhasedSeekBar psbHorizontal = (PhasedSeekBar) findViewById(R.id.psb_hor);
        psbLike = (PhasedSeekBar) findViewById(R.id.psb_like);
        psbStar = (PhasedSeekBar) findViewById(R.id.psb_star);
        psbNoImages = (PhasedSeekBar) findViewById(R.id.psb_no_images);

        final Resources resources = getResources();

        psbHorizontal.setAdapter(new SimplePhasedAdapter(resources, new int[] {
                R.drawable.btn_square_selector,
                R.drawable.btn_triangle_selector,
                R.drawable.btn_xis_selector }));

        psbLike.setAdapter(new SimplePhasedAdapter(resources, new int[] {
                R.drawable.btn_like_selector,
                R.drawable.btn_unlike_selector }));
        psbStar.setAdapter(new SimplePhasedAdapter(resources, new int[] {
                R.drawable.btn_star1_selector,
                R.drawable.btn_star2_selector,
                R.drawable.btn_star3_selector,
                R.drawable.btn_star4_selector,
                R.drawable.btn_star5_selector }));
        psbNoImages.setAdapter(new SimplePhasedAdapter(resources, new int[] {
                R.drawable.no_image_shape }));

        psbHorizontal.setListener(new PhasedListener() {
            @Override
            public void onPositionSelected(int position) {
//                psbLike.setVisibility(position == 0 ? View.VISIBLE : View.INVISIBLE);
//                psbStar.setVisibility(position == 1 ? View.VISIBLE : View.INVISIBLE);
//                psbNoImages.setVisibility(position == 2 ? View.VISIBLE : View.INVISIBLE);
                switch(position){
                    case 0:
                        //UNITED STATES OF AMERICA
                        mSetUpMap("uk");
                        break;
                    case 1:
                        //SINGAPORE
                        mSetUpMap("singapore");
                        break;
                    case 2:
                        //INDIA
                        mSetUpMap("india");
                        break;
                    default:
                        //US
                        mSetUpMap("uk");
                        break;
                }
            }
        });

        psbHorizontal.setInteractionListener(new PhasedInteractionListener() {
            @Override
            public void onInteracted(int x, int y, int position, MotionEvent motionEvent) {
                Log.d("PSB", String.format("onInteracted %d %d %d %d", x, y, position, motionEvent.getAction()));
            }
        });

        psbLike.setPosition(0);
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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mSetUpMap("us");
    }
    List<Marker> markersList;
    public void mSetUpMap(final String country) {
        /**clear the map before redraw to them*/
        mMap.clear();
        /**Create dummy Markers List*/
       markersList = new ArrayList<Marker>();
        Marker us=null,uk=null,singapore= null;
        us = mMap.addMarker(new MarkerOptions().position(new LatLng(
                35.814,-88.2225)).title("USA"));
        singapore = mMap.addMarker(new MarkerOptions().position(new LatLng(
                1.3521, 103.8198)).title("SINGAPORE"));
        uk = mMap.addMarker(new MarkerOptions().position(new LatLng(
                55.0461, -12.4040)).title("UK"));
        switch (country) {
            case "us":
                markersList.add(us);
                break;
            case"singapore":
                markersList.add(singapore);
                break;
            case "uk":
                markersList.add(uk);
                break;
            default:
                markersList.add(us);
        }

    /**create for loop for get the latLngbuilder from the marker list*/
        builder = new LatLngBounds.Builder();
        for (Marker m : markersList) {
            builder.include(m.getPosition());
        }
        /**initialize the padding for map boundary*/
        int padding = 250;
        /**create the bounds from latlngBuilder to set into map camera*/
        LatLngBounds bounds = builder.build();
        /**create the camera with bounds and padding to set into map*/
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        /**call the map call back to know map is loaded or not*/
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
//                mMap.animateCamera(cu);
//                mMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
                switch (country) {
                    case "us":
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                35.814,-88.2225), 2.0f));
                        break;
                    case"singapore":
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                1.3521, 103.8198), 2.0f));
                        break;
                    case "uk":
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                55.0461, -12.4040), 2.0f));
                        break;
                    default:
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                35.814,-88.2225), 2.0f));
                }



            }
        });
    }



}
