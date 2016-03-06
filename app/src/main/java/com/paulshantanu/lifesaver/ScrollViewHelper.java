package com.paulshantanu.lifesaver;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Shantanu Paul on 27-02-2016.
 */
public class ScrollViewHelper extends ScrollView {

    private OnScrollViewListener mOnScrollViewListener;

    public ScrollViewHelper(Context context) {
        super(context);
    }
    public ScrollViewHelper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ScrollViewHelper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface OnScrollViewListener {
        void onScrollChanged(ScrollViewHelper v, int l, int t, int oldl, int oldt );
    }

    public void setOnScrollViewListener(OnScrollViewListener l) {
        this.mOnScrollViewListener = l;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt );
        super.onScrollChanged( l, t, oldl, oldt );
    }

    public static  int getAlphaforActionBar(int scrollY) {
        int minDist = 0, maxDist = 550;
        if (scrollY > maxDist) {
            return 255;
        } else {
            if (scrollY < minDist) {
                return 0;
            } else {
                return (int) ((255.0 / maxDist) * scrollY);
            }
        }
    }
}
