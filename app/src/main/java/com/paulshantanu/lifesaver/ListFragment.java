package com.paulshantanu.lifesaver;

import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paulshantanu.lifesaver.UiHelperClasses.MainRecylcerViewItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends Fragment {

    private String jsonToParse;
    private double latitude,longitude;
    private RecyclerView mainRecyclerView;

    private OnListFragmentInteractionListener mListener;
    private List<User> userList;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ListFragment newInstance(String jsonToParse, Double latitude, Double longitude) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("jsonToParse", jsonToParse);
        args.putDouble("latitude", latitude);
        args.putDouble("longitude",longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.jsonToParse = getArguments().getString("jsonToParse");
            this.latitude = getArguments().getDouble("latitude");
            this.longitude = getArguments().getDouble("longitude");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

            mainRecyclerView = (RecyclerView) view.findViewById(R.id.rv_main);
            mainRecyclerView.setHasFixedSize(true);
            mainRecyclerView.setNestedScrollingEnabled(false);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mainRecyclerView.addItemDecoration(new MainRecylcerViewItemDecoration(getActivity()));
            mainRecyclerView.setLayoutManager(layoutManager);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainRecyclerViewAdapter adapter = displayList(jsonToParse);
        mainRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MainRecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onClick(View v, int pos) {
                mListener.onListFragmentInteraction(userList.get(pos));
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(User user);
    }

    public MainRecyclerViewAdapter displayList(String jsonToDisplay){

        userList = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(jsonToDisplay);

            for (int i=0; i< array.length(); i++){

                JSONObject currentObject = array.getJSONObject(i);

                JSONArray locationArray = currentObject.getJSONArray("location");

                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(locationArray.getDouble(1),locationArray.getDouble(0),1);
                String address = addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getLocality();

                double listLatitude = currentObject.getJSONArray("location").getDouble(1);
                double listLongitude = currentObject.getJSONArray("location").getDouble(0);
                float[] results = new float[1];
                Log.i("debug", "latitude: " + latitude + "&" + " longitude: " + longitude);
                Location.distanceBetween(latitude, longitude, listLatitude, listLongitude, results);

                userList.add(new User(currentObject.getString("name"), address, currentObject.getString("bloodGroup"), String.valueOf(Math.round(results[0] / 1000)) + " Kms away"));


            }
            MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(userList);

            return adapter;
        }
        catch (Exception ex){ex.printStackTrace();}
        return null;
    }
}
