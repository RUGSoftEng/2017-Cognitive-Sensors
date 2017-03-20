/*
Ashton Spina
20/03/2017

This class is a class for questions to be asked and
will be popped up to ask questions
 */
package com.teamwan.wander;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by Ashton Spina on 20-Mar-17.
 */
public class InGameSliderQuestion extends AppCompatActivity {

    private TextView questionDisplay; //where the number is shown

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_question_layout);
        int questionID = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            questionID = extras.getInt("questionID");
            //The key argument here must match that used in the other activity
        }

        //This uses a custom typeface for the number displayed
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/FuturaLT.ttf");
        //TextView questionDisplay = (TextView)findViewById(R.id.questionDisplayMulti);
        //questionDisplay.setTypeface(tf);

        //TODO:: get question at questionID index and place it in the textview
        //TODO:: display appropriate responses for that question ID
        //TODO:: return answer to the numberGame and save it
    }
}