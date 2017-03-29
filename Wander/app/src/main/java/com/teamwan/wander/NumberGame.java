/**
 *  This Class contains the the game which presents
 *  random digits to a user and has them click all but one
 *  digit.  The user is also asked questions
 *
 * @author  Ashton Spina
 * @version 1.1
 * @since   2017-03-20
 */

package com.teamwan.wander;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
//import android.icu.text.SimpleDateFormat;
//import android.icu.util.Calendar;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
import android.widget.Toast;

import com.teamwan.wander.db.DBUpload;
import com.teamwan.wander.db.DBpars;
import com.teamwan.wander.db.NumberGuess;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * This class contains the number game which displays random digits
 * and asks questions the the user.
 */
public class NumberGame extends AppCompatActivity {

    private TextView numberDisplay; //where the number is shown
    private Random rn;//random number generator
    private long startTime;//game start time
    private long gameLength; //game duration in seconds for testing
    private final int unClickableNum = 3;//number which should not be clicked
    private int currentNum;//current displayed number
    private int questionID;
    private ArrayList<QuestionInfo> questionIntervals;

    private int successCounter;
    private int failCounter;
    private long timeLastClicked; //last time the number was clicked
    private long timeNumberDisplayed; //Time last number was displayed
    private RelativeLayout rl;
    private GameState gameState;
    private boolean lastCorrect;
    private GameSession gs;

    public enum GameState {
        SUCCESS, NEUTRAL;
    }

    /**
     * This method is essentially the constructor for the game activity
     * It sets the appearances and initializes a Handler to check
     * the gameState every delay + random jitter milliseconds to ensure
     * the game isn't too predicatble.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);

        TextView numberDisplay=(TextView)findViewById(R.id.numberDisplay);
        Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/FuturaLT.ttf");
        numberDisplay.setTypeface(tf);

        runGame();

        final long delay = (getResources().getInteger(R.integer.number_display)) * 1000;
        final long jitter = (getResources().getInteger(R.integer.jitter_range)) * 100;

        gs = new GameSession(System.currentTimeMillis(),"numberGame");
        runGame();
      
        final Handler handle = new Handler();
        handle.postDelayed(new Runnable(){
            public void run(){
                    checkSuccess();
                handle.postDelayed(this, delay + ((rn.nextInt()) % jitter));
            }
        }, delay);
    }

    @Override
    protected void onDestroy(){
        saveGameSession();
        super.onDestroy();
    }

    /**
     * This method initializes values pertaining to the
     * game such that the game can run.
     * The purpose of this is simply organizational
     */
    protected void runGame(){
        rl = (RelativeLayout)findViewById(R.id.gameUI);
        questionID = 0;
        gameLength = (getResources().getInteger(R.integer.game_length)) * 1000;

        startTime = System.currentTimeMillis();
        timeLastClicked = System.currentTimeMillis();

        successCounter = 0;
        failCounter = 0;

        questionIntervals = new ArrayList<QuestionInfo>();
            questionIntervals.add(new QuestionInfo(0, 5));
            questionIntervals.add(new QuestionInfo(0, 10));
            questionIntervals.add(new QuestionInfo(1, 15));

        numberDisplay = (TextView) findViewById(R.id.numberDisplay);
        rn = new Random(System.nanoTime());
        genNewNumber();
    }

    /**
     * This method is activated when a number in the game is tapped
     * and takes actions based on whether this was the expected action
     * of the player.
     */
    public void onClickNumber(View v){
        if(gameState.equals(GameState.NEUTRAL)) {
            if (currentNum == unClickableNum)
                failOnNum();
            else
                successOnNum();
            timeLastClicked = System.currentTimeMillis();
        }
        saveLastNumberData();
    }

    /**
     * This is the method for an incorrect action from the user
     * clicking a number.  It changes the colour of the background
     * and increases the failCounter as well as setting the text
     * to an empty string until a new number can be generated.
     *
     * If the game length is expired at this point the game ends.
     */
    private void failOnNum(){
        rl.setBackgroundColor(getResources().getColor(R.color.negativeResult));
        gameState = GameState.SUCCESS;
        ++failCounter;
        numberDisplay.setText("");

        if(System.currentTimeMillis() - startTime > gameLength) {
            finish();
            overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        }
    }

