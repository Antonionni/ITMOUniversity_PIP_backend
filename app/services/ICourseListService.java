package services;

import models.serviceEntities.Course;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ICourseListService {
    // haha so unperformance
    CompletionStage<List<Course>> listRandomCourses(int size);

    CompletionStage<List<Course>> findCourses(String keyword);

    CompletionStage<Boolean> subscribeToCourse(int courseId);

    CompletionStage<List<Course>> listAllCourses();
}
