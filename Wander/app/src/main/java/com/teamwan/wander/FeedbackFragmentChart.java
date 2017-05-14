package com.teamwan.wander;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedbackFragmentChart extends Fragment {

    private TextView title;
    private TextView next;
    private ImageView chart;

    private String titleString;
    private Bitmap chartBitmap;
    private int nextChart;

    /**
     * Sets the view for the fragment when created. Taken from Google/Android documentation
     * example code.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback_chart, container, false);
        return view;
    }

    /**
     * Override class that also initialises view fields.
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView) view.findViewById(R.id.FragChartTitle);
        next = (TextView) view.findViewById(R.id.FragChartNext);
        chart = (ImageView) view.findViewById(R.id.FragChart);
        initialiseFragment();
    }

    /**
     * Sets all typefaces to Futura.
     */
    public void setTypefaces() {
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/FuturaLT.ttf");
        title.setTypeface(tf);
        next.setTypeface(tf);;
    }

    public void setVals(String s, Bitmap bm, int n){
        titleString = s;
        chartBitmap = bm;
        nextChart = n;
    }

    private void initialiseFragment(){
        title.setText(titleString);
        if (titleString.length()>20) {
            title.setTextSize(26);
        }
        if (chartBitmap!=null) { chart.setImageBitmap(chartBitmap); }
        if (nextChart<1) {
            next.setText("Next performance chart unlocked!  >");
            next.setTextColor(getResources().getColor(R.color.positiveResult));
        } else {
            next.setText("Complete " + nextChart + " more games to unlock the next performance chart.");
        }
        if (nextChart == Integer.MAX_VALUE) { next.setText(""); }
        setTypefaces();
    }


}
