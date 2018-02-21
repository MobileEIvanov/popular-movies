package com.popularmovies.data.models;

/**
 * Created by emilivanov on 3/20/17.
 */

public class ErrorParser {

    String error;
    String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorParser{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
