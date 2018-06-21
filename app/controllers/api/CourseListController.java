package controllers.api;

import config.ApiConst;
import play.libs.Json;
import play.mvc.Result;
import services.ICourseListService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class CourseListController extends BaseController {

    private final ICourseListService CourseListService;

    @Inject
    public CourseListController(ICourseListService courseListService) {
        CourseListService = courseListService;
    }

    public CompletionStage<Result> listRandomCourses(int size) {
        return CourseListService.listRandomCourses(size).thenApplyAsync(x -> ok(Json.toJson(x)));
    }

    public CompletionStage<Result> listAllCourses() {
        return CourseListService.listAllCourses().thenApplyAsync(x -> ok(Json.toJson(x)));
    }

    public CompletionStage<Result> findCourses(String keyword) {
        return CourseListService.findCourses(keyword).thenApplyAsync(x -> ok(Json.toJson(x)));
    }

    public CompletionStage<Result> subscribeToCourse(int id) {
        return CourseListService.subscribeToCourse(id).thenApplyAsync(x -> ok(Json.toJson(x)));
    }
}
