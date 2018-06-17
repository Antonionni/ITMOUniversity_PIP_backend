package models.serviceEntities;

import enumerations.AnswerType;
import models.entities.QuestionEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public class Question implements ILessonPageItem {
    private int id;
    private String textQuestion;
    private AnswerType answerType;
    private Collection<Answer> answers;

    public Question() {}

    public Question(int id, String textQuestion, AnswerType answerType, Collection<Answer> answers) {
        this.id = id;
        this.textQuestion = textQuestion;
        this.answerType = answerType;
        this.answers = answers;
    }

    public Question(QuestionEntity questionEntity) {
        this(
                questionEntity.getId(),
                questionEntity.getTextquestion(),
                questionEntity.getAnswerType(),
                questionEntity
                        .getAnswers()
                        .stream()
                        .map(Answer::new)
                        .collect(Collectors.toList()));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextQuestion() {
        return textQuestion;
    }

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public Collection<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Collection<Answer> answers) {
        this.answers = answers;
    }
}
