package services;

import models.serviceEntities.Course;
import models.serviceEntities.CourseInfo;
import models.serviceEntities.CoursePeriod;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface ICourseService {
    CompletionStage<Optional<Course>> get(int courseId);

    CompletionStage<CourseInfo> create(CourseInfo courseInfo);

    CompletionStage<CourseInfo> update(CourseInfo courseInfo);

    CompletionStage<Boolean> delete(int courseId);

    CompletionStage<CoursePeriod> createPeriod(CoursePeriod coursePeriod, int courseId);

    CompletionStage<CoursePeriod> updatePeriod(CoursePeriod coursePeriod, int courseId);

    CompletionStage<Boolean> deletePeriod(int coursePeriodId);
}
