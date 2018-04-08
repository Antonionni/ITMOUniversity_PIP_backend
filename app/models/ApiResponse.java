package models;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

public class ApiResponse<T> {

    @Nullable
    @SerializedName("error_code")
    private final Integer mErrorCode;

    @Nullable
    @SerializedName("data")
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
