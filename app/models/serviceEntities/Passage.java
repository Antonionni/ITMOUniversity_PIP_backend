package models.serviceEntities;

import models.entities.PassageEntity;
import models.entities.PassageEntityPK;

import java.util.Date;

public class Passage {

    private int id;

    private Date startdate;

    private Date enddate;

    private Integer correctAnswers;
    private Integer totalAnswers;

    private boolean isRight;

    public Passage() {}

    public Passage(int id, Date startdate, Date enddate, Integer correctAnswers, Integer totalAnswers, boolean isRight) {
        this.id = id;
        this.startdate = startdate;
        this.enddate = enddate;
        this.correctAnswers = correctAnswers;
        this.totalAnswers = totalAnswers;
        this.isRight = isRight;
    }

    public Passage(PassageEntity passageEntity) {
        this(passageEntity.getId(), passageEntity.getStartdate(), passageEntity.getEnddate(), passageEntity.getResult(), passageEntity.getTest().getQuestions().size(), passageEntity.isRight());
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
