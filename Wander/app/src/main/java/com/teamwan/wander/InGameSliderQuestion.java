/**
 *  This class is a class for questions to be asked and
 *  will be popped up to ask questions
 *
 * @author  Ashton Spina
 * @version 1.1
 * @since   2017-05-15
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

import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.Question;

import java.util.ArrayList;

/**
 * Created by Ashton Spina on 20-Mar-17.
 */
//TODO:: we need an abstract class for this and multi question
public class InGameSliderQuestion extends AppCompatActivity {

    private TextView questionDisplay;
    private int questionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_question_layout);

        questionDisplay = (TextView)findViewById(R.id.textViewForSliderQuestion);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            questionID = extras.getInt("questionID");
            ArrayList<Question> questionList = (new DBHelper(this).getQuestions());
            questionDisplay.setText(questionList.get(questionID).getQuestion());
        }
        else{
            questionDisplay.setText("No Question to Display");
        }
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

    /**
     * Sends user back to main menu when quit button is clicked
     */
    public void onClickQuit(View v){
        setResult(Activity.RESULT_OK, new Intent().putExtra("choice", -1));
        finish();
    }
}