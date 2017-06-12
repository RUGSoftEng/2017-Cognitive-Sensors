package com.teamwan.wander.db;

import java.util.ArrayList;

/** Class that represents a question object
*
* A question is represented by its questionID, the question itself and its questionType.
* Other parameters are needed to store in the database which are the next question to be asked and
* the boolean start that represents the start of a sequence of questions.
* **/
public class Question {

    private final int questionId;
    private final boolean start;
    private final boolean definesOnOffTask;
    private final String questionType;
    private final String question;
    private final ArrayList<Integer> nextQuestion;
    // The answers and onTask indicators only apply to a multiple-choice question
    private ArrayList<String> answers;
    private ArrayList<Boolean> onTask;

    Question(int questionId, boolean start, boolean definesOnOffTask, String type, String question,
             ArrayList<Integer> nextQuestion) {
        this.questionId = questionId;
        this.start = start;
        this.definesOnOffTask = definesOnOffTask;
        this.questionType = type;
        this.question = question;
        this.nextQuestion = nextQuestion;
    }

    int getQuestionId() {
        return questionId;
    }

    boolean isStart() {
        return start;
    }

    public String getQuestionType() {
        return questionType;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public ArrayList<Integer> getNextQuestion() {
        return nextQuestion;
    }

    public boolean isDefinesOnOffTask() {
        return definesOnOffTask;
    }

    public ArrayList<Boolean> getOnTask() {
        return onTask;
    }

    public void setOnTask(ArrayList<Boolean> onTask) {
        this.onTask = onTask;
    }
}
