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
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teamwan.wander.db.DBDownload;
import com.teamwan.wander.db.DBpars;

import java.util.ArrayList;
import java.util.Calendar;

public class MainMenu extends AppCompatActivity {

    private int countClicks;
    private MenuLayoutComponents mlc;

    /**
     * This method is the constructor for the game.
     * It checks that a consent agreement has been agree to
     * and that the alarm has been created to send notifications.
     * It also has a countClicks variable to enable deBug.  Appearances
     * are set here.
     *
     * The DBDownload ensures the questions are updated if possible when the game is started
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.animator.fade_in, 0);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        countClicks = 0;

        new DBDownload().execute(new DBpars(this));

        mlc = new MenuLayoutComponents();
        initialiseMLC();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        System.out.println(sharedPref.getBoolean("Consent?", false));
        if(!sharedPref.getBoolean("Consent?", false))
            toggleICA();

        setUpNotifications(sharedPref.getInt("noteSetting", -1));
        System.out.println(sharedPref.getBoolean("Consent?", false));

        TextView descriptionBox1=(TextView)findViewById(R.id.DescriptionBox1);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/FuturaLT.ttf");
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
        if(countClicks >= 3){
            Intent intent = new Intent(MainMenu.this, DebugActivity.class);
            MainMenu.this.startActivity(intent);
            countClicks = 0;
        }
    }

    /**
     * This method opens an options menu for tweaking settings in the application.
     */
    public void onClickOptions(View v){
        Intent intent = new Intent(MainMenu.this, Options.class);
        MainMenu.this.startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    /**
     * This methods opens the questions activity.
     */
    public void onClickQuestions(View v){
        Intent intent = new Intent(MainMenu.this, Questions.class);
        MainMenu.this.startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    /**
     * Toggles info overlay.
     */
    public void onClickInfo(View v){
        toggleInfo();
    }

    /**
     * This method sets a daily alarm for a certain time
     * //TODO:: right now we use a default, we might wanna change this if we ever give optional times
     */
    private void setUpNotifications(int currentSetting) {
        if(currentSetting == -1){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 30);
            calendar.set(Calendar.SECOND, 0);

            Intent alarmIntent = new Intent(this.getApplicationContext(), MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    /**
     * Method for accepting/rejecting ICA. Sets consent true or false depending on view clicked.
     */
    public void onClickICAAcceptReject(View v) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        toggleICA();

        if (v.getId() == R.id.ICAAccept)
            editor.putBoolean("Consent?", true);
        else
            editor.putBoolean("Consent?", false);
        editor.commit();
    }

    /**
     *  Method for passing view objects for overlay from layout to menu layout components object.
     */
    private void initialiseMLC() {
        mlc.overlay = (LinearLayout) findViewById(R.id.Overlay);
        mlc.contentBox = (LinearLayout) findViewById(R.id.ContentBox);
        mlc.title = (TextView) findViewById(R.id.OverlayTitle);
        mlc.body = (TextView) findViewById(R.id.ICABody);
        mlc.accept = (TextView) findViewById(R.id.ICAAccept);
        mlc.reject = (TextView) findViewById(R.id.ICAReject);
        mlc.close = (TextView) findViewById(R.id.InfoClose);
        mlc.futura = Typeface.createFromAsset(getAssets(),"fonts/FuturaLT.ttf");
        mlc.params = (LinearLayout.LayoutParams) mlc.body.getLayoutParams();
        mlc.body.setMovementMethod(new ScrollingMovementMethod());
        mlc.text = new ArrayList<>();
        mlc.text.add(mlc.title);
        mlc.text.add(mlc.body);
        mlc.text.add(mlc.accept);
        mlc.text.add(mlc.reject);
        mlc.text.add(mlc.close);
        setTypefaces();
    }

    /**
     * Sets all overlay text to Futura
     */
    private void setTypefaces() {
        for (TextView v : mlc.text)
            v.setTypeface(mlc.futura);
    }

    /**
     * Toggles the overlay and content box visble or invisible.
     */
    private void toggleOverlay() {
        mlc.overlayVis = (mlc.overlayVis==View.VISIBLE) ? (View.INVISIBLE) : (View.VISIBLE);
        mlc.overlay.setVisibility(mlc.overlayVis);
        mlc.contentBox.setVisibility(mlc.overlayVis);
    }

    /**
     * Toggles the info overlay visble or invisible.
     */
    private void toggleInfo() {
        toggleOverlay();
        mlc.infoVis = (mlc.infoVis==View.VISIBLE) ? (View.INVISIBLE) : (View.VISIBLE);
        mlc.title.setVisibility(mlc.infoVis);
        mlc.body.setVisibility(mlc.infoVis);
        mlc.close.setVisibility(mlc.infoVis);
    }

    /**
     * Initialises info components after ICA has been closed.
     * //TODO:: explain these "magic numbers"
     */
    private void initialiseInfo() {
        mlc.params.height = (int) ( 370 * getResources().getDisplayMetrics().density);
        mlc.body.setLayoutParams(mlc.params);
        mlc.title.setText(R.string.InfoTitle);
        mlc.body.setText(R.string.InfoBody);
        mlc.close.setText(R.string.InfoClose);
        ((ViewGroup) mlc.accept.getParent()).removeView(mlc.accept);
        ((ViewGroup) mlc.reject.getParent()).removeView(mlc.reject);
    }

    /**
     * Toggles Informed Consent Agreement visible or invisible.
     */
    private void toggleICA() {
        int vis = (mlc.overlay.getVisibility()==View.VISIBLE) ? (View.INVISIBLE) : (View.VISIBLE);
        mlc.overlay.setVisibility(vis);
        mlc.contentBox.setVisibility(vis);
        mlc.title.setVisibility(vis);
        mlc.body.setVisibility(vis);
        mlc.accept.setVisibility(vis);
        mlc.reject.setVisibility(vis);

        if (vis==View.INVISIBLE)
            initialiseInfo();
    }

}
