package models.serviceEntities.UserData;

import models.entities.UserEntity;
import models.serviceEntities.Course;

import java.util.Collection;
import java.util.stream.Collectors;

public class Teacher {
    private Collection<Course> courses;

    public Teacher(Collection<Course> courses) {
        this.courses = courses;
    }

    public Teacher(UserEntity userEntity) {
        this(userEntity
                .getTeacherCourses()
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
