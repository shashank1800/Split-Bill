package com.common.util;


import lombok.Data;

@Data
public class Valid<T> {
    private T value;
    private boolean isFailed = false;
    private String message;

    public Valid(T t) {
        this.value = t;
    }

    public Valid(boolean isFailed, String message) {
        this.isFailed = isFailed;
    }

    public static <T> Valid<T> success(T t) {
        return new Valid<>(t);
    }

    public static <T> Valid<T> fail(String message) {
        return new Valid<>(true, message);
    }

}
