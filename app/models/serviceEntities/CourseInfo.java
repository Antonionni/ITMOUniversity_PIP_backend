package models.serviceEntities;

import models.entities.CourseEntity;
import models.entities.CourseSubscriptionEntity;
import models.serviceEntities.UserData.AggregatedUser;
import models.serviceEntities.UserData.Teacher;
import services.IUserService;
import services.UserService;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class CourseInfo {
    private int id;
    private String title;
    private String subject;
    private String imageurl;
    private Date createdAt;
    private Date updatedAt;
    private Collection<AggregatedUser> teachers;

    public CourseInfo() {}

    private CourseInfo(int id, String title, String subject, String imageurl, Date createdAt, Date updatedAt, Collection<AggregatedUser> teachers) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.imageurl = imageurl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.teachers = teachers;
    }

    public CourseInfo(CourseEntity courseEntity) {
        this(
                courseEntity.getId(),
                courseEntity.getTitle(),
                courseEntity.getSubject(),
                courseEntity.getImageurl(),
                courseEntity.getCreatedAt(),
                courseEntity.getUpdatedAt(),
                courseEntity.getCourseTeachers().stream().map(UserService::ToAggregatedUser).collect(Collectors.toList()));
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

    public Collection<AggregatedUser> getTeachers() {
        return teachers;
    }

    public void setTeachers(Collection<AggregatedUser> teachers) {
        this.teachers = teachers;
    }
}
