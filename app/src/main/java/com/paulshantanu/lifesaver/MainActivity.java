package com.paulshantanu.lifesaver;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AlphaForeGroundColorSpan mAlphaForegroundColorSpan;
    private SpannableString mSpannableString;
    private List<User> userList;
    private RecyclerView mainRecyclerView;
    private double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(0);
        getSupportActionBar().setTitle("");

        mSpannableString = new SpannableString("Lifesaver");
        mAlphaForegroundColorSpan = new AlphaForeGroundColorSpan(0xFFFFFF);
        mainRecyclerView = (RecyclerView)findViewById(R.id.rv_main);
        mainRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mainRecyclerView.addItemDecoration(new MainRecylcerViewItemDecoration(MainActivity.this));
        mainRecyclerView.setLayoutManager(layoutManager);




        ScrollViewHelper scrollViewHelper = (ScrollViewHelper) findViewById(R.id.scrollViewHelper);
        scrollViewHelper.setOnScrollViewListener(new ScrollViewHelper.OnScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollViewHelper v, int l, int t, int oldl, int oldt) {
                setTitleAlpha(255 - ScrollViewHelper.getAlphaforActionBar(v.getScrollY()));
                toolbar.getBackground().setAlpha(ScrollViewHelper.getAlphaforActionBar(v.getScrollY()));
            }


        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        SharedPreferences shr = getApplication().getSharedPreferences("loginData",MODE_PRIVATE);
        String email = shr.getString("email", "");
        String token = shr.getString("loginToken","");

        List<Pair<String,String>> headerData = new ArrayList<>();
        headerData.add(new Pair<>("x-auth-email",email));
        headerData.add(new Pair<>("x-access-token",token));

        new APIAccessTask(MainActivity.this, "http://lifesaver-paulshantanu.rhcloud.com/auth/location/", "GET", null, headerData, new APIAccessTask.OnCompleteListener() {
            @Override
            public void onComplete(APIResponseObject result) {
                if(result.responseCode == HttpURLConnection.HTTP_OK){
                    try {
                        JSONObject locationObject = new JSONObject(result.response);
                        latitude = locationObject.getDouble("latitude");
                        longitude = locationObject.getDouble("longitude");

                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }).execute();

        headerData.add(new Pair<>("max-distance","10000"));

        new APIAccessTask(MainActivity.this, "http://lifesaver-paulshantanu.rhcloud.com/auth/nearbyusers", "GET", null, headerData, new APIAccessTask.OnCompleteListener() {
            @Override
            public void onComplete(APIResponseObject result) {
                Log.i("debug",String.valueOf(result.responseCode));

                if(result.responseCode == HttpURLConnection.HTTP_OK) {
                    displayList(result.response);
                }

            }
        }).execute();

    }

    public void displayList(String jsonToDisplay){

        userList = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(jsonToDisplay);

            for (int i=0; i< array.length(); i++){

                JSONObject currentObject = array.getJSONObject(i);

                JSONArray locationArray = currentObject.getJSONArray("location");

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());

                List<Address> addresses = geocoder.getFromLocation(locationArray.getDouble(1),locationArray.getDouble(0),1);
                String address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality();

                double listLatitude = currentObject.getJSONArray("location").getDouble(1);
                double listLongitude = currentObject.getJSONArray("location").getDouble(0);
                float[] results = new float[1];
                Log.i("debug","latitude: " + latitude+ "&" + " longitude: " + longitude);
                Location.distanceBetween(latitude,longitude,listLatitude,listLongitude,results);

                userList.add(new User(currentObject.getString("name"),address,currentObject.getString("bloodGroup"),String.valueOf(Math.round(results[0]/1000))+ " Kms away"));
                MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(userList);

                mainRecyclerView.setAdapter(adapter);
            }
        }
        catch (Exception ex){ex.printStackTrace();}
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setTitleAlpha(float alpha) {
        if(alpha<1){ alpha = 1; }
        mAlphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(mSpannableString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
