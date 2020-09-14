package com.TUBEDELIVERIES.Utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class SourceSansProRegularItTextView extends androidx.appcompat.widget.AppCompatTextView {
    public SourceSansProRegularItTextView(Context context) {
        super(context);
        createFont();
    }

    public SourceSansProRegularItTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public SourceSansProRegularItTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-LightIt_0.otf");
        setTypeface(font);
    }
}
