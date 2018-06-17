package models.serviceEntities;

import models.entities.LessonEntity;

import java.util.Date;

public class LessonInfo {
    private int id;
    private String title;

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

    public LessonInfo() {}

    public LessonInfo(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public LessonInfo(LessonEntity lessonEntity) {
        this(lessonEntity.getId(), lessonEntity.getTitle());
    }
}
