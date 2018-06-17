package models.serviceEntities;

import models.entities.AnswerEntity;
import models.entities.PassageHasAnswersEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public class PassageItem {
    private int id;
    private int questionId;
    private Collection<Integer> answerIds;
    private String textAnswer;
    private Boolean verified;

    public PassageItem() {}

    public PassageItem(int id, int questionId, Collection<Integer> answerIds, String textAnswer, Boolean verified) {
        this.id = id;
        this.questionId = questionId;
        this.answerIds = answerIds;
        this.textAnswer = textAnswer;
        this.verified = verified;
    }

    public PassageItem(PassageHasAnswersEntity passageHasAnswersEntity) {
        this(passageHasAnswersEntity.getId(),
                passageHasAnswersEntity.getQuestionEntity().getId(),
                passageHasAnswersEntity
                        .getAnswers()
                        .stream()
                        .map(AnswerEntity::getId)
                        .collect(Collectors.toList()),
                passageHasAnswersEntity.getTextAnswer(),
                passageHasAnswersEntity.getVerifiedByTeacher());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public Collection<Integer> getAnswerIds() {
        return answerIds;
    }

    public void setAnswerIds(Collection<Integer> answerIds) {
        this.answerIds = answerIds;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }
}
