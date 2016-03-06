package com.paulshantanu.lifesaver;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment supportMapFragment;

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fragmentManager = getChildFragmentManager();

        if(supportMapFragment == null){
            supportMapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.map,supportMapFragment).commit();
        }


        supportMapFragment.getMapAsync(this);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_maps,container,false);

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
                        int latitude = reader.getInt("latitude");
                        int longitude = reader.getInt("longitude");
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
