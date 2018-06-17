package models.serviceEntities;

import models.entities.CoursePeriodEntity;

import java.util.Date;

public class CoursePeriod {
    private int id;
    private Date startDate;
    private Date endDate;

    public CoursePeriod() {}

    public CoursePeriod(CoursePeriodEntity coursePeriodEntity) {
        this.startDate = coursePeriodEntity.getStartdate();
        this.endDate = coursePeriodEntity.getEnddate();
        this.id = coursePeriodEntity.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
