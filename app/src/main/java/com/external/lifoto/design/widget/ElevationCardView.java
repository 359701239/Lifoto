package com.external.lifoto.design.widget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.external.lifoto.R;

/**
 * Created by zuojie on 17-3-9.
 */

public class ElevationCardView extends CardView {

    public ElevationCardView(Context context) {
        super(context);
    }

    public ElevationCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ElevationCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isClickable()) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                animate().translationZ(getMaxCardElevation())
                        .setDuration(getResources().getInteger(R.integer.anim_duration_short))
                        .start();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                animate().translationZ(0)
                        .setDuration(getResources().getInteger(R.integer.anim_duration_short_little))
                        .start();
                break;
        }
        return super.onTouchEvent(event);
    }
}
