package controllers.api;

import enumerations.ErrorCode;
import models.ApiResponse;
import models.serviceEntities.CourseInfo;
import models.serviceEntities.CoursePeriod;
import models.serviceEntities.LessonInfo;
import play.libs.Json;
import play.mvc.Result;
import services.ICourseService;
import services.ILessonService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class CourseController extends BaseController {
    private final ICourseService CourseService;
    private final ILessonService LessonService;

    @Inject
    public CourseController(ICourseService courseService, ILessonService lessonService) {
        CourseService = courseService;
        LessonService = lessonService;
    }

    public CompletionStage<Result> get(int id) {
        return CourseService.get(id).thenApplyAsync(x -> x
                .map((course) -> ok(Json.toJson(new ApiResponse<>(course))))
                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> create() {
        CourseInfo courseInfo = getModelFromJson(CourseInfo.class);
        return CourseService.create(courseInfo).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> update() {
        CourseInfo courseInfo = getModelFromJson(CourseInfo.class);
        return CourseService.update(courseInfo).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> delete(int id) {
        return CourseService.delete(id).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> createCoursePeriod(int courseId) {
        CoursePeriod coursePeriod = getModelFromJson(CoursePeriod.class);
        return CourseService.createPeriod(coursePeriod, courseId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> updateCoursePeriod(int courseId) {
        CoursePeriod coursePeriod = getModelFromJson(CoursePeriod.class);
        return CourseService.updatePeriod(coursePeriod, courseId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> deleteCoursePeriod(int coursePeriodId) {
        return CourseService.deletePeriod(coursePeriodId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> getLesson(int id) {
        return LessonService.get(id).thenApplyAsync(x -> x
                .map((lesson) -> ok(Json.toJson(new ApiResponse<>(lesson))))
                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> createLesson(int courseId) {
        LessonInfo lesson = getModelFromJson(LessonInfo.class);
        return LessonService.create(lesson, courseId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> updateLesson(int courseId) {
        LessonInfo lesson = getModelFromJson(LessonInfo.class);
        return LessonService.update(lesson, courseId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> deleteLesson(int lessonId) {
        return LessonService.delete(lessonId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }
}
