package me.goldze.mvvmhabit.crash;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description：崩溃工具类
 *
 * @author Gej
 * @date 2025/2/5
 */
public class CrashUtils {

    /**
     * 生成错误文件
     * @param directoryPath 文件夹路径
     * @param throwable 异常
     */
    public static void writeCrashToFile(String directoryPath, Throwable throwable) {
        // 获取崩溃日志的文件路径
        String crashFilePath = getCrashFilePath(directoryPath);

        // 获取崩溃信息
        String crashInfo = getCrashInfo(throwable);

        // 写入文件
        try (FileOutputStream fos = new FileOutputStream(crashFilePath, true)) {
            fos.write(crashInfo.getBytes());
        } catch (IOException e) {
            Log.e("MyApplication", "Error writing crash log to file", e);
        }
    }

    /**
     * 获取崩溃日志文件全路径
     * @param directoryPath 文件夹路径
     * @return
     */
    private static String getCrashFilePath(String directoryPath) {
        // 获取应用的崩溃日志存储目录
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // 使用日期命名崩溃文件
        String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        return directoryPath + "/crash_" + date + ".log";
    }

    /**
     * 获取崩溃信息
     * @param throwable 崩溃信息
     * @return
     */
    private static String getCrashInfo(Throwable throwable) {
        // 获取崩溃堆栈信息
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);

        // 获取设备信息（可选）
        String deviceInfo = "Device: " + android.os.Build.MODEL + "\n";
        deviceInfo += "Android Version: " + android.os.Build.VERSION.RELEASE + "\n";

        // 拼接崩溃信息
        return deviceInfo + "Crash Time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n"
                + stringWriter + "\n\n";
    }
}
