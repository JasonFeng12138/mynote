package com.example.feng.alarm;

import android.net.Uri;
import android.util.Log;

/**
 * Created by feng on 16/6/30.
 */
public class Musicuri {
    private static Uri uri;

    public static Uri getUri() {
        return uri;
    }

    public static void setUri(Uri uri) {
        Musicuri.uri = uri;

    }
}