package edu.bhcc.sebastianv.soccerquiz;

        import android.content.Intent;
        import android.os.Build;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;

/**
 * Created by Sebastian Vasco on 9/20/2017.
 * Cheat Activity
 */

public class CheatActivity extends AppCompatActivity {

    public static final String EXTRA_ANSWER_IS_TRUE =
            "edu.bhcc.sebastianv.soccerquiz.answer_is_true"; //whether answer is true to false
    public static final String EXTRA_ANSWER_SHOWN =
            "edu.bhcc.sebastianv.soccerquiz.answer_shown";  //whether answer was shown
    private static final String KEY_IS_CHEATER="cheater";  //whether user cheated->key to this class
    private static final String API_LEVEL="API Level ";

    private boolean answerIsTrue;
    private Boolean sawAnswer;

    private TextView answerTextView;
    private Button showAnswer;

    private TextView apiVersionTextView;

    //Sending back an intent->if answer is shown or not, this will let QuizActivity know
    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data); //when users clicks show Ans. CheatAct. saves result here
        sawAnswer=isAnswerShown;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {  //save instance to bundle
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        //Text box will display API version
        apiVersionTextView = (TextView) findViewById(R.id.user_api_level);
        apiVersionTextView.setText(API_LEVEL + Build.VERSION.SDK_INT);

        if (savedInstanceState != null) {  //If instance exist

            //was answer shown before previous activity->last saved instance
            setAnswerShownResult(savedInstanceState.getBoolean(KEY_IS_CHEATER, false));
            sawAnswer = savedInstanceState.getBoolean(KEY_IS_CHEATER);  //did user cheat before last instance?

            //Grabs correct answer and assigns it to variable
            answerIsTrue = savedInstanceState.getBoolean(EXTRA_ANSWER_IS_TRUE, false);

            //Grabs Answer display text box
            answerTextView = (TextView) findViewById(R.id.answer_text_view);

            //statement checks if user already cheated
            if ((answerIsTrue) && (sawAnswer)) //is answer true and user did cheated already? display the answer again
                answerTextView.setText(R.string.true_button);
            else if ((!answerIsTrue) && (sawAnswer)) //is answer false user did cheated already? display answer again
                answerTextView.setText(R.string.true_button);

        }else { //if there was instance previously saved, user never cheated and answer was never shown
            sawAnswer = false;
            setAnswerShownResult(false);
        }


        /*If saved instance is bull in previous if statement, the following variables wouldnt be
        assigned.  In this case, you have to reassign the variables to make sure the textbox as well
        as the boolean variable is not null.*/
        answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        answerTextView = (TextView) findViewById(R.id.answer_text_view);

        //Show answer button
        showAnswer = (Button) findViewById(R.id.show_answer_button);
        showAnswer.setOnClickListener(new View.OnClickListener() { //shows answer button
            @Override
            public void onClick(View v) {

                //displays the corresponding toast to the question
                if (answerIsTrue) {
                    answerTextView.setText(R.string.true_button);
                } else {
                    answerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true); //assign the cheater status
            }


        });
    }

    //Saving data across rotation
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_IS_CHEATER, sawAnswer);// save the cheat status
        savedInstanceState.putBoolean(EXTRA_ANSWER_IS_TRUE, answerIsTrue);// is answer T/F
    }

}
