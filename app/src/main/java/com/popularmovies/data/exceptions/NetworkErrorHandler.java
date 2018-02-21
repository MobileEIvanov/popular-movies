package com.popularmovies.data.exceptions;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.popularmovies.data.models.ErrorParser;
import com.popularmovies.data.models.ErrorResponseObj;


import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;


public class NetworkErrorHandler {

    private static final String TAG ="NetworkError";
    private static final String ERROR_HTTP_EXCEPTION = "HttpException";
    private static final String ERROR_CONNECTION_EXCEPTION = "ConnectException";
    private static final String ERROR_SOCKET_TIMEOUT_EXCEPTION = "SocketTimeoutException";
    private static final String ERROR_IO_EXCEPTION = "IOException";

    public static String getErrorResponse(Throwable e) {



        String errorMessage = "Connection issue. Please try again later.";
        if (e instanceof HttpException) {



            Log.d(TAG, "HTTP Exception " + ((HttpException) e).code());
        } else if (e instanceof ConnectException) {
            Log.d(TAG, "ConnectException  ");

        } else if (e instanceof SocketTimeoutException) {
            Log.d(TAG, "SocketTimeoutException  ");


        } else if (e instanceof IOException) {

            Log.d(TAG, "IOException  ");
        }


        return errorMessage;
    }



    private static ErrorResponseObj getErrorObject(Throwable e) {



        ErrorResponseObj errorResponseObj = new ErrorResponseObj();
        if(e instanceof HttpException) {
            int serverCode = ((HttpException) e).code();
            ResponseBody body = ((HttpException) e).response().errorBody();
            Gson gson = new Gson();

            TypeAdapter<ErrorParser> adapter = gson.getAdapter
                    (ErrorParser
                            .class);
            ErrorParser errorParser = null;
            try {
                errorParser = adapter.fromJson(body.string());
                Log.d(TAG, "Error: " + errorParser.toString());

            } catch (IOException exs) {
                e.printStackTrace();
            }

            if(errorParser!=null) {
                if (errorParser.getMessage() != null) {
                    errorResponseObj.setApiErrorMessage(errorParser.getMessage());
                }
            }
            errorResponseObj.setErrorType(ERROR_HTTP_EXCEPTION);
            errorResponseObj.setServerErrorCode(serverCode);

        }else {

        }

        return errorResponseObj;
    }


}
