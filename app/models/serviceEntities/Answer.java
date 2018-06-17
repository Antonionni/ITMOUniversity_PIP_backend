package models.serviceEntities;

import models.entities.AnswerEntity;

public class Answer {
    private int id;
    private String text;
    private Boolean rightAnswer;

    public Answer() {}

    public Answer(int id, String text, Boolean rightAnswer) {
        this.id = id;
        this.text = text;
        this.rightAnswer = rightAnswer;
    }

    public Answer(AnswerEntity answerEntity) {
        this(answerEntity.getId(), answerEntity.getUseranswer(), answerEntity.isRight());
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

    public Boolean getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(Boolean rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
}
