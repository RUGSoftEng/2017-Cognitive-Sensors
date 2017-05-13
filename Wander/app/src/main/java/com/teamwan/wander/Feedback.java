package com.teamwan.wander;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Feedback extends AppCompatActivity {

    private ArrayList<TextView> text;
    int chartsUnlocked = 5;

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

        setTypefaces();

        mPager = (ViewPager) findViewById(R.id.FeedbackContent);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        initialiseFragments();
        getData();
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
     */
//    public Bitmap createLineChart(){
//
//    }

    /**
     * todo: Creates bar chart from data and returns bitmap.
     */
//    public Bitmap createBarChart(){
//
//    }

    /**
     * todo: Fetches required data. Must set chartsUnlocked to at least one.
     * If the corresponding charts are unlocked, these relevant charts should
     * be created with the above methods.
     */
    public void getData(){

        //use setVals to set feedback for each page <= chartsUnlocked
        //eg: page1.setVals(0.50,90,5);
    }
}
