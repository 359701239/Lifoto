package com.external.lifoto.design.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.LinearLayout;

/**
 * Created by zuojie on 17-12-5.
 */

public class FitFloatActionButton extends FloatingActionButton {
    public FitFloatActionButton(Context context) {
        super(context);
    }

    public FitFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.bottomMargin = marginLayoutParams.bottomMargin + insets.getSystemWindowInsetBottom();
        }
        return super.onApplyWindowInsets(insets);
    }
}
