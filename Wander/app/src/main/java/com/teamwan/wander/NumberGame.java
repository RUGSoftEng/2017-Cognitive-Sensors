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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

import com.teamwan.wander.db.DBHelper;
import com.teamwan.wander.db.DBUpload;
import com.teamwan.wander.db.DBpars;
import com.teamwan.wander.db.GameSession;
import com.teamwan.wander.db.NumberGuess;
import com.teamwan.wander.db.Question;
import com.teamwan.wander.db.QuestionAnswer;

import static java.lang.Math.abs;

/**
 * This class contains the number game which displays random digits
 * and asks questions the the user.
 */
public class NumberGame extends AppCompatActivity {

    private TextView numberDisplay;
    private Random rn;
    private long startTime;
    private long gameLength;
    private final int unClickableNum = 3;
    private int currentNum;
    private int questionID = 0;
    private ArrayList< Integer > questionIntervals;
    private ArrayList< Integer > questionSet;

    private int questionNumber;
    private int successCounter = 0;
    private int failCounter = 0;
    private int totalCounter = 0;
    private long timeLastClicked = 0;
    private long timeNumberDisplayed;
    private RelativeLayout rl;
    private GameState gameState;
    private boolean lastCorrect;
    private GameSession gs;
    public static final String AttemptDBUpload = "com.teamwan.wander.android.action.broadcast";

    private final Handler handle = new Handler();

    public enum GameState {
        SUCCESS, NEUTRAL
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

        gs = new GameSession(System.currentTimeMillis(),"numberGame");
        startTime = System.currentTimeMillis();
        runGame();
    }

    @Override
    protected void onDestroy(){
        saveGameSession();
        long time = System.currentTimeMillis();
        while ((System.currentTimeMillis()-time)<1000*2) { }
        openFeedback();
        super.onDestroy();
    }

    /**
     * This method initializes values pertaining to the
     * game such that the game can run.
     * The purpose of this is simply organizational
     * NOTE: Question Types can only be "MC" and anything else with the current iteration
     * TODO:: represent question types as values not strings
     */
    protected void runGame(){
        rl = (RelativeLayout)findViewById(R.id.gameUI);
//        questionID = 0;
        gameLength = (getResources().getInteger(R.integer.game_length)) * 1000;

        startTime = System.currentTimeMillis();
        timeNumberDisplayed = System.currentTimeMillis();
        //timeLastClicked = System.currentTimeMillis(); //???? What, game is only initialized, no click has even happened yet

//        successCounter = 0;
//        failCounter = 0;
//        totalCounter = 0;

        questionSet = new ArrayList<>();
        questionIntervals = new ArrayList<>();

        ArrayList<Question> questionList = (new DBHelper(this).getQuestions());
        int intervalCounter = 1;
        for(Question q : questionList)
        {
            if(q.getQuestionType().equals("MC"))
                questionSet.add(0);
            else
                questionSet.add(1);
            questionIntervals.add(10 * intervalCounter);
            ++intervalCounter;
        }

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

        if(questionID < questionIntervals.size() && (successCounter + failCounter + (rn.nextInt() % 3)) >= questionIntervals.get(questionID)){
            questionNumber = 0;
            callNextQuestion();
            ++questionID;
        }
        else
            genNewNumber();
    }

    private void callNextQuestion(){
        if(questionNumber < questionSet.size()) {
            if (questionSet.get(questionNumber).equals(1)) {
                Intent intent = new Intent(getApplicationContext(), InGameSliderQuestion.class);
                intent.putExtra("questionID", questionNumber);
                startActivityForResult(intent, 1);
            } else {
                Intent intent = new Intent(getApplicationContext(), InGameMultiQuestion.class);
                intent.putExtra("questionID", questionNumber);
                startActivityForResult(intent, 0);
            }
            ++questionNumber;
        }
        else
            genNewNumber();
    }

    /**
     * This method takes action when an activity that is initated by this activity
     * has completed.  In this case it simply generates a new number to continue the game.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK)
            saveLastNumberData(data.getIntExtra("choice", -1), questionID);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
            saveLastNumberData(data.getIntExtra("choice", -1), questionID);
        callNextQuestion();
    }

    /**
     * This method generates a new random number between 0 and 9.
     * It also sets the background back to its standard colour and ensures
     * that the new number is different from the previous one for simple
     * preference purposes.  The gameState is reverted to NEUTRAL.
     */
    public void genNewNumber(){

        totalCounter++;
        if(gs!= null && timeLastClicked > 0) {

            saveLastNumberData();
        }
        long hold = currentNum;
        while(hold == currentNum) {
            currentNum = abs(rn.nextInt() % 10);
        }

        numberDisplay.setText(Integer.toString(currentNum));
        numberDisplay.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
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

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        handle.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        final long delay = (getResources().getInteger(R.integer.number_display)) * 1000;
        final long jitter = (getResources().getInteger(R.integer.jitter_range)) * 100;

        handle.postDelayed(new Runnable(){
            public void run(){
                checkSuccess();
                handle.postDelayed(this, delay + ((rn.nextInt()) % jitter));
            }
        }, delay);
    }

    /**
     * This method saves all the data that is requested in the database.
     * It only does this if the "consent?" value in the preferences is true.
     * These values are available and need to be stored for the session for each number clicked:
     *  -timeLastClicked == system time at last number click
     *  -timeNumberDisplayed == last time a number was displayed
     *  -successCounter == successful clicks
     *  -failCounter == unsuccessful clicks
     *  -goodNum == TRUE if last displayed number was a clickable one, else FALSE
     */
    private void saveLastNumberData() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPref.getBoolean("Consent?", false)) {
            boolean goodNum = (currentNum != unClickableNum);
            NumberGuess ng = new NumberGuess(currentNum, goodNum, timeNumberDisplayed);
//            if(timeLastClicked<timeNumberDisplayed) {
//                ng.setResponseTime(0);
//            } else {
                ng.setResponseTime(timeLastClicked - timeNumberDisplayed);
//            }

            ng.setCorrect(lastCorrect);
            gs.addNumberGuess(ng);
         }
    }
    /**
     * In this version save the question result with a reference
     * to the previous numberData
     */
    private void saveLastNumberData(int questionResult, int questionID) {
        Log.i("saveLastNumberData", questionResult + " " + questionID);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPref.getBoolean("Consent?", false)) {
            Log.i("saveLastNumberData", "questionID:" + questionID);
            gs.addQuestionAnswer(new QuestionAnswer(System.currentTimeMillis(), questionID, questionResult));
        }
    }

    /**
     * Saves the gameSession to the local database and attempts to upload it, which depends on being connected to WIFI.
     */
    public void saveGameSession(){
        gs.setPercentage( 100 * (float)successCounter / (float)(totalCounter));
        gs.save(this);
        new DBUpload().execute(new DBpars(this));
    }

    public void openFeedback() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sharedPref.getBoolean("Consent?", false)) {
            Intent intent = new Intent(NumberGame.this, Feedback.class);
            NumberGame.this.startActivity(intent);
            overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        }
    }

}
