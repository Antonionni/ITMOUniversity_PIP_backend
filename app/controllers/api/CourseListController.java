package controllers.api;

import config.ApiConst;
import models.serviceEntities.Course;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.CourseListService;
import services.ICourseListService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public class CourseListController extends Controller {

    private final ICourseListService CourseListService;

    @Inject
    public CourseListController(ICourseListService courseListService) {
        CourseListService = courseListService;
    }

    public CompletionStage<Result> listRandomCourses(int size) {
        return CourseListService.listRandomCourses(size).thenApplyAsync(x -> ok(Json.toJson(x)));
    }

    public CompletionStage<Result> findCourses(String keyword, int pageNumber) {
        return CourseListService.findCourses(keyword, pageNumber, ApiConst.DEFAULT_PAGE_SIZE).thenApplyAsync(x -> ok(Json.toJson(x)));
    }

    public CompletionStage<Result> subscribeToCourse(int courseId) {
        return CourseListService.subscribeToCourse(courseId).thenApplyAsync(x -> ok(Json.toJson(x)));
    }


}
