package models.serviceEntities;

import models.entities.CourseEntity;
import models.entities.CoursePeriodEntity;
import models.entities.LessonEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public class Course {
    private CourseInfo courseInfo;
    private Collection<CoursePeriod> coursePeriods;
    private Collection<Lesson> lessons;

    public Course() {}

    public Course(CourseEntity courseEntity, Collection<CoursePeriodEntity> coursePeriodEntities, Collection<LessonEntity> lessonEntities) {
        courseInfo = new CourseInfo(courseEntity);
        coursePeriods = coursePeriodEntities.stream().map(CoursePeriod::new).collect(Collectors.toList());
        lessons = lessonEntities.stream().map(x -> new Lesson(x, x.getLessonTests(), x.getMaterials())).collect(Collectors.toList());
    }

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public Collection<CoursePeriod> getCoursePeriods() {
        return coursePeriods;
    }

    public void setCoursePeriods(Collection<CoursePeriod> coursePeriods) {
        this.coursePeriods = coursePeriods;
    }

    public Collection<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Collection<Lesson> lessons) {
        this.lessons = lessons;
    }
}
