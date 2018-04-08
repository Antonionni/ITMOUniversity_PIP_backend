package domain;

import akka.NotUsed;
import akka.japi.pf.PFBuilder;
import akka.stream.javadsl.Source;
import data.UserRepository;
import models.User;
import models.ApiResponse;
import org.jetbrains.annotations.NotNull;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;

@Singleton
public class UserInteractor {

    @NotNull private UserRepository mRepository;

    @Inject
    public UserInteractor(@NotNull UserRepository repository) {
        mRepository = repository;
    }

    public Source<ApiResponse<Void>, NotUsed> createNewUser(@NotNull User newUser) {
        return mRepository.createNewUser(newUser)
                .async()
                .map(ignore -> new ApiResponse<Void>(null, null))
                .recover(new PFBuilder<Throwable, ApiResponse<Void>>()
                        .match(Throwable.class, this::getApiResponseOnError)
                        .build());
    }

    private ApiResponse<Void> getApiResponseOnError(@NotNull Throwable e) {
        String message = e.getMessage();
        Logger.error(e.getMessage());
        Logger.error(Arrays.toString(e.getStackTrace()));

        int errorCode;
        if (message.contains(User.COLUMN_LOGIN)) {
            errorCode = ErrorCode.DUPLICATE_LOGIN;
        } else if (message.contains(User.COLUMN_EMAIL)) {
            errorCode = ErrorCode.DUPLICATE_EMAIL;
        } else {
            errorCode = ErrorCode.UNKNOWN;
        }

        return new ApiResponse<>(errorCode, null);
    }

}
