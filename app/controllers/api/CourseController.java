package controllers.api;

import enumerations.ErrorCode;
import models.ApiResponse;
import models.serviceEntities.Course;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.CourseService;
import services.ICourseService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class CourseController extends BaseController {
    private final ICourseService CourseService;

    @Inject
    public CourseController(ICourseService courseService) {
        CourseService = courseService;
    }

    public CompletionStage<Result> get(int id) {
        return CourseService.get(id).thenApplyAsync((x) -> x
                .map((course) -> ok(Json.toJson(new ApiResponse<>(course))))
                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> create() {
        Course course = getModelFromJson(Course.class);
        return CourseService.create(course).thenApplyAsync((x) -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> update() {
        Course course = getModelFromJson(Course.class);
        return CourseService.update(course).thenApplyAsync((x) -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> delete(int id) {
        return CourseService.delete(id).thenApplyAsync((x) -> ok(Json.toJson(new ApiResponse<>(x))));
    }
}
