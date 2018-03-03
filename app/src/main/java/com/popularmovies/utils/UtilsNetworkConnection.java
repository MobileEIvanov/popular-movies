package com.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Emil Ivanov on 5/7/2017.
 *
 * The solution is based on this post
 * https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
 */

public class UtilsNetworkConnection {

    private final Context _context;
    private static boolean HAS_CONNECTION = false;

    private UtilsNetworkConnection(Context context){
        this._context = context;
    }

    private boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        HAS_CONNECTION = true;
                        return true;
                    }
                }
            }
        }
        HAS_CONNECTION = false;
        return false;
    }


    public static boolean checkInternetConnection(Context context){
        boolean connection;

        UtilsNetworkConnection cd = new UtilsNetworkConnection(context);
        connection = cd.isConnectingToInternet();

        return  connection;
    }

}
