package com.teamwan.wander;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Fragment class to hold feedback screen to allow user to swipe through.
 */
public class FeedbackFragmentText extends Fragment {

    private TextView title;
    private TextView avg;
    private TextView avgVal;
    private TextView perc;
    private TextView percVal;
    private TextView next;

    /**
     * Sets the view for the fragment when created. Taken from Google/Android documentation
     * example code.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.feedback_text, container, false);
        return rootView;
    }

    /**
     * Override class that also initialises view fields.
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView) view.findViewById(R.id.FragTextTitle);
        avg = (TextView) view.findViewById(R.id.FragAvg);
        avgVal = (TextView) view.findViewById(R.id.FragAvgVal);
        perc = (TextView) view.findViewById(R.id.FragPerc);
        percVal = (TextView) view.findViewById(R.id.FragPercVal);
        next = (TextView) view.findViewById(R.id.FragTextNext);
        setTypefaces();
    }

    /**
     * Sets all typefaces to Futura.
     */
    public void setTypefaces() {
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/FuturaLT.ttf");
        title.setTypeface(tf);
        avg.setTypeface(tf);
        avgVal.setTypeface(tf);
        perc.setTypeface(tf);
        percVal.setTypeface(tf);
        next.setTypeface(tf);;
    }

    /**
     * Takes feedback data and displays as relevant values.
     * @param avg A float to 2dp representing average response time.
     * @param perc An int representing correct tap percentage.
     * @param n An int representing the number of games required to unlock the next chart.
     */
    public void setVals(float avg, int perc, int n){
        avgVal.setText(Float.toString(avg) + "s");
        percVal.setText(perc + "%");
        if (n<1) {
            next.setText("Next performance chart unlocked!  >");
            next.setTextColor(getResources().getColor(R.color.positiveResult));
        } else {
            next.setText("Complete " + n + " more games to unlock the next performance chart.");
        }
    }
}