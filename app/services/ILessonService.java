package services;

import models.serviceEntities.Lesson;
import models.serviceEntities.LessonInfo;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface ILessonService {
    CompletionStage<LessonInfo> create(LessonInfo lesson, int courseId);

    CompletionStage<LessonInfo> update(LessonInfo lesson, int courseId);

    CompletionStage<Boolean> delete(int lessonId);

    CompletionStage<Optional<Lesson>> get(int id);
}
