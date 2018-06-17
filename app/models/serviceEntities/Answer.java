package models.serviceEntities;

import models.entities.AnswerEntity;

public class Answer {
    private int id;
    private String text;

    public Answer() {}

    public Answer(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public Answer(AnswerEntity answerEntity) {
        this(answerEntity.getId(), answerEntity.getUseranswer());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
