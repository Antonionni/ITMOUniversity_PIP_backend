package controllers.api;

import enumerations.ErrorCode;
import models.ApiResponse;
import models.entities.PassageEntityPK;
import models.serviceEntities.PassageItem;
import models.serviceEntities.PassageItemsWrapper;
import models.serviceEntities.VerifyPassageItemModel;
import play.libs.Json;
import play.mvc.Result;
import services.ICourseService;
import services.ILessonService;
import services.IPassageService;

import javax.inject.Inject;
import java.util.Date;
import java.util.concurrent.CompletionStage;

public class PassageController extends BaseController {

    private final ICourseService CourseService;
    private final ILessonService LessonService;
    private final IPassageService PassageService;

    @Inject
    public PassageController(ICourseService courseService, ILessonService lessonService, IPassageService passageService) {
        CourseService = courseService;
        LessonService = lessonService;
        PassageService = passageService;
    }

    public CompletionStage<Result> startPassage(int testId) {
            return PassageService.startPassage(testId).thenApplyAsync((page) -> ok(Json.toJson(new ApiResponse<>(page))));
    }

    public CompletionStage<Result> submitPassageItems() {
        PassageItemsWrapper passageItemsWrapper = getModelFromJson(PassageItemsWrapper.class);
        return PassageService.savePassageItem(passageItemsWrapper.getPassageItems()).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> verifyPassageItem() {
        VerifyPassageItemModel passageItem = getModelFromJson(VerifyPassageItemModel.class);
        return PassageService.verifyPassageItem(passageItem).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> listNeedToVerifiedPassages(int passageId) {
        return PassageService.listNeedToVerifiedPassageItems(passageId).thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }
}
