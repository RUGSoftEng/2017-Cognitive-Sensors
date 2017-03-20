package com.teamwan.wander;

/**
 * Created by LaminatedLama on 20-Mar-17.
 */

public class QuestionInfo {
    private int questionType;
    private int questionOrder;

    public QuestionInfo(int questionType, int questionOrder){
        this.questionType = questionType;
        this.questionOrder = questionOrder;
    }

    public int getQuestionType(){
        return questionType;
    }
    public int getQuestionOrder(){
        return questionOrder;
    }
}
