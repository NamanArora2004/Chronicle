package com.example.myapplication100;

public class QnAItem {
    private String question;
    private String answer;

    public QnAItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
