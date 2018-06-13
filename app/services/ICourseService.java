package services;

import models.serviceEntities.Course;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface ICourseService {
    CompletionStage<Optional<Course>> get(int courseId);

    CompletionStage<Course> create(Course course);

    CompletionStage<Course> update(Course course);

    CompletionStage<Boolean> delete(int courseId);
}
