package models.serviceEntities;

import models.entities.CourseEntity;
import models.entities.CourseSubscriptionEntity;

import java.util.Date;

public class CourseInfo {
    private int id;
    private String title;
    private String subject;
    private String imageurl;
    private Date createdAt;
    private Date updatedAt;

    private CourseInfo(int id, String title, String subject, String imageurl, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.imageurl = imageurl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CourseInfo(CourseEntity courseEntity) {
        this(
                courseEntity.getId(),
                courseEntity.getTitle(),
                courseEntity.getSubject(),
                courseEntity.getImageurl(),
                courseEntity.getCreatedAt(),
                courseEntity.getUpdatedAt());
    }

    public CourseInfo(CourseSubscriptionEntity courseSubscriptionEntity) {
        this(courseSubscriptionEntity.getCourse());
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
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
}
