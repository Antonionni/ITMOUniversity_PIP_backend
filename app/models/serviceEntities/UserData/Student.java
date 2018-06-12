package models.serviceEntities.UserData;

import models.entities.UserEntity;
import models.serviceEntities.Course;

import java.util.Collection;
import java.util.stream.Collectors;

public class Student {
    private Collection<Course> courses;

    public Student(Collection<Course> courses) {
        this.courses = courses;
    }

    public Student(UserEntity userEntity) {
        this(userEntity
                .getStudentCourses()
                .stream()
                .map(Course::new)
                .collect(Collectors.toList()));
    }

    public Collection<Course> getCourses() {
        return courses;
    }

    public void setCourses(Collection<Course> courses) {
        this.courses = courses;
    }
}

