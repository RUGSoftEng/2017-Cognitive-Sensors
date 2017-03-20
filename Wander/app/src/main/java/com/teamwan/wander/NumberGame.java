/*
Ashton Spina
07/03/2017

This class handles the basic number game
generating when clicked and ends the game
after gameLength seconds.
 */

package com.teamwan.wander;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class NumberGame extends AppCompatActivity {

    private TextView numberDisplay; //where the number is shown
    private Random rn;//random number generator
    private long startTime;//game start time
    private long gameLength; //game duration in seconds for testing
    private int amountOfQuestions;
    private final int unClickableNum = 3;//number which should not be clicked
    private int currentNum;//current displayed number
    private int successValue;//state of game represented as -1 | 0 | 1 == failure |  nothing | success
    private int questionID;
    private ArrayList<QuestionInfo> questionIntervals;

    private int successCounter;
    private int failCounter;
    private long timeLastClicked;//last time the number was clicked
    private long timeNumberDisplayed;//Time last number was displayed
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);

        //This uses a custom typeface for the number displayed
        TextView numberDisplay=(TextView)findViewById(R.id.numberDisplay);
        Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FuturaLT.ttf");
        numberDisplay.setTypeface(tf);

        rl = (RelativeLayout)findViewById(R.id.gameUI);
        questionID = 0;
        gameLength = (getResources().getInteger(R.integer.game_length)) * 1000;
        amountOfQuestions = getResources().getInteger(R.integer.amount_questions);
        final long delay = (getResources().getInteger(R.integer.number_display)) * 1000;
        final long jitter = (getResources().getInteger(R.integer.jitter_range)) * 100;

        runGame();//setup the game values

        //This handler checks if the number has been successfully interacted with each delay period
        final Handler handle = new Handler();
        handle.postDelayed(new Runnable(){
            public void run(){
                if(System.currentTimeMillis() - timeLastClicked > delay)
                    checkSuccess();
                handle.postDelayed(this, delay + ((rn.nextInt()) % jitter));//adds a jitter in the next time check of a random value + or - jitter
            }
        }, delay);
    }

    //This function initializes the objects needed to run the game
    protected void runGame(){
        startTime = System.currentTimeMillis();
        timeLastClicked = System.currentTimeMillis();

        successCounter = 0;
        failCounter = 0;

        questionIntervals = new ArrayList<QuestionInfo>();
        questionIntervals.add(new QuestionInfo(0, 2));//TODO:: add all questions here using this example

        numberDisplay = (TextView) findViewById(R.id.numberDisplay);
        rn = new Random();
        genNewNumber();
    }

    //Puts a new integer in the textView when the TextView is clicked
    public void onClickNumber(View v){
        if(successValue == 0) {
            if (currentNum == unClickableNum)
                failOnNum();
            else
                successOnNum();
            timeLastClicked = System.currentTimeMillis();
        }
    }

    //actions taken if the player does things wrong
    private void failOnNum(){
        rl.setBackgroundColor(getResources().getColor(R.color.negativeResult));
        successValue = 1;
        ++failCounter;
        System.out.println("fail");
        numberDisplay.setText("");
    }

    //actions taken if the player does things successfully
    private void successOnNum(){
        rl.setBackgroundColor(getResources().getColor(R.color.positiveResult));
        successValue = 1;
        ++successCounter;
        System.out.println("success");
        numberDisplay.setText("");

        if(System.currentTimeMillis() - startTime > gameLength) {
            finish();
            overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        }
    }

    private void checkSuccess(){
        // if the number is the one that should not be clicked and they've not touched it for 3 seconds they are successful time can be changed to a set value
        if(currentNum == unClickableNum && successValue == 0){
            System.out.println("success in check");
            ++successCounter;
        }

        //number was not clicked when it should have been so it is a failure
        else if(currentNum == unClickableNum && successValue == 1){
            System.out.println("fail in check");
            ++failCounter;
        }

        //after a certain amount of numbers ask a question
        System.out.println(successCounter + failCounter);
        if(questionID < questionIntervals.size() && (successCounter + failCounter) >= questionIntervals.get(questionID).getQuestionOrder()){
            Intent intent;
            if(questionIntervals.get(questionID).getQuestionType() == 1)
                intent = new Intent(getApplicationContext(), InGameSliderQuestion.class);
            else
                intent = new Intent(getApplicationContext(), InGameMultiQuestion.class);

            intent.putExtra("questionID", questionID);
            startActivityForResult(intent, 1);
            ++questionID;
            //TODO::pause current activity
        }
        else{
            genNewNumber();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        genNewNumber();
    }

    //generates a new number and resets the text appearance
    public void genNewNumber(){
        saveLastNumberData();

        rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        long hold = currentNum;
        while(hold == currentNum)//ensure new value is different from old one.
            currentNum = abs(rn.nextInt() % 9);
        numberDisplay.setText(Integer.toString(currentNum));//set text in textView as the generated integer
        numberDisplay.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        successValue = 0;
        timeNumberDisplayed = System.currentTimeMillis();
    }

    //sends user back to main menu when quit button is clicked
    public void onClickQuit(View v){
        finish();
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }

    private void saveLastNumberData(){
        boolean goodNum = (currentNum != unClickableNum);
        //TODO:: save needed values here
        /*
            These values are available and need to be stored for the session for each number clicked:
                -timeLastClicked == system time at last number click
                -timeNumberDisplayed == last time a number was displayed
                -successCounter == successful clicks
                -failCounter == unsuccessful clicks
                -goodNum == TRUE if last displayed number was a clickable one, else FALSE
         */
    }

}
