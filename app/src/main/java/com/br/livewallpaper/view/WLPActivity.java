package com.br.livewallpaper.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by rafaelneiva on 09/05/17.
 */

public class WLPActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 1001;
    private RequestListener mRequestCallback;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * @param permission Need Manifest.permission
     * @return true if permission is granted, false otherwise
     */
    public boolean verifyPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     *
     Example
        if (!verifyPermission(Manifest.permission.CAMERA)) {
            requestPermission(Manifest.permission.CAMERA, new RequestListener() {
                @Override
                public void onRequestCallback(boolean granted) {
                    Toast.makeText(MainActivity.this, granted ? "Deu" : "Deu não", Toast.LENGTH_SHORT).show();
                }
        });
     }
     *
     * @param permission Permission to request
     * @param callback @RequestListener to provide callback for request
     */
    public void requestPermission(String permission, RequestListener callback) {
        this.mRequestCallback = callback;
        ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mRequestCallback.onRequestCallback(true);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    mRequestCallback.onRequestCallback(false);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public interface RequestListener {
        void onRequestCallback(boolean granted);
    }

}
