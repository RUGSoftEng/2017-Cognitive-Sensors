package com.teamwan.wander;

/**
 * Created by LaminatedLama on 20-Mar-17.
 */

public class QuestionInfo {
    private int questionType;
    private int questionOrder;
   // private int questionNumber;

    public QuestionInfo(int questionType, int questionOrder){
        this.questionType = questionType;
        this.questionOrder = questionOrder;
        //this.questionNumber = questionNumber;
    }

    public int getQuestionType(){
        return questionType;
    }
    public int getQuestionOrder(){ return questionOrder; }
    //public int getQuestionNumber(){ return questionNumber;}
}
