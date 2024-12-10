package com.kalisat.edulearn.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ImageUtil {

    public static String compressImageAndConvertToBase64(Context context, Uri imageUri) {
        try {
            ContentResolver resolver = context.getContentResolver();
            InputStream inputStream = resolver.openInputStream(imageUri);
            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);

            int maxWidth = 450;
            int maxHeight = 450;
            int width = originalBitmap.getWidth();
            int height = originalBitmap.getHeight();
            float scaleFactor = Math.min((float) maxWidth / width, (float) maxHeight / height);

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap,
                    (int) (width * scaleFactor),
                    (int) (height * scaleFactor),
                    false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);

            byte[] byteArray = baos.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
