package models.serviceEntities.UserData;

import models.entities.UserEntity;
import models.serviceEntities.CourseInfo;

import java.util.Collection;
import java.util.stream.Collectors;

public class Student {
    private Collection<CourseInfo> courseInfos;
    private String placeOfStudy;

    public Student(Collection<CourseInfo> courseInfos, String placeOfStudy) {
        this.courseInfos = courseInfos;
        this.placeOfStudy = placeOfStudy;
    }

    public Student() {}

    public Student(UserEntity userEntity) {

        this(
                userEntity
                    .getStudentCourses()
                    .stream()
                    .map(CourseInfo::new)
                    .collect(Collectors.toList()),
                userEntity.getPlaceOfStudy());
    }

    public Collection<CourseInfo> getCourseInfos() {
        return courseInfos;
    }

    public void setCourseInfos(Collection<CourseInfo> courseInfos) {
        this.courseInfos = courseInfos;
    }

    public String getPlaceOfStudy() {
        return placeOfStudy;
    }

    public void setPlaceOfStudy(String placeOfStudy) {
        this.placeOfStudy = placeOfStudy;
    }
}

