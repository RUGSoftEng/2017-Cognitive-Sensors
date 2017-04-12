/**
 *  This class describes an object which contains data on a question
 *  that can be passed around as an object.
 *
 * @author  Ashton Spina
 * @version 1.1
 * @since   2017-03-20
 */

package com.teamwan.wander;

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
    public int getQuestionOrder(){ return questionOrder; }
}
