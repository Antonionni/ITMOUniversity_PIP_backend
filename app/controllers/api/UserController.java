package controllers.api;

import enumerations.RoleType;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.IUserDAO;

import javax.inject.Inject;
import javax.management.relation.Role;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class UserController extends Controller {

    private final IUserDAO userDao;

    @Inject
    public UserController(IUserDAO userDAO) {
        this.userDao = userDAO;
    }

    public CompletionStage<Result> getStudent(int id) {
        return this.userDao.getStudent(id)
                .thenApplyAsync(optionalStudent ->
                        optionalStudent
                                .map(x -> ok(Json.toJson(x)))
                                .orElseGet(Results::notFound));
    }

    public CompletionStage<Result> getTeacher(int id) {
        return this.userDao.getTeacher(id)
                .thenApplyAsync(optionalStudent ->
                        optionalStudent
                                .map(x -> ok(Json.toJson(x)))
                                .orElseGet(Results::notFound));
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
                                .map(x -> ok(Json.toJson(x)))
                                .orElseGet(Results::notFound));
    }

    /*Teacher getTeacher(int id) {

    }

    Admin getAdmin(int id) {

    }*/

}
