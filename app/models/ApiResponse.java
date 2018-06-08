package models;

import org.jetbrains.annotations.Nullable;

public class ApiResponse<T> {

    @Nullable
    private final Integer ErrorCode;

    @Nullable
    private final T Data;

    private final String returnUrl;

    public ApiResponse(@Nullable Integer errorCode, @Nullable T data, @Nullable String returnUrl) {
        this.ErrorCode = errorCode;
        this.Data = data;
        this.returnUrl = returnUrl;
    }

    public String getReturnUrl() { return returnUrl; }

    @Nullable
    public Integer getErrorCode() {
        return ErrorCode;
    }

    @Nullable
    public T getData() {
        return Data;
    }
}
