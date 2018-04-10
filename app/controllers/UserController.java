package controllers;

import akka.stream.javadsl.Source;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import play.Logger;
import play.libs.EventSource;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

public class UserController extends Controller {

    @NotNull private static final Gson sGson = new GsonBuilder().serializeNulls().create();

    @NotNull private final UserInteractor mInteractor;

    @Inject
    public UserController(@NotNull UserInteractor interactor) {
        mInteractor = interactor;
    }

    public Result create() {
        User newUser = sGson.fromJson(request().body().asJson().toString(), User.class);
        Logger.debug(newUser.toString());
        final Source<EventSource.Event, ?> responseSource = mInteractor.createNewUser(newUser)
                .map(sGson::toJson)
                .map(EventSource.Event::event);
        return ok().chunked(responseSource.via(EventSource.flow())).as(Http.MimeTypes.JSON);

    }

}
