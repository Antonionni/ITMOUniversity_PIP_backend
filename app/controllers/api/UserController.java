package controllers.api;

import play.mvc.Controller;
import play.mvc.Result;
import services.IUserDAO;

import javax.inject.Inject;

public class UserController extends Controller {

    private final IUserDAO userDao;

    @Inject
    public UserController(IUserDAO userDAO) {
        this.userDao = userDAO;
    }

   /* public Result User() {

    }*/
}
