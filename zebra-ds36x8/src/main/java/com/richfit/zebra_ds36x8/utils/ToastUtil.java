package com.richfit.zebra_ds36x8.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 土司通知工具类
 * <p>
 * Created by javakam on 2016/7/5.
 */
public class ToastUtil {
    private static Toast toast;

    public static void shortToast(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void longToast(Context context, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

}