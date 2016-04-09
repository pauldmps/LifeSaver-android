package com.paulshantanu.lifesaver;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.paulshantanu.lifesaver.UiHelperClasses.ScrollViewHelper;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ScrollableMapFragment mapFragment;
    private ScrollViewHelper mScrollViewHelper;
    double longitude, latitude;

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        mScrollViewHelper = (ScrollViewHelper)getActivity().findViewById(R.id.scrollViewHelper);

        if(mapFragment == null){

            Log.i("debug","Not getting map fragment");
            mapFragment = new ScrollableMapFragment();
            fragmentManager.beginTransaction().replace(R.id.map,mapFragment).commit();

        }

        mapFragment.getMapAsync(this);
        mapFragment.setListener(new ScrollableMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                mScrollViewHelper.requestDisallowInterceptTouchEvent(true);
            }
        });

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_maps,container,false);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setPadding(0,100,0,0);


        SharedPreferences shr = this.getActivity().getApplication().getSharedPreferences("loginData", Context.MODE_PRIVATE);
        String email = shr.getString("email", "");
        String token = shr.getString("loginToken","");

        List<Pair<String,String>> headerData = new ArrayList<>();
        headerData.add(new Pair<>("x-auth-email",email));
        headerData.add(new Pair<>("x-access-token",token));

        new APIAccessTask(this.getActivity(), "http://lifesaver-paulshantanu.rhcloud.com/auth/location/", "GET", null, headerData, new APIAccessTask.OnCompleteListener() {
            @Override
            public void onComplete(APIResponseObject result) {
                if(result.responseCode == HttpURLConnection.HTTP_OK){
                    try {
                        JSONObject reader = new JSONObject(result.response);
                        latitude = reader.getDouble("latitude");
                        longitude = reader.getDouble("longitude");
                        LatLng currentLocation = new LatLng(latitude, longitude);
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,12.0f));
                    }
                    catch (Exception ex){
                        ex.printStackTrace();}
                }
            }
        }).execute();


    }
}
