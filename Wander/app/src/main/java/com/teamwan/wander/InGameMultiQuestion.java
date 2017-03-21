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
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ashton Spina on 20-Mar-17.
 */
public class InGameMultiQuestion extends AppCompatActivity {

    private TextView questionDisplay; //where the number is shown
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
        //This uses a custom typeface for the number displayed
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

        for (CheckBox cb : checkBoxes){
            cb.setTypeface(tf);
        }
    }

    /**
     * Adds all checkboxes to array and sets the answer texts
     */
    public void populateCheckBoxes() {

        checkBoxes = new ArrayList<CheckBox>();

        CheckBox cb1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBoxes.add(cb1);
        CheckBox cb2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBoxes.add(cb2);
        CheckBox cb3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBoxes.add(cb3);
        CheckBox cb4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBoxes.add(cb4);
        CheckBox cb5 = (CheckBox) findViewById(R.id.checkBox5);
        checkBoxes.add(cb5);
        CheckBox cb6 = (CheckBox) findViewById(R.id.checkBox6);
        String[] answers;
        //TODO:: find a better way of checking which question it is and how many answers are required
        if(questionID != -1) {
            if (questionID == 0) {
                checkBoxes.add(cb6);
                answers = getResources().getStringArray(R.array.Q1Answers);
            } else {
                cb6.setVisibility(View.INVISIBLE);
                answers = getResources().getStringArray(R.array.Q2Answers);
            }
            int i = 0;
            for (CheckBox cb : checkBoxes) {
                cb.setText(answers[i]);
                ++i;
            }
        }
    }

    /**
     * Toggles any other checkboxes off when any checkbox is checked.
     */
    public void onClickCheckBox (View v) {
        for (CheckBox cb : checkBoxes) {
            if (!cb.equals((CheckBox) v) && cb.isChecked()) {
                cb.toggle();
            }
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
