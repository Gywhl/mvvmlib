package me.goldze.mvvmhabit.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Description：时间工具
 *
 * @author Gej
 * @date 2023/12/28
 */
public class TimeUtils {

    /**
     * 北京时间
     */
    private final static String webbjtime = "http://www.bjtime.cn";
    /**
     * 百度时间
     */
    private final static String webbaidutime = "http://www.baidu.com";

    public final static String TYPE_TO_MINUTE = "yyyy-MM-dd HH:mm";
    public final static String TYPE_TO_HOUR = "yyyy-MM-dd HH";
    public final static String TYPE_TO_DAY = "yyyy-MM-dd";
    public final static String TYPE_YEAR = "yyyy";
    public final static String TYPE_MONTH = "MM";
    public final static String TYPE_DAY = "dd";
    public final static String TYPE_MONTH_DAY = "MM.dd";
    public final static String TYPE_HOUR_TO_SECOND = "HH:mm:ss";
    public final static String TYPE_HOUR_TO_MINUTE = "HH:mm";

    public final static String TYPE_POINT_TO_SECOND = "yyyy.MM.dd HH:mm:ss";
    public final static String TYPE_POINT_TO_MINUTE = "yyyy.MM.dd HH:mm";
    public final static String TYPE_POINT_TO_HOUR = "yyyy.MM.dd HH";
    public final static String TYPE_POINT_TO_DAY = "yyyy.MM.dd";
    public final static String TYPE_CHINESE_TO_SECOND = "yyyy年MM月dd日 HH:mm:ss";

    public final static String TYPE_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public final static String TYPE_NUMBER = "yyyyMMddHHmmss";

    public final static String DAY_START = "00:00:00";
    public final static String DAY_END = "23:59:59";

    private static long webtime = 0; //当前网络时间

    public static MyHandler myHandler;

    private static Timer mTimer;
    private static TimerTask mTimerTask;

    //首先创建一个HandlerThread对象
    public static HandlerThread mHT = new HandlerThread("gettime");

    public static class MyHandler extends Handler {

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //这里是运行在工作线程中，而不是UI线程
            switch (msg.what) {
                case 1:
                    try {
                        URL url = new URL(webbaidutime);// 取得资源对象
                        URLConnection uc = url.openConnection();// 生成连接对象
                        uc.connect();// 发出连接
                        webtime = uc.getDate();// 读取网站日期时间
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    //初始化线程
    public static void initThread() {
        if (myHandler == null) {
            //随后启动线程
            mHT.start();
            myHandler = new MyHandler(mHT.getLooper());
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    //这里通过传统的Handler发送消息的方式，来触发线程
                    Message msg = Message.obtain();
                    msg.what = 1; //消息的标识
                    myHandler.sendMessage(msg);
                }
            };
            mTimer.schedule(mTimerTask, 0, 1000);
        }
        // 最后不需要用的时候，结束线程，即停止线程的消息循环
        // mHT.quit();
    }

