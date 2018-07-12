package models.serviceEntities.UserData;

import models.entities.UserEntity;
import models.serviceEntities.CourseInfo;
import models.serviceEntities.Passage;

import java.util.Collection;
import java.util.stream.Collectors;

public class Student {
    private Collection<CourseInfo> courseInfos;
    private Collection<Passage> passages;

    public Student(Collection<CourseInfo> courseInfos, Collection<Passage> passages) {
        this.courseInfos = courseInfos;
        this.passages = passages;
    }

    public Student() {}

    public Student(UserEntity userEntity) {

        this(
                userEntity
                    .getStudentCourses()
                    .stream()
                    .map(CourseInfo::new)
                    .collect(Collectors.toList()),
                userEntity.getPassages()
                .stream()
                .map(Passage::new)
                .collect(Collectors.toList()));
    }

    public Collection<CourseInfo> getCourseInfos() {
        return courseInfos;
    }

    public void setCourseInfos(Collection<CourseInfo> courseInfos) {
        this.courseInfos = courseInfos;
    }

    public Collection<Passage> getPassages() {
        return passages;
    }

    public void setPassages(Collection<Passage> passages) {
        this.passages = passages;
    }
}

