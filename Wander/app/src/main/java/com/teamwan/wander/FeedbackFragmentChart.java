package com.teamwan.wander;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;

public class FeedbackFragmentChart extends Fragment {

    private TextView title, next;
    private LineChart lineChart;
    private BarChart barChart;
    private FrameLayout lineLayout;
    private FrameLayout barLayout;
    private boolean chartTypeLine;
    private String titleString;
    private ChartData chartData;
    private LinearLayout layout;
    private int nextChart;
    private ArrayList<String> lineLabels;
    private Typeface futura;

    /**
     * Sets the view for the fragment when created. Taken from Google/Android documentation
     * example code.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feedback_chart, container, false);
    }

    /**
     * Override class that also initialises view fields.
     */
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = (TextView) view.findViewById(R.id.FragChartTitle);
        next = (TextView) view.findViewById(R.id.FragChartNext);
        barChart = (BarChart) view.findViewById(R.id.FragBarChart);
        lineChart = (LineChart) view.findViewById(R.id.FragLineChart);
        layout = (LinearLayout) view.findViewById(R.id.ChartLayout);
        lineLayout = (FrameLayout) view.findViewById(R.id.LineLayout);
        barLayout = (FrameLayout) view.findViewById(R.id.BarLayout);
        initialiseFragment();
    }

    /**
     * Sets all typefaces to Futura.
     */
    private void setTypefaces() {
        futura = Typeface.createFromAsset(getActivity().getAssets(),"fonts/FuturaLT.ttf");
        title.setTypeface(futura);
        next.setTypeface(futura);
    }

    public void setVals(String s, ChartData c, int n, boolean line){
        titleString = s;
        chartTypeLine = line;
        chartData = c;
        nextChart = n;
    }

    private void initialiseFragment(){
        title.setText(titleString);

        if (chartTypeLine) {
            layout.removeView(barLayout);
            chartData.setValueTextSize(12);
            lineChart.setData((LineData)chartData);
            XAxis axisX = lineChart.getXAxis();
            YAxis axisY = lineChart.getAxisRight();
            axisX.setDrawAxisLine(false);
            axisX.setDrawGridLines(false);
            axisX.setTypeface(futura);
            axisX.setYOffset(10);
            axisY.setDrawLabels(false);
            axisY.setXOffset(-20);
            axisY.setDrawAxisLine(false);
            axisY = lineChart.getAxisLeft();
            axisY.setAxisLineColor(Color.TRANSPARENT);
            axisY.setTypeface(futura);
            axisY.setDrawAxisLine(false);
            axisY.setXOffset(20);
            lineChart.setTouchEnabled(false);
            lineChart.invalidate();
            lineChart.setDescriptionTextSize(0);
        } else {
            layout.removeView(lineLayout);
            barChart.setData((BarData) chartData);
            barChart.setDescriptionTextSize(0);
            barChart.setTouchEnabled(false);
            barChart.invalidate();

        }
        if (nextChart < 1) {
            next.setText("Swipe left for next performance chart!");
            next.setTextColor(getResources().getColor(R.color.positiveResult));
        } else {
            next.setText("Complete " + nextChart + " more games to unlock the next performance chart.");
        }
        if (nextChart == Integer.MAX_VALUE) { next.setText(""); }
        setTypefaces();
    }


}
