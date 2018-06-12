package models.serviceEntities.UserData;

import models.entities.UserEntity;
import models.serviceEntities.Course;

import java.util.Collection;
import java.util.stream.Collectors;

public class Student {
    private Collection<Course> courses;
    private String placeOfStudy;

    public Student(Collection<Course> courses, String placeOfStudy) {
        this.courses = courses;
        this.placeOfStudy = placeOfStudy;
    }

    public Student() {}

    public Student(UserEntity userEntity) {

        this(
                userEntity
                    .getStudentCourses()
                    .stream()
                    .map(Course::new)
                    .collect(Collectors.toList()),
                userEntity.getPlaceOfStudy());
    }

    public Collection<Course> getCourses() {
        return courses;
    }

    public void setCourses(Collection<Course> courses) {
        this.courses = courses;
    }

    public String getPlaceOfStudy() {
        return placeOfStudy;
    }

    public void setPlaceOfStudy(String placeOfStudy) {
        this.placeOfStudy = placeOfStudy;
    }
}

