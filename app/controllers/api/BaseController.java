package controllers.api;

import play.libs.Json;
import play.mvc.Controller;

public class BaseController extends Controller {
    protected <T> T getModelFromJson(Class<T> clazz) {
        return Json.fromJson(request().body().asJson(), clazz);
    }
}
