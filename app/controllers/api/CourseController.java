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

    @Inject
    public CourseController(ICourseService courseService) {
        CourseService = courseService;
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
}
