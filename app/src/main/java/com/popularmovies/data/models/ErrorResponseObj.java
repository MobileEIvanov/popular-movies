package com.popularmovies.data.models;

/**
 * Created by emilivanov on 3/17/17.
 */

public class ErrorResponseObj {

    int serverErrorCode;
    String serverMessage;

    int apiErrorCode;
    String apiErrorMessage;
    String errorType;



    public ErrorResponseObj(){}

    public ErrorResponseObj(int serverErrorCode, String serverMessage, int apiErrorCode, String apiErrorMessage) {
        this.serverErrorCode = serverErrorCode;
        this.serverMessage = serverMessage;
        this.apiErrorCode = apiErrorCode;
        this.apiErrorMessage = apiErrorMessage;
    }

    public int getServerErrorCode() {
        return serverErrorCode;
    }

    public void setServerErrorCode(int serverErrorCode) {
        this.serverErrorCode = serverErrorCode;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public int getApiErrorCode() {
        return apiErrorCode;
    }

    public void setApiErrorCode(int apiErrorCode) {
        this.apiErrorCode = apiErrorCode;
    }

    public String getApiErrorMessage() {
        return apiErrorMessage;
    }

    public void setApiErrorMessage(String apiErrorMessage) {
        this.apiErrorMessage = apiErrorMessage;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
    @Override
    public String toString() {
        return "ErrorResponseObj{" +
                "serverErrorCode=" + serverErrorCode +
                ", serverMessage='" + serverMessage + '\'' +
                ", apiErrorCode=" + apiErrorCode +
                ", apiErrorMessage='" + apiErrorMessage + '\'' +
                '}';
    }
}
