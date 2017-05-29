package com.teamwan.wander;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.GameSession;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Feedback extends AppCompatActivity {

    private ArrayList<TextView> text;
    int chartsUnlocked = 5;

    private GraphData data;

    FeedbackFragmentText page1;
    FeedbackFragmentChart page2;
    FeedbackFragmentChart page3;
    FeedbackFragmentChart page4;
    FeedbackFragmentChart page5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);
        data = new GraphData(this);
        data.calculateGraphData(4);
        data.getLastNGameSessions(4);
        data.getLatestNAverageResponses(4);
        setTypefaces();
        chartsUnlocked = Math.min(1 + fetchSessions() / 6, 5);
        mPager = (ViewPager) findViewById(R.id.FeedbackContent);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        initialiseFragments();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * Initialises feedback data for the 5 fragments in the view pager.
     */
    private void initialiseFragments(){
        page1 = new FeedbackFragmentText();
        page2 = new FeedbackFragmentChart();
        page3 = new FeedbackFragmentChart();
        page4 = new FeedbackFragmentChart();
        page5 = new FeedbackFragmentChart();
        page1.setVals(fetchResponse(), fetchAccuracy(), 6-fetchSessions());
        page2.setVals("Last 6 Sessions", createLineChart(0, fetchLineDataAcc()), 12-fetchSessions(), true);
        page3.setVals("Last 12 Sessions", createLineChart(1, fetchLineDataTime()), 18-fetchSessions(), true);
        page4.setVals("Tap Accuracy When \"On Task\" and \"Off Task\":", createBarChart(0, fetchBarDataTime(0),
                fetchBarDataTime(1)), 24-fetchSessions(), false);
        page5.setVals("Avg. Response Time When \"On Task\" and \"Off Task\":", createBarChart(1, fetchBarDataAcc(0),
                fetchBarDataAcc(1)), Integer.MAX_VALUE, false);
    }

    /**
     * A simple pager adapter that returns 5 ScreenSlidePageFragment objects, in
     * sequence. Created from Google documentation example code.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return page1;
                case 1:
                    return page2;
                case 2:
                    return page3;
                case 3:
                    return page4;
                case 4:
                    return page5;
            }
            return new FeedbackFragmentText();
        }

        @Override
        public int getCount() {
            return chartsUnlocked;
        }
    }


    /**
     * Sets typefaces to Futura.
     */
   private void setTypefaces() {
       text = new ArrayList<>();
       text.add((TextView) findViewById(R.id.FeedbackTitle));

       Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/FuturaLT.ttf");

       for (TextView v : text) {
           v.setTypeface(tf);
       }
   }

    /**
     * Creates line chart from data and returns LineData object.
     * @param type 0 if accuracy plot, 1 if response time plot
     */
    public LineData createLineChart(int type, List<Float> data){

        List<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        for (int i=0; i<data.size(); i++) {
            entries.add(new Entry(data.get(i), i));
            labels.add(Integer.toString(i));
        }
        LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setColor(Color.RED);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData lineData = new LineData(labels, dataSet);
        return lineData;
    }

    /**
     * Creates bar chart from data and returns BarData object.
     * @param type 1 if accuracy plot, 0 if response time plot
     */
    public BarData createBarChart(int type, float dataOn, float dataOff){
        List<BarEntry> entries = new ArrayList<BarEntry>();
        entries.add(new BarEntry(dataOff, 0));
        entries.add(new BarEntry(dataOn, 1));
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Off Task");
        labels.add("On Task");
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(Color.RED);
        BarData barData = new BarData(labels, dataSet);
        return barData;
    }

    /**
     * Fetches number of sessions that the player has completed
     */
    public int fetchSessions(){
       return data.getTotalGames();
    }

    public float fetchResponse(){
        float response = data.getLatestNAverageResponses(1).get(0);
        return response;
    }

    public float fetchAccuracy(){
        float accuracy = data.getLatestNTaskCorrectness(1).get(0);
        return accuracy;
    }

    public List<Float> fetchLineDataAcc(){
        return data.getLatestNTaskCorrectness(6);
    }

    public List<Float> fetchLineDataTime(){
        return data.getLatestNAverageResponses(6);
    }

    public float fetchBarDataTime(int task){
        ArrayList<Float> values;
        float avg = 0;
        if (task==0){
            values = data.getOnTaskCorrectnesses();
            for (float v: values) {
                avg += v;
            }
            return avg/values.size();
        } else {
            values = data.getOffTaskCorrectnesses();
            for (float v: values) {
                avg += v;
            }
            return avg/values.size();
        }
    }

    public float fetchBarDataAcc(int task){
        ArrayList<Float> values;
        float avg = 0;
        if (task==0){
            values = data.getOnTaskResponses();
            for (float v: values) {
                avg += v;
            }
            return avg/values.size();
        } else {
            values = data.getOffTaskResponses();
            for (float v: values) {
                avg += v;
            }
            return avg/values.size();
        }
    }
}
