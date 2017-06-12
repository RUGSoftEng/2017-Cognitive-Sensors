package com.teamwan.wander;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static java.lang.Math.min;


/**
 * Fragment class to hold feedback screen to allow user to swipe through.
 */
public class FeedbackFragmentText extends Fragment {

    private TextView title, avg, avgVal, perc, percVal, next;
    private int nextChart;
    private float accuracy, response;

    /**
     * Sets the view for the fragment when created. Taken from Google/Android documentation
     * example code.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feedback_text, container, false);
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
        initialiseFragment();
    }

    private void initialiseFragment(){
        String resp = Double.toString(response);
        resp = resp.substring(0, min(resp.length(), 6));
        String acc = Double.toString(accuracy);
        acc = acc.substring(0, min(acc.length(), 5));
        percVal.setText(acc + "%");
        avgVal.setText(resp + "s");
        if (nextChart<1) {
            next.setText("Swipe left for next performance chart!");
            next.setTextColor(getResources().getColor(R.color.positiveResult));
        } else {
            next.setText("Complete " + nextChart + " more games to unlock the next performance chart.");
        }
        setTypefaces();
    }

    /**
     * Sets all typefaces to Futura.
     */
    private void setTypefaces() {
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/FuturaLT.ttf");
        title.setTypeface(tf);
        avg.setTypeface(tf);
        avgVal.setTypeface(tf);
        perc.setTypeface(tf);
        percVal.setTypeface(tf);
        next.setTypeface(tf);
    }

    /**
     * Takes feedback data and displays as relevant values.
     * @param avg A float to 2dp representing average response time.
     * @param perc An int representing correct tap percentage.
     * @param n An int representing the number of games required to unlock the next chart.
     */
    public void setVals(float avg, float perc, int n){
        response = avg;
        accuracy = perc;
        nextChart = n;
    }
}