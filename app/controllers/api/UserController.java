package controllers.api;

import akka.stream.*;
import akka.stream.javadsl.*;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.util.ByteString;

import models.serviceEntities.Admin;
import models.serviceEntities.Student;
import models.serviceEntities.Teacher;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.IUserDAO;

import javax.inject.Inject;
import java.beans.Transient;
import java.util.concurrent.CompletionStage;

public class UserController extends Controller {

    private final IUserDAO userDao;

    @Inject
    public UserController(IUserDAO userDAO) {
        this.userDao = userDAO;
    }

    public CompletionStage<Result> getStudent(int id) {
        return this.userDao.getStudent(id).thenApplyAsync(student -> ok(Json.toJson(student)));
    }

    /*Teacher getTeacher(int id) {

    }

    Admin getAdmin(int id) {

    }*/

}
