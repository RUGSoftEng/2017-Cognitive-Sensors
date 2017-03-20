/**
 *  This class is the main activity for the application.
 *  It handles the main menu and starts other activities
 *  to actually play the game.
 *
 * @author  Ashton Spina
 * @version 1.1
 * @since   2017-03-20
 */

package com.teamwan.wander;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainMenu extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private int countClicks;
    private PendingIntent pendingIntent;

    /**
     * This method is the constructor for the game.
     * It checks that a consent agreement has been agree to
     * and that the alarm has been created to send notifications.
     * It also has a countClicks variable to enable deBug.  Appearances
     * are set here.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.animator.fade_in, 0);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        countClicks = 0;

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(!sharedPref.getBoolean("Consent?", false)){
            initialiseICA();
            editor.putBoolean("Consent?", true);
        }
        if(!sharedPref.getBoolean("Setup?", false)){
            setUpNotifications();
            editor.putBoolean("Setup?", true);
        }
        editor.commit();

        TextView descriptionBox1=(TextView)findViewById(R.id.DescriptionBox1);
        Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FuturaLT.ttf");
        descriptionBox1.setTypeface(tf);
    }

    /**
     * Start a new activity on click of the Start Button
     */
    public void onClickStart(View v) {
        Intent intent = new Intent(MainMenu.this, NumberGame.class);
        MainMenu.this.startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        countClicks = 0;
    }
    /**
     * This is activated when the copyright imageview is clicked.
     * After three clicks the debug menu is opened for debugging
     * the application.
     */
    public void onClickDebug(View v){
        ++countClicks;
        if(countClicks >= 3)
        {
            Intent intent = new Intent(MainMenu.this, DebugActivity.class);
            MainMenu.this.startActivity(intent);
            countClicks = 0;
        }
    }
    /**
     * This method opens an options menu for tweaking settings in the application.
     */
    public void onClickOptions(View v){
        Intent intent = new Intent(MainMenu.this, OptionsActivity.class);
        MainMenu.this.startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    /**
     *
     */
    public void onClickInfo(View v){
        //TODO:: switch out info text for info text for the current game that has been selected to play
    }


    //TODO:: test that this actually works and de-hardcode it to work with the Options.
    /**
     * This method sets a daily alarm for a certain time
     */
    public void setUpNotifications(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 1);

        Intent alarmIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Method for displaying ICA overlay and content.
     */
    public void initialiseICA() {
        LinearLayout overlay = (LinearLayout)findViewById(R.id.ICAOverlay);
        LinearLayout contentBox = (LinearLayout)findViewById(R.id.ICAContentBox);

        TextView title = (TextView)findViewById(R.id.ICATitle);
        TextView body = (TextView)findViewById(R.id.ICABody);
        TextView accept = (TextView)findViewById(R.id.ICAAccept);
        TextView quit = (TextView)findViewById(R.id.ICAQuit);

        overlay.setVisibility(View.VISIBLE);
        contentBox.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        body.setVisibility(View.VISIBLE);
        accept.setVisibility(View.VISIBLE);
        quit.setVisibility(View.VISIBLE);

        Typeface tf =Typeface.createFromAsset(getAssets(),"fonts/FuturaLT.ttf");
        title.setTypeface(tf);
        body.setTypeface(tf);
        accept.setTypeface(tf);
        quit.setTypeface(tf);
    }

    /**
     * Method for accepting ICA and closing overlay.
     */
    public void onClickICAAccept(View v) {
        LinearLayout overlay = (LinearLayout)findViewById(R.id.ICAOverlay);
        LinearLayout contentBox = (LinearLayout)findViewById(R.id.ICAContentBox);

        TextView title = (TextView)findViewById(R.id.ICATitle);
        TextView body = (TextView)findViewById(R.id.ICABody);
        TextView accept = (TextView)findViewById(R.id.ICAAccept);
        TextView quit = (TextView)findViewById(R.id.ICAQuit);

        overlay.setVisibility(View.INVISIBLE);
        contentBox.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
        body.setVisibility(View.INVISIBLE);
        accept.setVisibility(View.INVISIBLE);
        quit.setVisibility(View.INVISIBLE);
        //TODO:: move the closing of the ICA to another function and set acceptance of ICA Based on button clicked.
    }

    /**
     * Method for closing app when quit selected at ICA overlay.
     * @param v
     */
    public void onClickICAQuit(View v) {
        finish();
    }
}
