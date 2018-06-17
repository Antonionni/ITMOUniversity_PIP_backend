package models.serviceEntities.UserData;

import models.entities.UserEntity;
import models.serviceEntities.CourseInfo;

import java.util.Collection;
import java.util.stream.Collectors;

public class Teacher {
    private Collection<CourseInfo> courseInfos;

    public Teacher(Collection<CourseInfo> courseInfos) {
        this.courseInfos = courseInfos;
    }

    public Teacher() {}

    public Teacher(UserEntity userEntity) {
        this(userEntity
                .getTeacherCourses()
                .stream()
                .map(CourseInfo::new)
                .collect(Collectors.toList()));
    }

    public Collection<CourseInfo> getCourseInfos() {
        return courseInfos;
    }

    public void setCourseInfos(Collection<CourseInfo> courseInfos) {
        this.courseInfos = courseInfos;
    }
}
