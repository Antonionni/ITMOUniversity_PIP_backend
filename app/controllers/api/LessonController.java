package controllers.api;

import enumerations.ErrorCode;
import models.ApiResponse;
import models.serviceEntities.LessonInfo;
import models.serviceEntities.LessonPage;
import models.serviceEntities.Question;
import play.libs.Json;
import play.mvc.Result;
import services.ICourseService;
import services.ILessonService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class LessonController extends BaseController {

    private final ICourseService CourseService;
    private final ILessonService LessonService;

    @Inject
    public LessonController(ICourseService courseService, ILessonService lessonService) {
        CourseService = courseService;
        LessonService = lessonService;
    }

    public CompletionStage<Result> get(int id) {
        return LessonService.get(id).thenApplyAsync(x -> x
                .map((lesson) -> ok(Json.toJson(new ApiResponse<>(lesson))))
                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> create(int courseId) {
        LessonInfo lesson = getModelFromJson(LessonInfo.class);
        return LessonService.create(lesson, courseId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> update(int courseId) {
        LessonInfo lesson = getModelFromJson(LessonInfo.class);
        return LessonService.update(lesson, courseId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> delete(int lessonId) {
        return LessonService.delete(lessonId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> getMaterial(int id) {
        return LessonService.getMaterial(id).thenApplyAsync(x -> x
                .map((page) -> ok(Json.toJson(new ApiResponse<>(page))))
                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> createMaterial(int lessonId) {
        LessonPage page = getModelFromJson(LessonPage.class);
        return LessonService.createMaterial(page, lessonId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> updateMaterial(int lessonId) {
        LessonPage page = getModelFromJson(LessonPage.class);
        return LessonService.updateMaterial(page, lessonId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> deleteMaterial(int materialId) {
        return LessonService.deleteMaterial(materialId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> getTest(int id) {
        return LessonService.getTest(id).thenApplyAsync(x -> x
                .map((page) -> ok(Json.toJson(new ApiResponse<>(page))))
                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> createTest(int lessonId) {
        LessonPage page = getModelFromJson(LessonPage.class);
        return LessonService.createTest(page, lessonId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> updateTest(int lessonId) {
        LessonPage page = getModelFromJson(LessonPage.class);
        return LessonService.updateTest(page, lessonId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> deleteTest(int testId) {
        return LessonService.deleteTest(testId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> createQuestion(int testId) {
        Question question = getModelFromJson(Question.class);
        return LessonService.createQuestion(question,testId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> updateQuestion(int testId) {
        Question question = getModelFromJson(Question.class);
        return LessonService.updateQuestion(question, testId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> deleteQuestion(int questionId) {
        return LessonService.deleteQuestion(questionId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }
}
