/*
Ashton Spina
07/03/2017

This class handles the basic number game
generating when clicked and ends the game
after gameLength seconds.
 */

package com.teamwan.wander;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import static android.R.attr.textColor;
import static java.lang.Math.abs;

public class NumberGame extends AppCompatActivity {

    private TextView numberDisplay; //where the number is shown
    private Random rn;//random number generator
    private long startTime;//game start time
    private long timeLastClicked;//last time the number was clicked
    private final long gameLength = 30 * 1000; //game duration in seconds for testing

    private final int unClickableNum = 3;//number which should not be clicked
    private int currentNum;//current displayed number
    private int successValue;//state of game represented as -1 | 0 | 1 == failure |  nothing | success

    private int successCounter;
    private int failCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);

        runGame();//setup the game values

        final Handler handle = new Handler();
        final long delay = 650;
        //This handler checks if the number has been successfully interacted with each delay period
        handle.postDelayed(new Runnable(){
            public void run(){
                if(System.currentTimeMillis() - timeLastClicked > delay)
                    checkSuccess();

                handle.postDelayed(this, delay);
            }
        }, delay);
    }
    //This function initializes the objects needed to run the game
    protected void runGame(){
        startTime = System.currentTimeMillis();
        timeLastClicked = System.currentTimeMillis();

        successCounter = 0;
        failCounter = 0;

        numberDisplay = (TextView) findViewById(R.id.numberDisplay);
        rn = new Random();
        genNewNumber();

    }

    //Puts a new integer in the textView when the TextView is clicked
    public void onClickNumber(View v){
        if(currentNum == unClickableNum)
            failOnNum();
        else
            successOnNum();
        timeLastClicked = System.currentTimeMillis();
    }
    //actions taken if the player does things wrong
    private void failOnNum(){
        numberDisplay.setTextColor(getResources().getColor(R.color.negativeResult));
        successValue = -1;
        ++failCounter;
    }
    //actions taken if the player does things successfully
    private void successOnNum(){
        ++successCounter;
        numberDisplay.setTextColor(getResources().getColor(R.color.positiveResult));
        successValue = 1;
        //finish activity after gameLength seconds and return to mainMenu which is on top of the activity stack
        if(System.currentTimeMillis() - startTime > gameLength)
            finish();
    }

    private void checkSuccess(){
        if(successValue != 0){//if something has happened
            if (successValue == 1)//if previous action was successful
                genNewNumber();
            else//This else lets the delay last a little longer so they can experience their failure.
                successValue = 1;
        }
        else{
            if(currentNum == unClickableNum && System.currentTimeMillis() - timeLastClicked > 2000)
                successOnNum();//
            // if the number is the one that should not be clicked and they've not touched it for 2 seconds they are successful time can be changed though
        }
    }
    //generates a new number and resets the text appearance
    private void genNewNumber(){
        long hold = currentNum;
        while(hold == currentNum)//ensure new value is different from old one.
            currentNum = abs(rn.nextInt() % 9);
        numberDisplay.setText(Integer.toString(currentNum));//set text in textView as the generated integer
        numberDisplay.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        successValue = 0;
    }
}
