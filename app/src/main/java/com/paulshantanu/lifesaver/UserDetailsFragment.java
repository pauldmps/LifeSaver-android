package com.paulshantanu.lifesaver;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnUserDetailsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserDetailsFragment#} factory method to
 * create an instance of this fragment.
 */
public class UserDetailsFragment extends Fragment {

    private OnUserDetailsFragmentInteractionListener mListener;
    private User user;
    private TextView tv_user_name;
    private TextView tv_user_distance;
    private TextView tv_user_email;
    private TextView tv_user_contact;
    private Button btn_user_details;

    public UserDetailsFragment() {

    }

    @SuppressLint("ValidFragment")
    public UserDetailsFragment(User user) {
        this.user = user;
    }

    /**
     * Use this factory method to create a new instance of
     */

    /* // TODO: Rename and change types and number of parameters
    public static UserDetailsFragment newInstance(User user) {
        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    } */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        tv_user_name = (TextView)view.findViewById(R.id.user_details_name);
        tv_user_distance = (TextView)view.findViewById(R.id.user_details_distance);
        tv_user_contact = (TextView)view.findViewById(R.id.user_details_contact);
        tv_user_email = (TextView)view.findViewById(R.id.user_details_email);
        btn_user_details = (Button)view.findViewById(R.id.button_request_details);

        tv_user_name.setText(user.getName());
        tv_user_distance.setText(user.getDistance());
        tv_user_email.setText(user.getEmail());
        tv_user_contact.setText(user.getContactNumber());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserDetailsFragmentInteractionListener) {
            mListener = (OnUserDetailsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserDetailsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return true;
    } */

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
    public interface OnUserDetailsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
