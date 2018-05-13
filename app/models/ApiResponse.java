package models;

import org.jetbrains.annotations.Nullable;

public class ApiResponse<T> {

    @Nullable
    private final Integer mErrorCode;

    @Nullable
    private final T mData;

    public ApiResponse(@Nullable Integer errorCode, @Nullable T data) {
        mErrorCode = errorCode;
        mData = data;
    }

    @Nullable
    public Integer getErrorCode() {
        return mErrorCode;
    }

    @Nullable
    public T getData() {
        return mData;
    }
}