    /**
     * This is the method for an correct action from the user
     * clicking a number.  It changes the colour of the background
     * and increases the failCounter as well as setting the text
     * to an empty string until a new number can be generated.
     *
     * If the game length is expired at this point the game ends.
     */
    private void successOnNum(){
        rl.setBackgroundColor(getResources().getColor(R.color.positiveResult));
        gameState = GameState.SUCCESS;
        ++successCounter;
        numberDisplay.setText("");

        if(System.currentTimeMillis() - startTime > gameLength) {
            finish();
            overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        }
    }
    /**
     * This method runs every time the handler cycle is complete.  The
     * handler calls this method to check the current state of the game
     * and update it as necessary.  If it is time for a question this method
     * calls a new activity and pauses the current one until the question activity
     * has finished.
     *
     * If it is not time for a new question it simply generates a new random digit for the game
     */
    private void checkSuccess(){

        if(currentNum == unClickableNum && gameState.equals(GameState.NEUTRAL)){
            ++successCounter;
            lastCorrect=true;
        }
        else if(currentNum == unClickableNum && gameState.equals(GameState.SUCCESS)){
            ++failCounter;
            lastCorrect=false;
        }

        if(questionID < questionIntervals.size() && (successCounter + failCounter + (rn.nextInt() % 3)) >= questionIntervals.get(questionID).getQuestionOrder()){
            Intent intent;
            if(questionIntervals.get(questionID).getQuestionType() == 1) {
                intent = new Intent(getApplicationContext(), InGameSliderQuestion.class);
                intent.putExtra("questionID", questionID);
                startActivityForResult(intent, 1);
            }
            else{
                intent = new Intent(getApplicationContext(), InGameMultiQuestion.class);
                intent.putExtra("questionID", questionID);
                startActivityForResult(intent, 0);
            }
            ++questionID;
        }
        else
            genNewNumber();
    }

    /**
     * This method takes action when an activity that is initated by this activity
     * has completed.  In this case it simply generates a new number to continue the game.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            saveLastNumberData(data.getIntExtra("choice", -1), questionID);
        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            saveLastNumberData(data.getIntExtra("choice", -1), questionID);
        }
        genNewNumber();
    }

    /**
     * This method generates a new random number between 0 and 9.
     * It also sets the background back to its standard colour and ensures
     * that the new number is different from the previous one for simple
     * preference purposes.  The gameState is reverted to NEUTRAL.
     */
    public void genNewNumber(){

        rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
        long hold = currentNum;
        while(hold == currentNum)
            currentNum = abs(rn.nextInt() % 10);

        numberDisplay.setText(Integer.toString(currentNum));
        numberDisplay.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        gameState = GameState.NEUTRAL;
        timeNumberDisplayed = System.currentTimeMillis();
    }

    /**
     * Upon clicking the "QUIT" imageview the activity finishes and returns the player to the mainMenu
     */
    public void onClickQuit(View v){
        saveLastNumberData();
        finish();
        overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
    }
    /**
     * This method saves all the data that is requested in the database.
     * It only does this if the "consent?" value in the preferences is true.
     */
    private void saveLastNumberData() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPref.getBoolean("Consent?", false)) {
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
            NumberGuess ng = new NumberGuess(currentNum, goodNum, timeNumberDisplayed);
            if(timeLastClicked<timeNumberDisplayed){
                ng.setResponseTime(0);
            }else{
                ng.setResponseTime(timeLastClicked-timeNumberDisplayed);
            }

            ng.setCorrect(lastCorrect);
            gs.addNumberGuess(ng);
         }
    }
    /**
     * In this version save the question result with a reference
     * to the previous numberData
     */
    private void saveLastNumberData(int questionResult, int questionID) {
        System.out.println(questionResult + " " + questionID);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPref.getBoolean("Consent?", false)) {
            System.out.println(questionResult + " " + questionID);
        }
    }

    public void saveGameSession(){
        //TODO remove toasts
        Toast.makeText(this, "Starting saving data", Toast.LENGTH_SHORT).show();
        gs.save(this);
        //TODO remove toasts
        Toast.makeText(this, "Completed saving data", Toast.LENGTH_SHORT).show();
        new DBUpload().execute(new DBpars(this));
    }
}
