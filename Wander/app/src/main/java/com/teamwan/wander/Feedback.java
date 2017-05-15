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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
        //TODO:: make the graph data not crash
        //data = new GraphData();

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
        page4 = new FeedbackFragmentChart();//TODO:: make page 4 not crash
        page5 = new FeedbackFragmentChart();
        page1.setVals(fetchResponse(), fetchAccuracy(), 6-fetchSessions());
        page2.setVals("Last 6 Sessions", createLineChart(0), 12-fetchSessions());
        page3.setVals("Last 12 Sessions", createLineChart(1), 18-fetchSessions());
        page4.setVals("Tap Accuracy When \"On Task\" and \"Off Task\":", createBarChart(0), 24-fetchSessions());
        page5.setVals("Avg. Response Time When \"On Task\" and \"Off Task\":", createBarChart(1), Integer.MAX_VALUE);
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
     * todo: Creates line chart from data and returns bitmap.
     * @param type 0 if accuracy plot, 1 if response time plot
     */
    public LineData createLineChart(int type){
        List<Entry> entries = new ArrayList<Entry>();

        // turn data into Entry objects

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        dataSet.setColor(Color.RED);
        LineData lineData = new LineData();
        lineData.addDataSet(dataSet);
        return lineData;
    }

    /**
     * todo: Creates bar chart from data and returns bitmap.
     * @param type 1 if accuracy plot, 0 if response time plot
     */
    public BarData createBarChart(int type){
        return new BarData();
    }

    /**
     * todo: Fetches number of sessions that the player has completed
     */
    public int fetchSessions(){
        //int sessions = data.getTotalGames();
        int sessions = 24;
        //get total number of sessions here
        return sessions;
    }

    public double fetchResponse(){
        //double response = data.getLatestART();
        double response = 1.25;
        //get avg. response time for the session here
        return response;
    }

    public int fetchAccuracy(){
        //int accuracy = (int)data.getLatestCTP();
        int accuracy = 95;
        //get tap accuracy here
        return accuracy;
    }
}
