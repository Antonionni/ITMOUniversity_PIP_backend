package services;

import models.serviceEntities.Lesson;
import models.serviceEntities.LessonInfo;
import models.serviceEntities.LessonPage;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface ILessonService {
    CompletionStage<LessonInfo> create(LessonInfo lesson, int courseId);

    CompletionStage<LessonInfo> update(LessonInfo lesson, int courseId);

    CompletionStage<Boolean> delete(int lessonId);

    CompletionStage<Optional<Lesson>> get(int id);

    CompletionStage<Optional<LessonPage>> getMaterial(int id);

    CompletionStage<LessonPage> createMaterial(LessonPage page, int lessonId);

    CompletionStage<LessonPage> updateMaterial(LessonPage page, int lessonId);

    CompletionStage<Boolean> deleteMaterial(int materialId);

    CompletionStage<LessonPage> createTest(LessonPage page, int lessonId);

    CompletionStage<LessonPage> updateTest(LessonPage page, int lessonId);

    CompletionStage<Boolean> deleteTest(int testId);

    CompletionStage<Optional<LessonPage>> getTest(int id);
}
