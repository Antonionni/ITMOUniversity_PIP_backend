package models;

import enumerations.ErrorCode;
import org.jetbrains.annotations.Nullable;

public class ApiResponse<T> {

    @Nullable
    private ErrorCode ErrorCode = null;

    @Nullable
    private String ErrorMessage = null;

    @Nullable
    private T Data = null;

    @Nullable
    private String ReturnUrl = null;

    public ApiResponse(@Nullable ErrorCode errorCode, @Nullable String errorMessage, @Nullable T data, @Nullable String returnUrl) {
        this.ErrorCode = errorCode;
        this.Data = data;
        this.ReturnUrl = returnUrl;
        this.ErrorMessage = errorMessage;
    }

    public ApiResponse(ErrorCode errorCode) {
        this.ErrorCode = errorCode;
    }

    public static ApiResponse<String> withReturnUrl(String returnUrl) {
        return new ApiResponse<>(null, null, null, returnUrl);
    }

    public ApiResponse(ErrorCode errorCode, String errorMessage) {
        this.ErrorCode = errorCode;
        this.ErrorMessage = errorMessage;
    }

    public ApiResponse(T data) {
        this.Data = data;
    }

    @Nullable
    public String getReturnUrl() {
        return ReturnUrl;
    }

    @Nullable
    public ErrorCode getErrorCode() {
        return ErrorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    @Nullable
    public T getData() {
        return Data;
    }
}
