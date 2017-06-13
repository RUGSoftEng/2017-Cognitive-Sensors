package com.teamwan.wander;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Container class for layout view objects used in main menu overlay.
 * (Manipulation or looking up of view objects must be performed in the activity corresponding to
 * the layout, so no methods are used in this class.)
 */

class MenuLayoutComponents {

    private MainMenu menu;
    private Context context;
    public LinearLayout overlay;
    public LinearLayout contentBox;
    public ArrayList<TextView> text;
    public TextView title;
    public TextView body;
    public TextView accept;
    public TextView reject;
    public Typeface futura;
    public TextView close;
    public LinearLayout.LayoutParams params;
    public int overlayVis = View.INVISIBLE;
    public int infoVis = View.INVISIBLE;
}
