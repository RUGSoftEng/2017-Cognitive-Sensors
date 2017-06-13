/**
 *  This class is a class for questions to be asked and
 *  will be popped up to ask questions
 *
 * @author  Ashton Spina
 * @version 1.1
 * @since   2017-05-15
 */

package com.teamwan.wander;

import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.Question;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

//TODO:: we need an abstract class for this and slider question
public class InGameMultiQuestion extends AppCompatActivity {

    private Question question;
    private ArrayList<CheckBox> checkBoxes;

    public static final String EXTRA_CHOICE = "choice";
    public static final String EXTRA_NEXT_QUESTION = "nextQuestion";
    public static final String EXTRA_QUESTION_ID = "questionID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_choice_question_layout);
        TextView questionDisplay = (TextView)findViewById(R.id.textViewForQuestion);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int questionID = extras.getInt(EXTRA_QUESTION_ID);
            ArrayList<Question> questionList = (new DBHelper(this).getQuestions());
            question = questionList.get(questionID);
            questionDisplay.setText(question.getQuestion());
        }
        else{
            questionDisplay.setText("No Question to Display, please report error");
        }
        populateCheckBoxes();
        initialiseTypefaces();
    }

    /**
     * Sets a custom font for text fields.
     */
    private void initialiseTypefaces() {

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
     * The checkboxes are populated with questions and answers from the database.
     */
    private void populateCheckBoxes() {

        checkBoxes = new ArrayList<>();
        int numberOfQuestions = question.getAnswers().size();

        for(int i = 1; i <= numberOfQuestions; ++i)
        {
            CheckBox cb = (CheckBox) findViewById(getResources().getIdentifier(("checkBox" + Integer.toString(i)), "id", getPackageName()));
            cb.setText(question.getAnswers().get(i - 1));
            checkBoxes.add(cb);
        }

        for(int i = numberOfQuestions + 1; i <= 6; i++) {
            CheckBox cb = (CheckBox) findViewById(getResources().getIdentifier(("checkBox" + Integer.toString(i)), "id", getPackageName()));
            cb.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Toggles any other checkboxes off when any checkbox is checked.
     */
    public void onClickCheckBox (View v) {
        for (CheckBox cb : checkBoxes) {
            if (!cb.equals(v) && cb.isChecked())
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
                Intent result = new Intent();
                result.putExtra(EXTRA_CHOICE, i);
                result.putExtra(EXTRA_NEXT_QUESTION, question.getNextQuestion().get(i).intValue());
                setResult(Activity.RESULT_OK, result);

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
        Intent result = new Intent();
        result.putExtra(EXTRA_CHOICE, -1);
        result.putExtra(EXTRA_NEXT_QUESTION, -1);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
