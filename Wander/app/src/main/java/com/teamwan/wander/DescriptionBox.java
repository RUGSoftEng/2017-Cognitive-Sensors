package com.teamwan.wander;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/** Sets Typeface of DescriptionBox to FuturaLT
 * Created by Jake on 06/03/2017.
 */

public class DescriptionBox extends TextView {

    public DescriptionBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DescriptionBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DescriptionBox(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface t = Typeface.createFromAsset(getContext().getAssets(), "fonts/FuturaLT.ttf");
        setTypeface(t);
    }
}
