package controllers.api;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import config.RolesConst;
import enumerations.ErrorCode;
import enumerations.RoleType;
import models.ApiResponse;
import models.serviceEntities.UserData.AggregatedUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.IUserService;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class UserController extends Controller {

    private final IUserService userDao;

    @Inject
    public UserController(IUserService userService) {
        this.userDao = userService;
    }

    @Restrict(@Group(RolesConst.AuthenticatedUser))
    public CompletionStage<Result> update() {
        AggregatedUser user = Json.fromJson(request().body().asJson(), AggregatedUser.class);
        return this.userDao.update(user).thenApplyAsync(x -> x
                ? ok(Json.toJson(new ApiResponse<Boolean>(true)))
                : badRequest(Json.toJson(new ApiResponse<Boolean>(ErrorCode.EntityNotFound))));
    }

    public CompletionStage<Result> getStudent(int id) {
        return this.userDao.getStudent(id)
                .thenApplyAsync(optionalStudent ->
                        optionalStudent
                                .map(x -> ok(Json.toJson(new ApiResponse<>(x))))
                                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> getTeacher(int id) {
        return this.userDao.getTeacher(id)
                .thenApplyAsync(optionalStudent ->
                        optionalStudent
                                .map(x -> ok(Json.toJson(new ApiResponse<>(x))))
                                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> getUser(int id, String role) {
        return getUserWithSeveralRoles(id, role);
    }

    public CompletionStage<Result> getUserWithSeveralRoles(int id, String roles) {
        Collection<RoleType> parsedRoles = Arrays
                .stream(roles.split(","))
                .filter(x -> !x.isEmpty())
                .map(RoleType::valueOf)
                .collect(Collectors.toList());
        return this.userDao.getUserAndGatherDataForRoles(id, parsedRoles)
                .thenApplyAsync(optionalStudent ->
                        optionalStudent
                                .map(x -> ok((Json.toJson(new ApiResponse<>(x)))))
                                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> getProfile() {
        return this.userDao.getProfileData().thenApplyAsync(profile ->
                profile
                        .map(x -> ok((Json.toJson(new ApiResponse<>(x)))))
                        .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    /*Teacher getTeacher(int id) {

    }

    Admin getAdmin(int id) {

    }*/

}
