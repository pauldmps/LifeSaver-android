package com.paulshantanu.lifesaver;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.MapFragment;

/**
 * Created by Shantanu Paul on 13-03-2016.
 */

public class ScrollableMapFragment extends MapFragment {

    private OnTouchListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);

        TouchableWrapper framelayout = new TouchableWrapper(getActivity());
        framelayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ((ViewGroup)layout).addView(framelayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        return layout;
    }

    public void setListener(OnTouchListener listener){
        mListener = listener;
    }

    public interface OnTouchListener{
        void onTouch();
    }

    public class TouchableWrapper extends FrameLayout{
        public TouchableWrapper(Context context){
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mListener.onTouch();
                    break;
                case MotionEvent.ACTION_UP:
                    mListener.onTouch();
            }
            return super.dispatchTouchEvent(ev);
        }
    }
}



