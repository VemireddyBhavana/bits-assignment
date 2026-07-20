/*
 * Student ID: 2024EB01570
 * Course: Programming in Mobile Devices - Staff Graded Assignment 2
 * Java Helper for Internet Connectivity Checking
 */
package com.campus.noticeboard.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.widget.Toast;

public class NetworkHelper {

    /**
     * Checks if active internet connectivity is available on the device.
     * @param context Application/Activity Context
     * @return boolean true if connected, false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.net.Network activeNetwork = cm.getActiveNetwork();
            if (activeNetwork == null) return false;
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                     capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                     capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            @SuppressWarning("deprecation")
            android.net.NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
    }

    /**
     * Helper method to validate connection and show the exact specified Toast message if offline.
     */
    public static boolean checkNetworkAndShowToast(Context context) {
        if (!isNetworkAvailable(context)) {
            Toast.makeText(context, "Please connect to the internet.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
