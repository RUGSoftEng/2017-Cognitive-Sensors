/*
Ashton Spina
20/03/2017

This class is a class for questions to be asked and
will be popped up to ask questions
 */
package com.teamwan.wander;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Ashton Spina on 20-Mar-17.
 */
public class InGameSliderQuestion extends AppCompatActivity {

    private TextView questionDisplay; //where the number is shown
    private int questionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_question_layout);

        questionDisplay = (TextView)findViewById(R.id.textViewForSliderQuestion);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            questionID = extras.getInt("questionID");
            String[] questions = getResources().getStringArray(R.array.Questions);
            questionDisplay.setText(questions[questionID]);
        }
        else{
            questionDisplay.setText("No Question to Display");
        }
        //This uses a custom typeface for the number displayed
        initialiseTypefaces();
    }


    /**
     * Sets a custom font for text fields.
     */
    public void initialiseTypefaces() {

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/FuturaLT.ttf");

        TextView questionDisplay = (TextView)findViewById(R.id.textViewForSliderQuestion);
        TextView confirm = (TextView)findViewById(R.id.ConfirmSlider);
        TextView pos = (TextView)findViewById(R.id.Positive);
        TextView neg = (TextView)findViewById(R.id.Negative);

        confirm.setTypeface(tf);
        questionDisplay.setTypeface(tf);
        pos.setTypeface(tf);
        neg.setTypeface(tf);
    }

    /**
     * Finds which answer is selected and saves this answer.
     */
    public void onClickConfirmSlider(View v) {

        SeekBar slider = (SeekBar) findViewById(R.id.SliderSeekBar);
        setResult(Activity.RESULT_OK, new Intent().putExtra("choice", slider.getProgress()));
        finish();
    }

    //sends user back to main menu when quit button is clicked
    public void onClickQuit(View v){
        setResult(Activity.RESULT_OK, new Intent().putExtra("choice", -1));
        finish();
    }
}