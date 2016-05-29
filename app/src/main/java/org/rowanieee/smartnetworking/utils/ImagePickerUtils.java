package org.rowanieee.smartnetworking.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Nick Felker on 5/29/2016.
 */
public class ImagePickerUtils {
    public static final int REQUEST_CODE = 800;

    /** The max profile image width */
    private static final int MAX_WIDTH = 100;
    /** The max profile image height */
    private static final int MAX_HEIGHT = 100;

    public static void openImagePicker(Activity mActivity) {
        openSafWithFallback(mActivity);
    }

    public static void interpretActivityResult(Activity mActivity, int requestCode, int resultCode, Intent data, ImagePickerListener listener) {
        //Check if we have request code. If so, try to grab a URI from the Intent
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        Uri documentUri = data.getData();
                        //Get bitmap of Uri
                        ParcelFileDescriptor parcelFileDescriptor =
                                null;
                        try {
                            parcelFileDescriptor = mActivity.getContentResolver().openFileDescriptor(documentUri, "r");
                            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                            parcelFileDescriptor.close();

                            Bitmap smallerBitmap = scaleBitmap(image);

                            //Now get a base64 of that Bitmap
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            smallerBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream .toByteArray();
                            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            listener.onImageSelected(encoded);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }

    private static Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            int ratio = width / MAX_WIDTH;
            width = MAX_WIDTH;
            height = height / ratio;
        } else if (height > width) {
            // portrait
            int ratio = height / MAX_HEIGHT;
            height = MAX_HEIGHT;
            width = width / ratio;
        } else {
            // square
            height = MAX_HEIGHT;
            width = MAX_WIDTH;
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
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

    public static Bitmap getBitmapFromBase64(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public interface ImagePickerListener {
        void onImageSelected(String userPhotoBase64);
    }
}
