/*
Ashton Spina
07/03/2017

This class handles the basic number game
generating when clicked and ends the game
after gameLength seconds.
 */

package com.teamwan.wander;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import static java.lang.Math.abs;

public class NumberGame extends AppCompatActivity {

    private TextView numberDisplay;
    private Random rn;
    private long startTime;
    private long gameLength = 10; //game duration in seconds for testing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);

        runGame();
    }
    //This function initializes the objects needed to run the game
    protected void runGame(){
        startTime = System.currentTimeMillis();
        numberDisplay = (TextView) findViewById(R.id.numberDisplay);
        rn = new Random();
        int i = rn.nextInt() % 9;
        numberDisplay.setText(Integer.toString(abs(i)));//set text in textView as the generated integer
    }
    //Puts a new integer in the textView when the TextView is clicked
    public void onClickNumber(View v){
        int i = rn.nextInt() % 9;
        numberDisplay.setText(Integer.toString(abs(i)));
        //finish activity after gameLength seconds and return to mainMenu which is on top of the activity stack
        if(System.currentTimeMillis() - startTime > gameLength * 1000)
            finish();
    }
}
