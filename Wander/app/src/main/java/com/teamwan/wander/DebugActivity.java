/*
Ashton Spina
07/03/2017

Tap the mainmenu copyright image 3 time to access this debug menu
for testing and demoing features
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

    public void onClickNotify(View v){
        sendNotification();
    }

    public void onClickMultiChoice(View v) {
        Intent intent = new Intent(DebugActivity.this, InGameMultiQuestion.class);
        DebugActivity.this.startActivity(intent);
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    public void onClickDebugBack(View v){
        finish();
    }
    //TODO:: create tests for the other buttons

    public void sendNotification(){
        Intent alarmIntent = new Intent(this, MyReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 10, pendingIntent);
    }
}

