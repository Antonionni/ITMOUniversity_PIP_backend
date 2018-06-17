package models.serviceEntities;

import javafx.scene.paint.Material;
import models.entities.MaterialEntity;
import models.entities.TestEntity;

import java.util.Date;

public class LessonPageInfo {
    private int id;
    private String title;
    private Date createdAt;
    private Date updatedAt;

    public LessonPageInfo() {}

    public LessonPageInfo(int id, String title, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public LessonPageInfo(TestEntity testEntity) {
        this(testEntity.getId(), testEntity.getTitle(), testEntity.getCreatedAt(), testEntity.getUpdatedAt());
    }

    public LessonPageInfo(MaterialEntity materialEntity) {
        this(materialEntity.getId(), materialEntity.getTitle(), materialEntity.getCreatedAt(), materialEntity.getUpdatedAt());
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
}
