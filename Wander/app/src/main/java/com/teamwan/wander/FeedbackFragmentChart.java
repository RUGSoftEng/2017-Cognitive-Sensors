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

    /**
     * Sets the view for the fragment when created. Taken from Google/Android documentation
     * example code.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.feedback_chart, container, false);
        return rootView;
    }

    /**
     * Override class that also initialises view fields.
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView) view.findViewById(R.id.FragChartTitle);
        next = (TextView) view.findViewById(R.id.FragChartNext);
        chart = (ImageView) view.findViewById(R.id.FragChart);
        setTypefaces();
    }

    /**
     * Sets all typefaces to Futura.
     */
    public void setTypefaces() {
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/FuturaLT.ttf");
        title.setTypeface(tf);
        next.setTypeface(tf);;
//        setTitle("Last 6 Sessions:");
    }

    public void setVals(Bitmap bm, int n){
        chart.setImageBitmap(bm);
        if (n<1) {
            next.setText("Next performance chart unlocked!  >");
            next.setTextColor(getResources().getColor(R.color.positiveResult));
        } else {
            next.setText("Complete " + n + " more games to unlock the next performance chart.");
        }
    }

    /**
     * Setter for fragment title.
     */
    public void setTitle(String t){
        title.setText(t);
    }
}
