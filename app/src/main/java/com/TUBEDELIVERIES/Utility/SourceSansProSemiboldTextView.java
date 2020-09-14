package com.TUBEDELIVERIES.Utility;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class SourceSansProSemiboldTextView  extends androidx.appcompat.widget.AppCompatTextView {
    public SourceSansProSemiboldTextView(Context context) {
        super(context);
        createFont();
    }

    public SourceSansProSemiboldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createFont();
    }

    public SourceSansProSemiboldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createFont();
    }

    public void createFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Semibold.otf");
        setTypeface(font);
    }
}
