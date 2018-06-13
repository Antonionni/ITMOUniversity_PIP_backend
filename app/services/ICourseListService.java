package services;

import models.PagedResult;
import models.serviceEntities.Course;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ICourseListService {
    // haha so unperformance
    CompletionStage<List<Course>> listRandomCourses(int size);

    CompletionStage<PagedResult<Course>> findCourses(String keyword, int pageNumber, int pageSize);

    CompletionStage<Boolean> subscribeToCourse(int courseId);
}
