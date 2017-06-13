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
import java.util.List;
import java.util.Locale;
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
    private static final Random rn = new Random(System.currentTimeMillis());
    private long startTime;
    private long gameLength;
    private final int unClickableNum = 3;
    private int currentNum;
    private List<Question> questionList;

    private int successCounter = 0;
    private int failCounter = 0;
    private int totalCounter = 0;
    private int idleCount = -1;
    private long timeLastClicked = 0;
    private long idleCheck = 0;
    private long timeNumberDisplayed;
    private GameState gameState;
    private boolean lastCorrect;
    private boolean saveGame = true;
    private GameSession gs;

    // The ID of the question that was last asked
    private int lastQuestionId;

    private int nextQuestionAt;

    private final Handler handle = new Handler();

    private enum GameState {
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

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        saveGame = sharedPref.getBoolean("Consent?", false);

        gs = new GameSession(System.currentTimeMillis(),"numberGame");
        startTime = System.currentTimeMillis();
        runGame();
    }

    @Override
    protected void onDestroy(){
        if (saveGame) { saveGameSession(); }
        long time = System.currentTimeMillis();
        while ((System.currentTimeMillis()-time)<1500) { }

        if (saveGame) { openFeedback(); }
        super.onDestroy();
    }

    /**
     * This method initializes values pertaining to the
     * game such that the game can run.
     * The purpose of this is simply organizational
     * NOTE: Question Types can only be "MC" and anything else with the current iteration
     * TODO:: represent question types as values not strings
     */
    private void runGame(){
        gameLength = (getResources().getInteger(R.integer.game_length)) * 1000;

        startTime = System.currentTimeMillis();
        timeNumberDisplayed = System.currentTimeMillis();

        nextQuestionAt = 10 + rn.nextInt(3);

        questionList = new DBHelper(this).getQuestions();

        numberDisplay = (TextView) findViewById(R.id.numberDisplay);
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
     * This is the method for an incorrect action from the user clicking a number. It increases the
     * failCounter as well as setting the text to an empty string until a new number can be
     * generated.
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

        if (successCounter + failCounter >= nextQuestionAt) {
            nextQuestionAt += 10 + rn.nextInt(3);
            callNextQuestion(0);
        } else
            genNewNumber();
    }

    private final static int REQUEST_CODE_MC = 0;
    private final static int REQUEST_CODE_SLIDER = 1;

    /**
     * Show the next question.
     * @param questionID - The questionID to show
     */
    private void callNextQuestion(int questionID){
        Log.d(this.getClass().getSimpleName(), "Showing question with ID " + questionID);
        lastQuestionId = questionID;
        if (questionList.get(questionID).getQuestionType().equals("MC")) {
            Intent intent = new Intent(getApplicationContext(), InGameMultiQuestion.class);
            intent.putExtra(InGameMultiQuestion.EXTRA_QUESTION_ID, questionID);
            startActivityForResult(intent, REQUEST_CODE_MC);
        } else {
            Intent intent = new Intent(getApplicationContext(), InGameSliderQuestion.class);
            intent.putExtra(InGameSliderQuestion.EXTRA_QUESTION_ID, questionID);
            startActivityForResult(intent, REQUEST_CODE_SLIDER);
        }
    }

    /**
     * This method takes action when an activity that is initiated by this activity
     * has completed.  In this case it simply generates a new number to continue the game.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int nextQuestion = -1;
        if (requestCode == REQUEST_CODE_SLIDER && resultCode == Activity.RESULT_OK) {
            saveLastNumberData(data.getIntExtra(InGameSliderQuestion.EXTRA_CHOICE, -1), lastQuestionId);
            nextQuestion = data.getIntExtra(InGameSliderQuestion.EXTRA_NEXT_QUESTION, -1);
        } else if (requestCode == REQUEST_CODE_MC && resultCode == Activity.RESULT_OK) {
            saveLastNumberData(data.getIntExtra(InGameMultiQuestion.EXTRA_CHOICE, -1), lastQuestionId);
            nextQuestion = data.getIntExtra(InGameMultiQuestion.EXTRA_NEXT_QUESTION, -1);
        }

        if (nextQuestion != -1)
            callNextQuestion(nextQuestion);
    }

    /**
     * This method generates a new random number between 0 and 9.
     * It also ensures that the new number is different from the
     * previous one for simple preference purposes.  The gameState
     * is reverted to NEUTRAL. Checks for 10 consecutive numbers
     * without user input, if this happens, the game is closed.
     */
    private void genNewNumber(){

        if (timeLastClicked==idleCheck) {
            idleCount++;
            if (idleCount==10) { saveGame=false; finish(); }
        } else {
            idleCheck = timeLastClicked;
            idleCount = 0;
        }

        totalCounter++;
        if(gs!= null && timeLastClicked > 0) {

            saveLastNumberData();
        }
        long hold = currentNum;
        while(hold == currentNum) {
            currentNum = abs(rn.nextInt() % 10);
        }

        numberDisplay.setText(String.format(Locale.getDefault(), "%d", currentNum));
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
        super.onPause();

        handle.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
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
            if(timeLastClicked>timeNumberDisplayed) {
                ng.setResponseTime(timeLastClicked - timeNumberDisplayed);
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
    private void saveGameSession(){
        gs.setPercentage((float)totalCounter == 0 ? 0 : 100 * (float)successCounter / (float)(totalCounter));
        gs.save(this);
        new DBUpload().execute(new DBpars(this));
    }

    private void openFeedback() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(sharedPref.getBoolean("Consent?", false)) {
            Intent intent = new Intent(NumberGame.this, Feedback.class);
            NumberGame.this.startActivity(intent);
            overridePendingTransition(R.animator.fade_in, R.animator.fade_out);
        }
    }

}
