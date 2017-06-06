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

    private Question question;

    public static final String EXTRA_CHOICE = "choice";
    public static final String EXTRA_NEXT_QUESTION = "nextQuestion";
    public static final String EXTRA_QUESTION_ID = "questionId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_question_layout);

        TextView questionDisplay = (TextView) findViewById(R.id.textViewForSliderQuestion);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int questionID = extras.getInt(EXTRA_QUESTION_ID);
            ArrayList<Question> questionList = (new DBHelper(this).getQuestions());
            question = questionList.get(questionID);
            questionDisplay.setText(question.getQuestion());
        }
        else{
            questionDisplay.setText("No Question to Display");
        }
        initialiseTypefaces();
    }


    /**
     * Sets a custom font for text fields.
     */
    private void initialiseTypefaces() {

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

        Intent result = new Intent();
        result.putExtra(EXTRA_CHOICE, slider.getProgress());
        result.putExtra(EXTRA_NEXT_QUESTION, question.getNextQuestion().get(0).intValue());
        setResult(Activity.RESULT_OK, result);

        finish();
    }

    /**
     * Sends user back to main menu when quit button is clicked
     */
    public void onClickQuit(View v){
        Intent result = new Intent();
        result.putExtra(EXTRA_CHOICE, -1);
        result.putExtra(EXTRA_NEXT_QUESTION, -1);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}