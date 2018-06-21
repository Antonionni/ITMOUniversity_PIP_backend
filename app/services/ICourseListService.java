package services;

import models.PagedResult;
import models.serviceEntities.CourseInfo;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ICourseListService {
    // haha so unperformance
    CompletionStage<List<CourseInfo>> listRandomCourses(int size);

    CompletionStage<List<CourseInfo>> findCourses(String keyword);

    CompletionStage<Boolean> subscribeToCourse(int courseId);

    CompletionStage<List<CourseInfo>> listAllCourses();
}
