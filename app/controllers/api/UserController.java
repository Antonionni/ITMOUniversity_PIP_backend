package controllers.api;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import config.RolesConst;
import enumerations.ErrorCode;
import enumerations.RoleType;
import models.ApiResponse;
import models.serviceEntities.UserData.AggregatedUser;
import play.libs.Json;
import play.mvc.Result;
import services.IUserService;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class UserController extends BaseController {

    private final IUserService UserService;

    @Inject
    public UserController(IUserService userService) {
        this.UserService = userService;
    }

    @Restrict(@Group(RolesConst.AuthenticatedUser))
    public CompletionStage<Result> update() {
        AggregatedUser user = getModelFromJson(AggregatedUser.class);
        return this.UserService.update(user).thenApplyAsync(x -> x
                ? ok(Json.toJson(new ApiResponse<Boolean>(true)))
                : badRequest(Json.toJson(new ApiResponse<Boolean>(ErrorCode.EntityNotFound))));
    }

    @Restrict(@Group({RolesConst.ApprovedTeacher, RolesConst.Admin}))
    public CompletionStage<Result> getStudent(int id) {
        return this.UserService.getStudent(id)
                .thenApplyAsync(optionalStudent ->
                        optionalStudent
                                .map(x -> ok(Json.toJson(new ApiResponse<>(x))))
                                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    @Restrict(@Group(RolesConst.AuthenticatedUser))
    public CompletionStage<Result> getTeacher(int id) {
        return this.UserService.getTeacher(id)
                .thenApplyAsync(optionalStudent ->
                        optionalStudent
                                .map(x -> ok(Json.toJson(new ApiResponse<>(x))))
                                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    @Restrict(@Group(RolesConst.Admin))
    public CompletionStage<Result> getUser(int id, String role) {
        return getUserWithSeveralRoles(id, role);
    }

    @Restrict(@Group(RolesConst.Admin))
    public CompletionStage<Result> getUserWithSeveralRoles(int id, String roles) {
        Collection<RoleType> parsedRoles = Arrays
                .stream(roles.split(","))
                .filter(x -> !x.isEmpty())
                .map(RoleType::valueOf)
                .collect(Collectors.toList());
        return this.UserService.getUserAndGatherDataForRoles(id, parsedRoles)
                .thenApplyAsync(optionalStudent ->
                        optionalStudent
                                .map(x -> ok((Json.toJson(new ApiResponse<>(x)))))
                                .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    @Restrict(@Group(RolesConst.AuthenticatedUser))
    public CompletionStage<Result> getProfile() {
        return this.UserService.getProfileData().thenApplyAsync(profile ->
                profile
                        .map(x -> ok((Json.toJson(new ApiResponse<>(x)))))
                        .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> getFullUser(int id) {
        return this.UserService.getUserAndGatherDataForRoles(id, Arrays.asList(RoleType.values()))
                .thenApplyAsync(x -> x
                        .map(user -> ok(Json.toJson(new ApiResponse<>(user))))
                        .orElseGet(() -> notFound(Json.toJson(new ApiResponse<>(ErrorCode.EntityNotFound)))));
    }

    public CompletionStage<Result> getUsersByRole(String role) {
        RoleType parsedRole = RoleType.valueOf(role);
        return getUserList(parsedRole);
    }

    public CompletionStage<Result> getAllUsers() {
        return this.UserService.getUsersList()
                .thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }

    public CompletionStage<Result> getRequestForTeacherUserList() {
        return getUserList(RoleType.Teacher);
    }

    private CompletionStage<Result> getUserList(RoleType parsedRole) {
        return this.UserService.getUsersList(parsedRole)
                .thenApplyAsync(x -> ok(Json.toJson(new ApiResponse<>(x))));
    }
}
