package org.rowanieee.smartnetworking.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;

/**
 * Created by Nick Felker on 5/29/2016.
 */
public class ImagePickerUtils {
    public static final int REQUEST_CODE = 800;

    public static void openImagePicker(Activity mActivity) {
        openSafWithFallback(mActivity);
    }

    public static void interpretActivityResult(int requestCode, int resultCode, Intent data, ImagePickerListener listener) {
        //Check if we have request code. If so, try to grab a URI from the Intent
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void openSafUI(Activity mActivity) {
        Intent selectFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        selectFile.addCategory(Intent.CATEGORY_OPENABLE);
        selectFile.setType("*/*");
        mActivity.startActivityForResult(selectFile, REQUEST_CODE);
    }

    private static void openChooser(Activity mActivity) {
        final Intent selectFile = new Intent(Intent.ACTION_GET_CONTENT);
        // The MIME data type filter
        selectFile.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        selectFile.addCategory(Intent.CATEGORY_OPENABLE);
        mActivity.startActivityForResult(selectFile, REQUEST_CODE);
    }

    private static void openSafWithFallback(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            openSafUI(mActivity);
        } else {
            // Fallback to custom UI:
            openChooser(mActivity);
        }
    }

    public interface ImagePickerListener {
        void onImageSelected(String userPhotoBase64);
    }
}
