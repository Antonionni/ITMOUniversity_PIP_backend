package models.serviceEntities;

import models.entities.PassageEntity;

import java.util.Date;

public class Passage {

    private Date startdate;

    private Date enddate;

    private Integer correctAnswers;
    private Integer totalAnswers;

    private boolean isRight;

    public Passage() {}

    public Passage(Date startdate, Date enddate, Integer correctAnswers, Integer totalAnswers, boolean isRight) {
        this.startdate = startdate;
        this.enddate = enddate;
        this.correctAnswers = correctAnswers;
        this.totalAnswers = totalAnswers;
        this.isRight = isRight;
    }

    public Passage(PassageEntity passageEntity) {
        this(passageEntity.getStartdate(), passageEntity.getEnddate(), passageEntity.getResult(), passageEntity.getTest().getQuestions().size(), passageEntity.isRight());
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(Integer totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
