package com.zm.search.bean;

public class QAEntity {

    private int id;
    private String childTopicId;
    private String questionId;
    private String questionName;
    private String answerId;
    private String userName;
    private String excerpt;
    private boolean isArtical;

    public QAEntity() {
    }

    public QAEntity(int id, String childTopicId, String questionId, String questionName, String answerId, String userName, String excerpt, boolean isArtical) {
        this.id = id;
        this.childTopicId = childTopicId;
        this.questionId = questionId;
        this.questionName = questionName;
        this.answerId = answerId;
        this.userName = userName;
        this.excerpt = excerpt;
        this.isArtical = isArtical;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChildTopicId() {
        return childTopicId;
    }

    public void setChildTopicId(String childTopicId) {
        this.childTopicId = childTopicId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public boolean isArtical() {
        return isArtical;
    }

    public void setArtical(boolean artical) {
        isArtical = artical;
    }
}
