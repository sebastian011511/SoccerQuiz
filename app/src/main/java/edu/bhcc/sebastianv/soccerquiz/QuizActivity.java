/**
 * Created by Sebastian Vasco on 9/19/2017.
 * This App will ask 7 True or False questions.  User needs to answer the question and they will be
 * graded.  If user does not know the answer, they can cheat but they will get the answer wrong.
 * Ap will also display the number of questions in which they cheated on.
 */


package edu.bhcc.sebastianv.soccerquiz;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index"; //saved instance question currently answering
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String KEY_CHEATER = "did cheat";//saved instance if cheated or not
    private static final String KEY_ARRAY_CHEATER = "cheater array";
    private static final String KEY_NUMBER_CHEATS = "number of cheats";
    private static final int KEY_TOTAL_QUESTIONS = 7;
    private static final String KEY_ANSWERED_QUESTIONS_ARRAY = "answered all questions array";
    private static final String KEY_CORRECT_ANSWERS = "correct answers";

    private Button trueButton;
    private Button falseButton;
    private Button cheatButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView questionTextView;
    private TextView progressTextView; //display # of correct questions
    private TextView finalMessageTextView; //display the final message
    private TextView numberCheatsTextView; //display the # of times cheated

    //Storing all question w/ corresponding answers
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_red_card, false),
            new Question(R.string.question_mississippi, true),
            new Question(R.string.question_pele, true),
            new Question(R.string.question_penalty, false),
            new Question(R.string.question_world_cup, false),
            new Question(R.string.question_ronaldinho, true),
            new Question(R.string.question_soccer_ball, true)
    };

    private int currentIndex = 0;
    private boolean mIsCheater; //weather he cheated or not
    /*array will hold cheat status.  index matches questionBank*/
    private boolean didCheat[] = {false, false, false, false, false, false, false};
    private int numberOfCheats = 0;

    //private boolean correctBank[] = {false, false, false, false, false, false, false};
    /*array will hold if questions has been answered status.  index matches questionBank*/
    private boolean alreadyAnsweredBank[] = {false, false, false, false, false, false, false};
    private int correctAnswers = 0;  //# of questions answered correctly

    private void updateQuestion() {
        int question = mQuestionBank[currentIndex].getTextResId();
        questionTextView.setText(question);
    }

    /*Check if all questions were answered, user can go in a loop but if the answered has already
    * been answered, user will not be able to change their answer*/
    private void compAllQuestions() {

        int totalQuestionsAnsw = 0;

        for (int value = 0; value < alreadyAnsweredBank.length; value++) {
            if (alreadyAnsweredBank[value])
                totalQuestionsAnsw++;
        }

        if (totalQuestionsAnsw == KEY_TOTAL_QUESTIONS) { //if all questions completed=display corresponding message
            double percentGrade;
            NumberFormat formatter = new DecimalFormat("#0.0"); //Class to format decimal

            switch (correctAnswers) {
                case 1:
                case 2:
                case 3:
                case 4:
                    finalMessageTextView.setText(R.string.grade_below_5);
                    break;
                case 5:
                    finalMessageTextView.setText(R.string.grade_5);
                    break;
                case 6:
                    finalMessageTextView.setText(R.string.grade_6);
                    break;
                case 7:
                    finalMessageTextView.setText(R.string.grade_7);
                    break;
            }

            trueButton.setClickable(false);
            falseButton.setClickable(false);
            cheatButton.setClickable(false);
            percentGrade = 100 * ((double) correctAnswers / (double) KEY_TOTAL_QUESTIONS); //score in percent

            //final message, summary
            progressTextView.setText("Total Grade: " + correctAnswers + "/" + KEY_TOTAL_QUESTIONS
                    + "= " + formatter.format(percentGrade) + "%");
            numberCheatsTextView.setText("Cheated " + numberOfCheats + " times.");
        }

    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[currentIndex].isAnswerTrue();
        int messageResId; //ID associated with string

        if (userPressedTrue == answerIsTrue) { //if user clicked right answer

            if (didCheat[currentIndex]) { //did cheat and correct answer
                messageResId = R.string.judgment_toast;
                if (!alreadyAnsweredBank[currentIndex]) {  //if user has not answered b4, then increment cheats
                    numberOfCheats++;
                    numberCheatsTextView.setText("Cheats:\n" + numberOfCheats);
                }
            } else {
                messageResId = R.string.correct_toast;
                if (!alreadyAnsweredBank[currentIndex]) { //if user has not answered question before->meaning not false
                    correctAnswers++;  //increment correct answers
                    //display current total
                    progressTextView.setText("Current Score: " + correctAnswers + "/" + KEY_TOTAL_QUESTIONS);
                }
            }
        } else {   //user's answer doesnt math the correct answer
            if (didCheat[currentIndex]) {           //did cheat in that question
                messageResId = R.string.judgment_toast;
                if (!alreadyAnsweredBank[currentIndex]) {  ////if user has not answered b4, then increment cheats
                    numberOfCheats++;
                    numberCheatsTextView.setText("Cheats:\n" + numberOfCheats);
                }
            } else {
                if (alreadyAnsweredBank[currentIndex])  //If you have already answered the question, display message
                    messageResId = R.string.already_answered;
                else {
                    messageResId = R.string.incorrect_toast;
                    //Display current total
                    progressTextView.setText("Current Score: " + correctAnswers + "/" + KEY_TOTAL_QUESTIONS);
                    numberCheatsTextView.setText("Cheats:\n" + numberOfCheats);
                }
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        alreadyAnsweredBank[currentIndex] = true; //by clicking on a T/F, user has answered the question
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        /**ActionBar aBar=getActionBar();
        aBar.setTitle("Soccer Quiz");
        aBar.setSubtitle("Do you know your sport?");           //  The action bar keeps crashing my app*/

        //Text view of current progress
        progressTextView = (TextView) findViewById(R.id.progress_text_view);
        progressTextView.setText("Current Score: " + correctAnswers + "/" + KEY_TOTAL_QUESTIONS);
        //Text View of # of cheats
        numberCheatsTextView = (TextView) findViewById(R.id.cheat_result_text_view);
        numberCheatsTextView.setText("Cheats:\n" + numberOfCheats);

        finalMessageTextView = (TextView) findViewById(R.id.final_message);

        questionTextView = (TextView) findViewById(R.id.question_text_view);
        questionTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                currentIndex = (currentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        /* Action listeners for all four buttons starting from TRUE,FALSE,PREV,NEXT */

        trueButton = (Button) findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                compAllQuestions();
            }
        });

        falseButton = (Button) findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                compAllQuestions();
            }
        });

        nextButton = (ImageButton) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        prevButton = (ImageButton) findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % mQuestionBank.length;

                //This prevents program from crashing when going out of bound using prev key
                if (currentIndex < 0)
                    currentIndex = mQuestionBank.length + currentIndex;

                updateQuestion();
            }
        });

        if (savedInstanceState != null) { //if an instance of this state already exist
            didCheat = savedInstanceState.getBooleanArray(KEY_ARRAY_CHEATER);
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0); //current index pulled from the saved instance
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false); //status of weather the user cheated on this question
            correctAnswers = savedInstanceState.getInt(KEY_CORRECT_ANSWERS, 0); //number of correct questions
            alreadyAnsweredBank = savedInstanceState.getBooleanArray(KEY_ANSWERED_QUESTIONS_ARRAY); //array hold status if ? was answered
            numberOfCheats = savedInstanceState.getInt(KEY_NUMBER_CHEATS, numberOfCheats);//number of times cheated

            Log.i(TAG, "Saved all instance states");
        }

        cheatButton = (Button) findViewById(R.id.cheat_button);
        cheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Starting cheat activity");
                //Start cheat activity when cheat button is clicked

                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[currentIndex].isAnswerTrue();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        progressTextView.setText("Current Score: " + correctAnswers + "/" + KEY_TOTAL_QUESTIONS);
        numberCheatsTextView.setText("Cheats:\n" + numberOfCheats);
        updateQuestion();

    }


    //Getting info from cheat activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            //storing data from cheatActivity into boolean and boolean array
            mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
            didCheat[currentIndex] = mIsCheater;
            Log.d(TAG, "storing data in variable & array");
        }
    }

    //Saving data across rotation
    public void onSaveInstanceState(Bundle savedInstanceSate) {
        super.onSaveInstanceState(savedInstanceSate);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceSate.putInt(KEY_INDEX, currentIndex); //saves instance in current bank/array position
        savedInstanceSate.putBooleanArray(KEY_ARRAY_CHEATER, didCheat);
        savedInstanceSate.putBoolean(KEY_CHEATER, mIsCheater); //saves instance if cheated
        savedInstanceSate.putBooleanArray(KEY_ANSWERED_QUESTIONS_ARRAY, alreadyAnsweredBank); //saves instance if answered question
        savedInstanceSate.putInt(KEY_CORRECT_ANSWERS, correctAnswers); //saves instance of number of correct questions
        savedInstanceSate.putInt(KEY_NUMBER_CHEATS, numberOfCheats); //saves instance of number of times cheated
    }

    //Logcats of the lifecycle
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
