package models.serviceEntities;

import models.entities.MaterialEntity;
import models.entities.TestEntity;

import java.util.Date;

public class LessonPageInfo {
    private int id;
    private String title;
    private Date createdAt;
    private Date updatedAt;

    //bad code because it applies only for test ^_^
    private int testTreshold;
    private int minutesToGo;

    public LessonPageInfo() {}

    public LessonPageInfo(int id, String title, Date createdAt, Date updatedAt, int testTreshold, int minutesToGo) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.testTreshold = testTreshold;
        this.minutesToGo = minutesToGo;
    }

    public LessonPageInfo(TestEntity testEntity) {
        this(testEntity.getId(), testEntity.getTitle(), testEntity.getCreatedAt(), testEntity.getUpdatedAt(), testEntity.getThreshold(), testEntity.getMinutesToGo());
    }

    public LessonPageInfo(MaterialEntity materialEntity) {
        this(materialEntity.getId(), materialEntity.getTitle(), materialEntity.getCreatedAt(), materialEntity.getUpdatedAt(), 0, 0);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTestTreshold() {
        return testTreshold;
    }

    public void setTestTreshold(int testTreshold) {
        this.testTreshold = testTreshold;
    }

    public int getMinutesToGo() {
        return minutesToGo;
    }

    public void setMinutesToGo(int minutesToGo) {
        this.minutesToGo = minutesToGo;
    }
}
