package com.teamwan.wander;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

public class FeedbackFragmentChart extends Fragment {

    private TextView title;
    private TextView next;
    private Chart chart;

    private String titleString;
    private ChartData chartData;
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
        chart = (Chart) view.findViewById(R.id.FragChart);
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

    public void setVals(String s, ChartData c, int n){
        titleString = s;
        chartData = c;
        nextChart = n;
    }

    private void initialiseFragment(){
        title.setText(titleString);
        if (titleString.length()>20) {
            title.setTextSize(26);
        }
        if (chartData instanceof LineData) {
            chart.setData((LineData) chartData);
            chart.invalidate();
        } else {
            chart.setData((BarData) chartData);
        }
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
