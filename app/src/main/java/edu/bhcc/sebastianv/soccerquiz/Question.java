package edu.bhcc.sebastianv.soccerquiz;

/**
 * Created by Sebastian Vasco on 9/19/2017.
 * Class holds question and answer.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;

    public Question(int textResId, boolean manswerTrue) {
        mTextResId = textResId;
        mAnswerTrue = manswerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
