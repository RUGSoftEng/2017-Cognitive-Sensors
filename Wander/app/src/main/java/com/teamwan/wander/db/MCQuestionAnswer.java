package com.teamwan.wander.db;

/**
 * Class that represents a multiple choice question.
 *
 * If a question is a multiple choice question we need to store the di↵erent answers that
 * the user can choose between.
 */

class MCQuestionAnswer {

    private int questionI;
    private int answerNumber;
    private String answer;


    public MCQuestionAnswer(int questionI, int answerNumber, String answer) {
        this.questionI = questionI;
        this.answerNumber = answerNumber;
        this.answer = answer;
    }

    public int getQuestionI() {
        return questionI;
    }

    public void setQuestionI(int questionI) {
        this.questionI = questionI;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


}
