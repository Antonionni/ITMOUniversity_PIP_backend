package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class MainController extends Controller {

    public Result main() {
        return ok(views.html.main.render("Hello world"));
    }

}
