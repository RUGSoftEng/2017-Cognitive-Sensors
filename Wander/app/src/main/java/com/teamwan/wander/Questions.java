package com.teamwan.wander;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.teamwan.wander.db.GameSession;

import java.util.ArrayList;

public class Questions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        setTypefaces();
        checkConsent();
    }

    /**
     * Checks that the user has given informed consent before allowing interaction with buttons.
     */
    private void checkConsent() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        System.out.println(sharedPref.getBoolean("Consent?", false));
        if(!sharedPref.getBoolean("Consent?", false)) {
            findViewById(R.id.UserIDButton).setVisibility(View.INVISIBLE);
            ((TextView) findViewById(R.id.QuestionsButton)).setText("Please give informed consent to gain access.");
            findViewById(R.id.QuestionsButton).setClickable(false);
        }
    }

    /**
     * Copies the UserID to clipboard.
     */
    public void onClickUserIDButton(View v) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("PREF_UNIQUE_ID", Context.MODE_PRIVATE);
        String id = GameSession.getUniqueID(this).hashCode() + "";
        if (id!=null) {
            ClipData clip = ClipData.newPlainText("ID", id);
            clipboard.setPrimaryClip(clip);
            ((TextView) v).setText("UserID copied to clipboard!");
            ((TextView) v).setTextColor(getResources().getColor(R.color.positiveResult));
            TextView qv = (TextView) findViewById(R.id.QuestionsButton);
            qv.setText(Html.fromHtml("<a href=https://docs.google.com/forms/d/e/1FAIpQLScz5KC_5MxabFJJeRnix-WK3RruY80mStcNwOukQflgxKEpVA/viewform?usp=sf_link>Tap here to go to questionnaire."));
            qv.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            ((TextView) v).setText("No user ID found, complete a game session to recieve an ID.");
            ((TextView) v).setTextColor(getResources().getColor(R.color.negativeResult));
        }

    }

    /**
     * Sets TextView typefaces to Futura.
     */
    private void setTypefaces() {
        ArrayList<TextView> text = new ArrayList<>();
        text.add((TextView) findViewById(R.id.QuestionsTitle));
        text.add((TextView) findViewById(R.id.QuestionsText));
        text.add((TextView) findViewById(R.id.UserIDButton));
        text.add((TextView) findViewById(R.id.QuestionsButton));
        text.add((TextView) findViewById(R.id.QuestionsBack));
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/FuturaLT.ttf");

        for (TextView v : text) {
            v.setTypeface(tf);
        }
    }

    /**
     * Closes the activity when "BACK" TextView is clicked.
     */
    public void onClickBack(View v) { finish(); }

}
