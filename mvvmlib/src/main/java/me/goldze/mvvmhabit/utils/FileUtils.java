package me.goldze.mvvmhabit.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Description：文件管理类
 *
 * @author Gej
 * @date 2025/2/5
 */
public class FileUtils {

    /**
     * 声明各种类型文件的dataType
     **/
    private static final String DATA_TYPE_ALL = "*/*";//未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
    private static final String DATA_TYPE_APK = "application/vnd.android.package-archive";
    private static final String DATA_TYPE_VIDEO = "video/*";
    private static final String DATA_TYPE_AUDIO = "audio/*";
    private static final String DATA_TYPE_HTML = "text/html";
    private static final String DATA_TYPE_IMAGE = "image/*";
    private static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    private static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
    private static final String DATA_TYPE_WORD = "application/msword";
    private static final String DATA_TYPE_CHM = "application/x-chm";
    private static final String DATA_TYPE_TXT = "text/plain";
    private static final String DATA_TYPE_PDF = "application/pdf";

    public static void openFile(String filePath, Context context) {
        File file = new File(filePath);
        openFile(file, context);
    }

    /**
     * 打开文件
     *
     * @param file 文件
     */
    public static void openFile(File file, Context context) {
        if (!file.exists()) {
            ToastUtils.showShort("打开失败，原因：文件已经被移动或者删除");
            return;
        }
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        Intent intent = null;
        String filePath = file.getPath();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            intent = generateVideoAudioIntent(filePath, DATA_TYPE_AUDIO, context);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            intent = generateVideoAudioIntent(filePath, DATA_TYPE_VIDEO, context);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            intent = generateCommonIntent(filePath, DATA_TYPE_IMAGE, context);
        } else if (end.equals("apk")) {
            intent = generateCommonIntent(filePath, DATA_TYPE_APK, context);
        } else if (end.equals("ppt")) {
            intent = generateCommonIntent(filePath, DATA_TYPE_PPT, context);
        } else if (end.equals("xls")) {
            intent = generateCommonIntent(filePath, DATA_TYPE_EXCEL, context);
        } else if (end.equals("doc")) {
            intent = generateCommonIntent(filePath, DATA_TYPE_WORD, context);
        } else if (end.equals("pdf")) {
            intent = generateCommonIntent(filePath, DATA_TYPE_PDF, context);
        } else if (end.equals("chm")) {
            intent = generateCommonIntent(filePath, DATA_TYPE_CHM, context);
        } else if (end.equals("txt")) {
            intent = generateCommonIntent(filePath, DATA_TYPE_TXT, context);
        } else {
            intent = generateCommonIntent(filePath, DATA_TYPE_ALL, context);
        }
        context.startActivity(intent);
    }

    /**
     * 产生打开视频或音频的Intent
     *
     * @param filePath 文件路径
     * @param dataType 文件类型
     * @return
     */
    private static Intent generateVideoAudioIntent(String filePath, String dataType, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        File file = new File(filePath);
        intent.setDataAndType(getUri(intent, file, context), dataType);
        return intent;
    }

    /**
     * 产生打开网页文件的Intent
     *
     * @param filePath 文件路径
     * @return
     */
    private static Intent generateHtmlFileIntent(String filePath) {
        Uri uri = Uri.parse(filePath)
                .buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content")
                .encodedPath(filePath)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, DATA_TYPE_HTML);
        return intent;
    }

    /**
     * 产生除了视频、音频、网页文件外，打开其他类型文件的Intent
     *
     * @param filePath 文件路径
     * @param dataType 文件类型
     * @return
     */
    private static Intent generateCommonIntent(String filePath, String dataType, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(filePath);
        Uri uri = getUri(intent, file, context);
        intent.setDataAndType(uri, dataType);
        return intent;
    }

    /**
     * 获取对应文件的Uri
     *
     * @param intent 相应的Intent
     * @param file   文件对象
     * @return
     */
    public static Uri getUri(Intent intent, File file, Context context) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本是否在7.0以上
            uri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".provider",
                    file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 在本地创建文件
     *
     * @param context  context
     * @param path     相对路径
     * @param fileName 文件名
     * @return
     */
    public static File createFile(Context context, String path, String fileName) {
        try {
            File fileEx = context.getExternalFilesDir(null);
            String dir = fileEx.getAbsolutePath() + path;
            deleteFile(dir);
            File mTempPhotoFile = new File(dir + fileName);
            if (!mTempPhotoFile.getParentFile().exists()) {
                mTempPhotoFile.getParentFile().mkdirs();
            }
            mTempPhotoFile.createNewFile();
            return mTempPhotoFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在本地创建文件
     *
     * @param path 文件路径
     * @return
     */
    public static boolean createFile(String path) {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除所有文件
     *
     * @param path
     */
    public static void deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }

        String[] tempList = file.list();

        File temp = null;

        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                deleteFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                temp.delete();//再删除空文件夹
            }
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * 文件是否存在
     *
     * @param path 文件路径
     * @return 文件是否存在
     */
    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 从 res/raw 目录复制模板文件到存储位置
     *
     * @param context  上下文
     * @param rawResId 资源文件 源文件 路径
     * @param destPath 目标文件
     */
    public static void copyFileFromRawToStorage(Context context, int rawResId, String destPath) throws IOException {
        File file = new File(destPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        InputStream inStream = context.getResources().openRawResource(rawResId);
        OutputStream outStream = new FileOutputStream(destPath);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, length);
        }

        inStream.close();
        outStream.close();
    }

    /**
     * 从 res/raw 目录复制模板文件到存储位置
     *
     * @param sourcePath 源文件路径
     * @param destPath   目标文件
     */
    public static void copyFileToStorage(String sourcePath, String destPath) throws IOException {
        File file = new File(destPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        InputStream inStream = new FileInputStream(sourcePath);
        OutputStream outStream = new FileOutputStream(destPath);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, length);
        }

        inStream.close();
        outStream.close();
    }

    /**
     * 读取文件内容（兼容 Android 8.0 以下）
     *
     * @param filePath 文件路径
     * @return
     */
    public static String readFileContent(String filePath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8.0 及以上，使用 Files.readAllBytes()
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            stringBuilder.append(new String(fileBytes));
        } else {
            // Android 8.0 以下，使用 FileInputStream
            FileInputStream inputStream = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
        }
        return stringBuilder.toString();
    }

    /**
     * 从数据流中读取文件中的数据
     *
     * @param dataInputStream 数据流
     * @param start           开始位置
     * @param end             结束位置
     * @return
     */
    public static String readToString(DataInputStream dataInputStream, int start, int end) {
        String str = "";
        byte[] b = new byte[end - start];
        try {
            if (null != dataInputStream) {
                dataInputStream.read(b, start, b.length);
                str = new String(b);
            }
        } catch (Exception ignored) {
        }
        return str;
    }

    /**
     * 从数据流中读取文件中的数据
     *
     * @param dataInputStream 数据流
     * @param length          数据长度
     * @return
     */
    public static int readToInt(DataInputStream dataInputStream, int length) {
        int num = 0;
        byte[] b = new byte[length];
        try {
            if (null != dataInputStream) {
                dataInputStream.read(b, 0, b.length);
                num = ConvertUtils.byteToNum(b, 0, b.length, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    /**
     * 从数据流中读取文件中的数据
     *
     * @param dataInputStream 数据流
     * @param start           开始位置
     * @return
     */
    public static byte readToByte(DataInputStream dataInputStream, int start) {
        byte num = 0;
        byte[] b = new byte[1];
        try {
            if (null != dataInputStream) {
                dataInputStream.read(b, start, b.length);
                num = b[0];
            }
        } catch (Exception ignored) {
        }
        return num;
    }

    /**
     * 打开本地文件夹
     *
     * @param path 要打开文件夹的路径
     */
    public static void openLocalFolder(String path, Context context) {
        Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:" + path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "*/*");
        // 启动文件管理器
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // 处理无法打开文件夹的情况
            ToastUtils.showShort("无法打开文件夹");
        }
    }

    /**
     * 文件分享
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static void shareFile(Activity context, String filePath) {
        // 获取文件对象
        File file = new File(filePath);
        // 创建一个 Uri 对象
        Uri fileUri;
        // Android 7.0 (API 24) 及以上需要使用 FileProvider 来分享文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 使用 FileProvider 获取安全的 Uri
            fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        } else {
            // Android 7.0 以下直接使用 file:// 的 Uri
            fileUri = Uri.fromFile(file);
        }
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        // 设置 文件的 MIME 类型
        shareIntent.setType("application/pdf");
        // 通过 EXTRA_STREAM 分享文件
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        // 允许接收方应用读取文件
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 启动选择目标应用的对话框
        context.startActivity(Intent.createChooser(shareIntent, "分享至"));
    }

    /**
     * 将 Uri 转换为 File 对象 适配Android 10以上
     *
     * @param context context
     * @param uri     uri
     * @return
     */
    public static File getFileFromUri(Context context, Uri uri) {
        if (uri != null) {
            try {
                String fileName = null;

                // 获取 ContentResolver
                ContentResolver contentResolver = context.getContentResolver();

                // 查询文件的 DisplayName
                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        // 获取文件名
                        fileName = cursor.getString(nameIndex);
                    }
                    cursor.close();
                }

                InputStream inputStream = contentResolver.openInputStream(uri);

                // 你可以通过 Retrofit 或 OkHttp 将这个 InputStream 上传到服务器
                // 将 InputStream 转换为 RequestBody
                File file = new File(context.getCacheDir(), fileName);
                try (OutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return file;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
