package me.goldze.mvvmhabit.utils;

/**
 * Description：WGS84坐标系/GPS坐标系/大地坐标系转高德坐标系
 *
 * @author Gej
 * @date 2023/12/22
 */
public class WGS84ToAMap {
    final static double PI = 3.14159265358979324;

    /**
     * a = 6378245.0, 1/f = 298.3
     * b = a * (1 - f)
     * ee = (a^2 - b^2) / a^2;
     */
    final static double A = 6378245.0;
    final static double EE = 0.00669342162296594323;
    /**
     * PI
     */
    final static double DEF_PI = 3.14159265359;
    /**
     * 2*PI
     */
    final static double DEF_2PI = 6.28318530712;
    /**
     * PI/180.0
     */
    final static double DEF_PI180 = 0.01745329252;
    /**
     * 地球半径
     */
    final static double DEF_R = 6370693.5;

    /**
     * WGS84坐标系/GPS坐标系/大地坐标系转高德坐标系
     * @param wgLat 经度
     * @param wgLon 纬度
     * @return
     */
    public static double[] transform(double wgLat, double wgLon) {
        double mgLat;
        double mgLon;
        if (outOfChina(wgLat, wgLon)) {
            mgLat = wgLat;
            mgLon = wgLon;

        } else {
            double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
            double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
            double radLat = wgLat / 180.0 * PI;
            double magic = Math.sin(radLat);
            magic = 1 - EE * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);
            dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * PI);
            mgLat = wgLat + dLat;
            mgLon = wgLon + dLon;
        }
        return new double[]{mgLat, mgLon};
    }

    /**
     * 是否不在中国
     * @param lat 经度
     * @param lon 纬度
     * @return
     */
    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        return lat < 0.8293 || lat > 55.8271;
    }

    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 获取两点之间距离
     * @param lon1 纬度1
     * @param lat1 经度1
     * @param lon2 纬度2
     * @param lat2 经度2
     * @return
     */
    public static double getShortDistance(double lon1, double lat1, double lon2, double lat2) {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI) {
            dew = DEF_2PI - dew;
        } else if (dew < -DEF_PI) {
            dew = DEF_2PI + dew;
        }
        //东西方向长度(在纬度圈上的投影长度)
        dx = DEF_R * Math.cos(ns1) * dew;
        // 南北方向长度(在经度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2);
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }
}
