package me.goldze.mvvmhabit.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceInfoUtils {
    public static String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            }
        return "20231123";
//        return "20231109001";
//            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
