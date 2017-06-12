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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

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

    @Override
    /**
     * When the activity is created the layout is setup,
     * the buttons and typefaces are initialised,
     * and the seekbar is set to show progress == to its current setting
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_options);

        mVisible = true;

        initialiseConsentButton();
        setTypefaces();

        ((SeekBar)findViewById(R.id.NotifSlider)).setProgress(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt("noteSetting", 1));
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
        /*
      Holds all text views to allow for more efficient typeface setting.
     */
        ArrayList<TextView> text = new ArrayList<>();
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
        }
        else {
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
        consentButton.setTextColor(ContextCompat.getColor(this, R.color.positiveResult));
        consentButton.setClickable(false);
    }

    /**
     * Saves notifications preference and changes text to give feedback through UI.
     * TODO:: do proper testing of this, it currently changes the sharedPrefs, but we don't know if it actually sets a weekly alarm, because it hasn't been a week
     */
    public void onClickNotifSave(View v) {
        final TextView save = (TextView) findViewById(R.id.NotifSave);
        SeekBar sb = (SeekBar) findViewById(R.id.NotifSlider);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt("noteSetting", sb.getProgress());

        //TODO:: if we want to make time of day dynamic, we can change this
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(this.getApplicationContext(), MyReceiver.class);
        System.out.println("Found pending intent already scheduled? :  " + checkIfPendingIntentIsRegistered(intent));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        try {
            alarmManager.cancel(pendingIntent);
            System.out.println("Successfully cancelled alarm for" + pendingIntent.toString());
        } catch (Exception e) {
            System.out.println("AlarmManager update was not canceled. " + e.toString());
        }

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        switch (sb.getProgress()) {
            case 1:
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                break;
            case 2:
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                break;
            default:
                break;
        }

        editor.commit();
        save.setText(R.string.SAVED);
        save.setTextColor(ContextCompat.getColor(this, R.color.positiveResult));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                save.setText(R.string.SAVE);
                save.setTextColor(ContextCompat.getColor(Options.this, R.color.colorPrimaryDark));
            }
        }, 1500);
    }
    private boolean checkIfPendingIntentIsRegistered(Intent toTest) {
        // Build the exact same pending intent you want to check.
        // Everything has to match except extras.
        return (PendingIntent.getBroadcast(this.getApplicationContext(), 0, toTest, PendingIntent.FLAG_NO_CREATE) != null);
    }
}
