package modules;

import Exceptions.UnauthorizedAccessException;
import enumerations.ErrorCode;
import models.ApiResponse;
import play.http.HttpErrorHandler;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Singleton;

@Singleton
public class ControllerErrorHandler implements HttpErrorHandler {
    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        return CompletableFuture.completedFuture(
                Results.status(statusCode, "A client error occurred: " + message)
        );
    }

    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        if(exception instanceof UnauthorizedAccessException) {
            return CompletableFuture.completedFuture(Results.unauthorized(Json.toJson(new ApiResponse<>(ErrorCode.UnauthorizedAccess))));
        }
        return CompletableFuture.completedFuture(
                Results.internalServerError("A server error occurred: " + exception.getMessage())
        );
    }
}