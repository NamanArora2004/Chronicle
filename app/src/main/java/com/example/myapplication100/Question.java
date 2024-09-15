package com.example.myapplication100;

import java.util.Map;

public class Question {
    private String questionId;
    private String questionText;
    private String askedBy;
    private Map<String, String> replies; // Map for replies with replyId as key and reply text as value

    public Question() {
    }

    public Question(String questionId, String questionText, String askedBy, Map<String, String> replies) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.askedBy = askedBy;
        this.replies = replies;
    }

    // Getters and setters for each field

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAskedBy() {
        return askedBy;
    }

    public void setAskedBy(String askedBy) {
        this.askedBy = askedBy;
    }

    public Map<String, String> getReplies() {
        return replies;
    }

    public void setReplies(Map<String, String> replies) {
        this.replies = replies;
    }
}
