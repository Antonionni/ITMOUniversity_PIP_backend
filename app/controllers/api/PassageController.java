package controllers.api;

import play.mvc.Result;
import services.ICourseService;
import services.ILessonService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class PassageController extends BaseController {

    private final ICourseService CourseService;
    private final ILessonService LessonService;

    @Inject
    public PassageController(ICourseService courseService, ILessonService lessonService) {
        CourseService = courseService;
        LessonService = lessonService;
    }

    /*public CompletionStage<Result> startPassage(int testId) {

    }
    public CompletionStage<> submitTest(int testId) {
        LessonInfo lesson = getModelFromJson(LessonInfo.class);
    }*/
}