    /**
     * 获取当前string类型时间
     *
     * @param type 时间类型
     * @return
     */
    public static String getStrCurrentTime(String type) {
        try {
            if (TextUtils.isEmpty(type)) {
                type = TYPE_DEFAULT;
            }
            return longToStringTime(webtime == 0 ? System.currentTimeMillis() : webtime, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取当前long类型时间
     *
     * @return
     */
    public static long getLongCurrentTime(String type) {
        if (TextUtils.isEmpty(type)) {
            type = TYPE_DEFAULT;
        }
        String strCurrentTime = getStrCurrentTime(type);
        return strToLongTime(strCurrentTime, type);
    }

    /**
     * 获取当前日初时间戳
     *
     * @return
     */
    public static String getCurrentTimeStart() {
        return getStrCurrentTime(TYPE_TO_DAY) + " " + DAY_START;
    }

    /**
     * 获取当前日末时间戳
     *
     * @return
     */
    public static String getCurrentTimeEnd() {
        return getStrCurrentTime(TYPE_TO_DAY) + " " + DAY_END;
    }

    /**
     * 时间string类型转long类型
     *
     * @param time       string类型时间
     * @param timeFormat time 的时间类型
     * @return
     */
    public static long strToLongTime(String time, String timeFormat) {
        try {
            if (TextUtils.isEmpty(time)) {
                return -1;
            }
            if (TextUtils.isEmpty(timeFormat)) {
                timeFormat = TYPE_DEFAULT;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
            Date parse = sdf.parse(time);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 时间long类型转string类型
     *
     * @param time       long类型时间
     * @param timeFormat string 的时间类型
     * @return
     */
    public static String longToStringTime(long time, String timeFormat) {
        try {
            if (time < 0) {
                return "";
            }
            if (TextUtils.isEmpty(timeFormat)) {
                timeFormat = TYPE_DEFAULT;
            }
            // Locale.getDefault()获取当前的语言环境
            // Locale.CHINA 中文
            // Locale.US() 英文
            Date date = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //获取周初时间戳
    public static long getTimeOfWeekStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek());
        return ca.getTimeInMillis();
    }

    //获取月初时间戳
    public static long getTimeOfMonthStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_MONTH, 1);
        return ca.getTimeInMillis();
    }

    //获取月末时间
    public static String getTimeOfMonthEnd(String timeType) {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        long timeInMillis = ca.getTimeInMillis();
        return longToStringTime(timeInMillis, timeType);
    }

    //获取月末的最后一秒时间戳
    public static long getTimeOfMonthEnd() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        long timeInMillis = ca.getTimeInMillis();
        String time = longToStringTime(timeInMillis, TYPE_TO_DAY) + " " + DAY_END;
        return strToLongTime(time, TYPE_DEFAULT);
    }

    //获取年初时间戳
    public static long getTimeOfYearStart() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.clear(Calendar.MINUTE);
        ca.clear(Calendar.SECOND);
        ca.clear(Calendar.MILLISECOND);
        ca.set(Calendar.DAY_OF_YEAR, 1);
        return ca.getTimeInMillis();
    }

    //获取前几天的零点时间
    public static long getLongOldTimeZero(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -days);//往上推一天  30推三十天  365推一年
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    //获取前几天的零点时间
    public static String getStrOldTimeZero(int days, String timeType) {
        long longOldTimeZero = getLongOldTimeZero(days);
        return longToStringTime(longOldTimeZero, timeType);
    }

    /**
     * 获取一天24小时long时间
     *
     * @return
     */
    public static long get24HourTime() {
        return 24 * 60 * 60 * 1000;
    }

    /**
     * 获取某个时间中的某一部分时间
     *
     * @param time       传入时间
     * @param timeFormat 当前时间类型
     * @return
     */
    public static Calendar getCalendar(Object time, String timeFormat) {
        Calendar ca = null;
        if (time instanceof String) {
            ca = Calendar.getInstance();
            if (TextUtils.isEmpty(timeFormat)) {
                timeFormat = TYPE_DEFAULT;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
            Date parse;
            try {
                parse = sdf.parse(String.valueOf(time));
                ca.setTime(parse);
                return ca;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (time instanceof Long) {
            ca = Calendar.getInstance();
            ca.setTimeInMillis((Long) time);
            return ca;
        }
        return ca;
    }

    /**
     * 获取两个时间差
     *
     * @param endTime
     * @param startTime
     * @return
     */
    public static String getDiffTime(long endTime, long startTime) {
        long time = endTime - startTime;
        if (time < 0) {
            return "";
        }
        // Locale.getDefault()获取当前的语言环境
        // Locale.CHINA 中文
        // Locale.US() 英文
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(TYPE_HOUR_TO_SECOND, Locale.getDefault());
        // 设置时区为 GMT+0
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return sdf.format(date);
    }

}
