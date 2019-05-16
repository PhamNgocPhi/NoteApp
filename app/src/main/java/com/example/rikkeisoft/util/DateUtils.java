package com.example.rikkeisoft.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String partDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(date);
    }

    public static String addDate(int amount) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, amount);
        Date dateAdded = c.getTime();
        return sdf.format(dateAdded);
    }

    public static long parseDateToMilisecond(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date mDate = sdf.parse(dateString);
            return mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeByPattern(String pattern) {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        String time = sdf.format(c.getTime());
        return time;
    }

    //fixme method này sẽ đặt trong class khác, class util sẽ chỉ chứa những hàm liên quan đến date
    // có thể tạo thêm class commonUtils rồi đưa hàm này vào
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }
}
