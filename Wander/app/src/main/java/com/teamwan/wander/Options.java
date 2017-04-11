/**
 *  This class is the main activity for the application.
 *  It handles the main menu and starts other activities
 *  to actually play the game.
 *
 * @author  Jake Davison
 * @version 1.0
 * @since   2017-03-20
 */

package com.teamwan.wander;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Options extends AppCompatActivity {
    /**
     * Auto generated system variables
     */
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    /**
     * Holds all text views to allow for more efficient typeface setting.
     */
    private ArrayList<TextView> text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_options);

        mVisible = true;

        initialiseConsentButton();
        setTypefaces();
    }

    @Override
    /**
    * Trigger the initial hide() shortly after the activity has been
    * created, to briefly hint to the user that UI controls are available.
    */
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible)
            hide();
        else
            show();
    }
    /**
     * This first hides the UI then
     * schedules a runnable to remove the status and navigation bar after a default delay time
     */
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        mVisible = false;
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    /**
     * This first shows the system bar then
     * Schedules a runnable to display UI elements after a delay
     */
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }
    /**
     * This first hides the UI then
     * schedules a runnable to remove the status and navigation bar after a specified delay
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    /**
     *  Sets custom typefaces for all text.
     */
    private void setTypefaces() {
        text = new ArrayList<TextView>();
        text.add((TextView) findViewById(R.id.OptionsTitle));
        text.add((TextView) findViewById(R.id.NotifFreqHead));
        text.add((TextView) findViewById(R.id.Never));
        text.add((TextView) findViewById(R.id.Daily));
        text.add((TextView) findViewById(R.id.Weekly));
        text.add((TextView) findViewById(R.id.ConsentHead));
        text.add((TextView) findViewById(R.id.ConsentButton));
        text.add((TextView) findViewById(R.id.NotifSave));

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/FuturaLT.ttf");

        for (TextView v : text) {
            v.setTypeface(tf);
        }

    }

    /**
     * Initialises the consent button to display correct message depending on current consent status.
     */
    private void initialiseConsentButton() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        TextView consentButton = (TextView) findViewById(R.id.ConsentButton);

        if (!sharedPref.getBoolean("Consent?", false)) {
            consentButton.setText(R.string.ConsentNotGiven);
            consentButton.setClickable(false);
        } else {
            consentButton.setText(R.string.TapToRevoke);
        }
    }

    /**
     *  Method for revoking consent when button is clicked
     */
    public void onClickRevoke(View v) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        TextView consentButton = (TextView) findViewById(R.id.ConsentButton);
        editor.putBoolean("Consent?", false);
        editor.commit();
        consentButton.setText(R.string.ConsentRevoked);
        consentButton.setTextColor(getResources().getColor(R.color.positiveResult));
        consentButton.setClickable(false);
    }

    /**
     * Saves notifications preference and changes text to give feedback through UI.
     */
    public void onClickNotifSave(View v) {
        final TextView save = (TextView) findViewById(R.id.NotifSave);
        SeekBar sb = (SeekBar) findViewById(R.id.NotifSlider);
        switch (sb.getProgress()) {
            case 0:
                //todo: turn notifications off
                break;
            case 1:
                //todo: set daily notifcations
                break;
            case 2:
                //todo: set weekly notifications
        }
        save.setText(R.string.SAVED);
        save.setTextColor(getResources().getColor(R.color.positiveResult));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                save.setText(R.string.SAVE);
                save.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }, 1500);
    }
}
