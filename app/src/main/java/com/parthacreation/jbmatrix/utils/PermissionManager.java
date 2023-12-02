package com.parthacreation.jbmatrix.utils;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionManager {

    public static final int PERMISSION_REQUEST_CODE = 100;

    public static boolean checkPermission(Activity activity) {
        if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    activity.requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE,Manifest.permission.READ_MEDIA_VIDEO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
            return false;
        } else return true;
    }

    // Implement this method in your Activity to handle permission request results
    public static boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults,Activity activity) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                // Handle further operations or start the download process
                return true;
            } else {
                // Permission is denied
                // You can show a message to the user indicating that permission is required for downloading videos
                return false;
            }
        }
        return false;
    }
}

