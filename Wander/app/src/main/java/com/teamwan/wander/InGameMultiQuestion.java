/**
 *  This class is a class for questions to be asked and
 *  will be popped up to ask questions
 *
 * @author  Ashton Spina
 * @version 1.0
 * @since   2017-03-20
 */

package com.teamwan.wander;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
//TODO:: we need an abstract class for this and slider question
public class InGameMultiQuestion extends AppCompatActivity {

    private TextView questionDisplay;
    private int questionID;
    private ArrayList<CheckBox> checkBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_choice_question_layout);
        TextView questionDisplay = (TextView)findViewById(R.id.textViewForQuestion);
        questionID = -1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            questionID = extras.getInt("questionID");
            String[] questions = getResources().getStringArray(R.array.Questions);
            questionDisplay.setText(questions[questionID]);
        }
        else{
            questionDisplay.setText("No Question to Display");
        }
        populateCheckBoxes();
        initialiseTypefaces();
    }

    /**
     * Sets a custom font for text fields.
     */
    public void initialiseTypefaces() {

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/FuturaLT.ttf");

        TextView questionDisplay = (TextView)findViewById(R.id.textViewForQuestion);
        TextView confirm = (TextView)findViewById(R.id.ConfirmMultiChoice);

        confirm.setTypeface(tf);
        questionDisplay.setTypeface(tf);

        for (CheckBox cb : checkBoxes)
            cb.setTypeface(tf);
    }

    /**
     * Adds all checkboxes to array and sets the answer texts
     */
    public void populateCheckBoxes() {

        checkBoxes = new ArrayList<CheckBox>();

        int NUMBER_OF_QUESTIONS = 5;

        if (questionID == 0) {
            NUMBER_OF_QUESTIONS = 6;
        }
        String[] answers = getResources().getStringArray(getResources().getIdentifier("Q" + Integer.toString(questionID+1) + "Answers", "array", getPackageName()));
        for(int i = 1; i <= NUMBER_OF_QUESTIONS; i++)
        {
            CheckBox cb = (CheckBox) findViewById(getResources().getIdentifier(("checkBox" + Integer.toString(i)), "id", getPackageName()));
            cb.setText(answers[i-1]);
            checkBoxes.add(cb);
        }

        for(int i = NUMBER_OF_QUESTIONS + 1; i <= 6; i++) {
            CheckBox cb = (CheckBox) findViewById(getResources().getIdentifier(("checkBox" + Integer.toString(i)), "id", getPackageName()));
            cb.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Toggles any other checkboxes off when any checkbox is checked.
     */
    public void onClickCheckBox (View v) {
        for (CheckBox cb : checkBoxes) {
            if (!cb.equals((CheckBox) v) && cb.isChecked())
                cb.toggle();
        }
    }

    /**
     * Finds which answer is selected and saves this answer.
     */
    public void onClickConfirm(View v) {
        int i = 0;
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                setResult(Activity.RESULT_OK, new Intent().putExtra("choice", i));
                finish();
                break;
            }
            ++i;
        }
    }
    /**
     * Sends user back to main menu when quit button is clicked
     */
    public void onClickQuit(View v){
        setResult(Activity.RESULT_OK, new Intent().putExtra("choice", -1));
        finish();
    }
}
