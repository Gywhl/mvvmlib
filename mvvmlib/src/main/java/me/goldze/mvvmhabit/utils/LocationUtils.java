package me.goldze.mvvmhabit.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Description：位置信息工具类
 *
 * @author Gej
 * @date 2025/2/12
 */
public class LocationUtils {

    /**
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */
    public static Address getLocationInfo(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            // 使用经纬度进行反向地理编码
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // 获取详细位置信息
                // 国家
                String country = address.getCountryName();
                // 省
                String province = address.getAdminArea();
                // 市
                String city = address.getLocality();
                // 区
                String district = address.getSubLocality();
                // 详细地址
                String addressLine = address.getAddressLine(0);
                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */
    public static Address getLocationInfo(Context context, String latitude, String longitude) {
        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
            return null;
        }
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            // 使用经纬度进行反向地理编码
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // 获取详细位置信息
                // 国家
                String country = address.getCountryName();
                // 省
                String province = address.getAdminArea();
                // 市
                String city = address.getLocality();
                // 区
                String district = address.getSubLocality();
                // 详细地址
                String addressLine = address.getAddressLine(0);
                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
