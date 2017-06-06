/**
 *  Tapping the copyright button in the main menu
 *  3 times opens this menu to handle debugging.
 *
 * @author  Ashton Spina
 * @version 1.0
 * @since   2017-03-07
 */

package com.teamwan.wander;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
    }
    /**
     *  This method and the one below sends a notification to test notification function.
     */
    public void onClickNotify(View v){
        sendNotification();
    }

    private void sendNotification(){
        Intent alarmIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 10, pendingIntent);
    }
    /**
     * Launches a slider question activity
     */
    public void onClickSlider(View v) {
        Intent intent = new Intent(DebugActivity.this, InGameSliderQuestion.class);
        DebugActivity.this.startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }
    /**
     * Launches a multiple choice question activity
     */
    public void onClickFeedback(View v) {
        Intent intent = new Intent(DebugActivity.this, Feedback.class);
        DebugActivity.this.startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    /**
     * This method returns the activity to the main menu activity
     * on click of the back button.
     */
    public void onClickDebugBack(View v){
        finish();
    }

    //TODO:: create tests for the other buttons

}

