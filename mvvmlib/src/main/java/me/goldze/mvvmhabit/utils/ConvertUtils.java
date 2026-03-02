package me.goldze.mvvmhabit.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Locale;

import me.goldze.mvvmhabit.utils.constant.MemoryConstants;
import me.goldze.mvvmhabit.utils.constant.TimeConstants;

/**
 * Description：转换相关工具类
 *
 * @author Gej
 * @date 2024/2/28
 */
public final class ConvertUtils {

    public final static String TAG = ConvertUtils.class.getName();
    /**
     * 用于建立十六进制字符的输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 用于建立十六进制字符的输出的大写字符数组
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static final String HEX_STRING = "0123456789ABCDEF";

    private ConvertUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * CRC高位字节值表
     */
    private static int[] aucCRCHi = {
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
            0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
            0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
            0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
            0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
            0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
            0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
            0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
            0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
            0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
            0x80, 0x41, 0x00, 0xC1, 0x81, 0x40
    };

    /**
     * CRC低位字节值表
     */
    private static int[] aucCRCLo = {
            0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06,
            0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD,
            0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09,
            0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A,
            0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4,
            0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3,
            0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3,
            0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4,
            0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A,
            0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29,
            0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED,
            0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26,
            0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60,
            0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67,
            0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F,
            0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8, 0x68,
            0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E,
            0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5,
            0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71,
            0x70, 0xB0, 0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92,
            0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C,
            0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B,
            0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B,
            0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C,
            0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42,
            0x43, 0x83, 0x41, 0x81, 0x80, 0x40
    };

    /**************************************UNICODE与String之间的转换**************************************/
    /**
     * 得到字符串的Unicode编码
     *
     * @param str 源字符串
     * @return unicode编码
     */
    public static String getUnicode(String str) {
        char[] cs = str.toCharArray();
        String result = "";
        for (char c : cs) {
            String s = Integer.toHexString(c);
            if (s.length() == 2) {
                s = "\\u00" + s;
            } else {
                s = "\\u" + s;
            }
            result += s;
        }
        return result;
    }

    /**
     * unicodetoString
     *
     * @param s
     * @return
     */
    public static final String unicodeToString(String s) {
        if (s == null || "".equalsIgnoreCase(s.trim())) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        boolean escape = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\':
                case '%':
                    escape = true;
                    break;
                case 'u':
                case 'U':
                    if (escape) {
                        try {
                            sb.append((char) Integer.parseInt(s.substring(i + 1,
                                    i + 5), 16));
                            escape = false;
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException();
                        }
                        i += 4;
                    } else {
                        sb.append(c);
                    }
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**************************************转ASCII码**************************************/

    /**
     * byte转换ASCII码图形字符串
     *
     * @param b 数组
     * @return String 对应的字符串
     */
    public static String byteToAscii(byte[] b, int start, int length) {
        try {
            return hexToAscii(byteToHex(copyByte(b, start, length)));
        } catch (Exception e) {
            Log.e("Error", "类：HexUtil    方法：decodeHexStr    异常：" + e.getMessage());
        }
        return "";
    }

    /**
     * 十六进制转换ASCII码图形字符串
     *
     * @param hex Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexToAscii(String hex) {
        try {
            hex = hex.replaceAll(" ", "");
            StringBuilder sb = new StringBuilder();
            for (int count = 0; count < hex.length() - 1; count += 2) {
                String output = hex.substring(count, (count + 2));
                int decimal = Integer.parseInt(output, 16);
                sb.append((char) decimal);
            }
            return sb.toString();
        } catch (Exception e) {
            Log.e("Error", "类：HexUtil    方法：decodeHexStr    异常：" + e.getMessage());
        }
        return "";
    }

    /**************************************转Hex（16进制）**************************************/
    /**
     * ASCII码图形字符串转换成十六进制字符串
     *
     * @param str 待转换的字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String asciiToHex(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     *
     * @param str         字符串
     * @param charsetName 编码 UTF-8 、GB2312等
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String strToHex(String str, String charsetName) {
        //根据默认编码获取字节数组
        byte[] bytes;
        try {
            str = str.replaceAll(" ", "");
            if (TextUtils.isEmpty(charsetName)) {
                charsetName = "UTF-8";
            }
            bytes = str.getBytes(charsetName);
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            //将字节数组中每个字节拆解成2位16进制整数
            for (int i = 0; i < bytes.length; i++) {
                sb.append(HEX_STRING.charAt((bytes[i] & 0xf0) >> 4));
                sb.append(HEX_STRING.charAt((bytes[i] & 0x0f) >> 0));
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data byte[]
     * @return 十六进制String
     */
    public static String byteToHex(byte[] data) {
        return byteToHex(data, true);
    }

    /**
     * byteArr转hexString
     * <p>例如：</p>
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节
     * @return 16进制大写字符串
     */
    public static String bytes2Hex(final byte bytes) {
        char[] ret = new char[2];
        ret[0] = DIGITS_UPPER[bytes >>> 4 & 0x0f];
        ret[1] = DIGITS_UPPER[bytes & 0x0f];
        return new String(ret);
    }

    /**
     * 将字节数组转换为十六进制字符串 中间有空格
     *
     * @param bytes byte[]
     * @return 十六进制String
     */
    public static String byteToHexSpace(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format(Locale.US, "%02x", b)).append(" ,");
        }
        return hex.toString();
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param b byte[]
     * @return 十六进制String
     */
    public static String byteToHex(byte[] b, int start, int length) {
        return byteToHex(copyByte(b, start, length), true);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data        byte[]
     * @param toLowerCase <code>true</code> 转换成小写格式 ， <code>false</code> 转换成大写格式
     * @return 十六进制String
     */
    public static String byteToHex(byte[] data, boolean toLowerCase) {
        return byteToHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制String
     */
    protected static String byteToHex(byte[] data, char[] toDigits) {
        if (data == null) {
            Log.e(TAG, "this data is null.");
            return "";
        }
        return new String(byteToHexChar(data, toDigits));
    }

    /**
     * hexChar转int
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private static int hex2Dec(final char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * charArr转byteArr
     *
     * @param chars 字符数组
     * @return 字节数组
     */
    public static byte[] chars2Bytes(final char[] chars) {
        if (chars == null || chars.length <= 0) {
            return null;
        }
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (chars[i]);
        }
        return bytes;
    }

    /**
     * float 转16进制字符,返回length为8的hex,4字节
     *
     * @param f
     * @param flag true:高位在前  false:低位在前
     * @return
     */
    public static String floatToHex(float f, boolean flag) {
        String hex = Integer.toHexString(Float.floatToIntBits(f));
        if ("0".equals(hex)) {
            return "00000000";
        }
        if (hex.length() % 2 != 0) {
            return "";
        }
        if (flag) {
            return hex;
        } else {
            return reverseOrder(hex);
        }
    }

    /**************************************转char**************************************/
    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的char[]
     * @return 十六进制char[]
     */
    private static char[] byteToHexChar(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    /**************************************转str**************************************/
    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     *
     * @param hex         Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @param charsetName 解码 UTF-8 、GB2312等
     * @return String 对应的字符串
     */
    public static String hexToStr(String hex, String charsetName) {
        try {
            hex = hex.replaceAll(" ", "");
            ByteArrayOutputStream baos = new ByteArrayOutputStream(hex.length() / 2);
            //将每2位16进制整数组装成一个字节
            for (int i = 0; i < hex.length(); i += 2) {
                baos.write((HEX_STRING.indexOf(hex.charAt(i)) << 4 | HEX_STRING.indexOf(hex.charAt(i + 1))));
            }
            String bb = "";
            try {
                if (TextUtils.isEmpty(charsetName)) {
                    charsetName = "UTF-8";
                }
                bb = new String(baos.toByteArray(), charsetName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bb;
        } catch (Exception e) {
            Log.e("Error", "类：HexUtil    方法：decodeHexStr    异常：" + e.getMessage());
        }
        return "";
    }

    /**
     * 16进制字符串转换成byte数组
     * */
    public static byte[] hex2Bytes(String hexString){
        if(TextUtils.isEmpty(hexString)){
            return new byte[0];
        }
        byte[] arrB = hexString.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        String strTmp = null;
        for (int i = 0; i < iLen; i += 2)
        {
            strTmp = new String(arrB, i, 2);
            arrOut[(i / 2)] = ((byte)Integer.parseInt(strTmp, 16));
        }
        return arrOut;
    }

    /**************************************byte与整形数字之间的转换**************************************/
    /**
     * num转byte数组
     *
     * @param size 要获得的数组大小
     * @param data
     * @param flag 标识高低位顺序，高位在前为true，低位在前为false
     */
    public static byte[] numToBytes(int size, long data, boolean flag) {
        byte[] bytes = new byte[size];
        for (int i = 0; i < bytes.length; i++) {
            int i1 = (flag ? (bytes.length - 1 - i) : i) << 3;
            bytes[i] = (byte) ((data >> i1) & 0xff);
        }
        return bytes;
    }

    /**
     * 16字节byte数组数组转int
     *
     * @param b
     * @param start 第几位开始
     * @param flag  标识高低位顺序，参数b高位在前为true，b低位在前为false
     * @return
     */
    public static int byteToNum(byte[] b, int start, int length, boolean flag) {
        int value = 0;
        // 循环读取每个字节通过移位运算完成long的8个字节拼装
        for (int i = 0; i < length; ++i) {
            int shift = (flag ? (length - i - 1) : i) << 3;
            value |= ((long) 0xff << shift) & ((long) b[start + i] << shift);
        }
        return value;
    }

    /**************************************转float**************************************/
    /**
     * 16进制字符串 转float
     *
     * @param str
     * @param flag true:str高位在前  false:str低位在前
     * @return
     */
    public static float hexToFloat(String str, boolean flag) {
        try {
            if (TextUtils.isEmpty(str)) {
                throw new RuntimeException("HexUtil:hexToFloat str is null");
            }
            byte[] bytes = str.getBytes();
            return byteToFloat(bytes, 0, 4, flag);
        } catch (Exception e) {
            Log.e("Error", "类：HexUtil    方法：hexToFloat    异常：" + e.getMessage());
        }
        return 0f;
    }
    /**
     * 16进制字符串 转float
     *
     * @param str
     * @param flag true:str高位在前  false:str低位在前
     * @return
     */
    public static int hexToInt(String str, boolean flag) {
        try {
            if (TextUtils.isEmpty(str)) {
                throw new RuntimeException("HexUtil:hexToFloat str is null");
            }
            byte[] bytes = str.getBytes();
            return byteToNum(bytes, 0, 2, flag);
        } catch (Exception e) {
            Log.e("Error", "类：HexUtil    方法：hexToFloat    异常：" + e.getMessage());
        }
        return 0;
    }

    /**
     * 16字节byte数组 转float
     *
     * @param b
     * @param flag 是否不更换前后顺序
     * @return
     */
    public static float byteToFloat(byte[] b, int start, int length, boolean flag) {
        float value = 0f;
        String s;
        if (flag) {
            s = byteToHex(copyByte(b, start, length));
        } else {
            //把s顺序颠倒
            s = byteToHex(reverse(copyByte(b, start, length)));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            value = Float.intBitsToFloat(Integer.parseInt(s, 16));
        }
        return value;
    }

    /**************************************数字转16进制字符串**************************************/

    private static String hexInt(int total) {
        int a = total / 256;
        int b = total % 256;
        if (a > 255) {
            return hexInt(a) + format(b);
        }
        return format(a) + format(b);
    }

    private static String format(int hex) {
        String hexa = Integer.toHexString(hex);
        int len = hexa.length();
        if (len < 2) {
            hexa = "0" + hexa;
        }
        return hexa;
    }

    /**************************************辅助功能**************************************/
    /**
     * 改变16进制字符的顺序 顺<->逆
     *
     * @param data
     * @return
     */
    public static String reverseOrder(String data) {
        byte[] bytes = data.getBytes();
        byte[] bs = new byte[bytes.length];
        for (int i = 0; i < bytes.length / 2; i++) {
            bs[i * 2] = bytes[bytes.length - 1 - i * 2];
            bs[i * 2 + 1] = bytes[bytes.length - 2 - i * 2];
        }
        return byteToHex(bs, true);
    }

    /**
     * 字节数组逆序
     *
     * @param data
     * @return
     */
    public static byte[] reverse(byte[] data) {
        byte[] reverseData = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            reverseData[i] = data[data.length - 1 - i];
        }
        return reverseData;
    }

    /**
     * 复制长度为length的数组
     *
     * @param b      被复制的byte数组
     * @param start  开始位置
     * @param length 复制长度
     * @return
     */
    public static byte[] copyByte(byte[] b, int start, int length) {
        if (b == null) {
            Log.d(TAG, "HexUtil getByte:byte data is null");
            return null;
        }
        if (length <= 0) {
            Log.d(TAG, "HexUtil getByte:length  is 0");
            return null;
        }
        byte[] bs = new byte[length];
        System.arraycopy(b, start, bs, 0, bs.length);
        return bs;
    }

    /**
     * 合并两个数组
     * @param byte1 数组1
     * @param byte2 数组2
     * @return
     */
    public static byte[] mergeByteArrays(byte[] byte1, byte[] byte2) {
        byte[] byte3 = new byte[byte1.length + byte2.length];
        try {
            System.arraycopy(byte1, 0, byte3, 0, byte1.length);
            System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        } catch (Exception ignored) {
        }
        return byte3;
    }

    /**************************************校验码**************************************/
    /**
     * Function Name:  crc16校验
     *
     * @param ucbuf 校验数据
     * @return
     */
    public static int crc16(byte[] ucbuf) {
        if(ucbuf==null){
            return -1;
        }
        int crcHi = 0xFF;
        int crcLo = 0xFF;
        int iIndex;
        for (int i = 0; i < ucbuf.length; i++) {
            int data = ucbuf[i];
            if (data < 0) {
                data = 256 + ucbuf[i];
            }
            iIndex = crcLo ^ data;
            crcLo = crcHi ^ aucCRCHi[iIndex];
            crcHi = aucCRCLo[iIndex];
        }
        return (crcHi << 8 | crcLo);
    }

    /**
     * 计算CRC16校验码
     *
     * @param bytes
     * @return
     */
    public static int getCrc(byte[] bytes) {
        int crc = 0x0000ffff;
        int polynomial = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            crc ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((crc & 0x00000001) != 0) {
                    crc >>= 1;
                    crc ^= polynomial;
                } else {
                    crc >>= 1;
                }
            }
        }
        return crc;
    }

    /**
     * 获取16进制 和校验 字符
     *
     * @param hexdata 要校验的16进制字符
     * @param flag    true:高位在前 false:低位在前
     * @return
     */
    public static String checkSum(String hexdata, boolean flag) {
        if (TextUtils.isEmpty(hexdata)) {
            return "00";
        }
        hexdata = hexdata.replaceAll(" ", "");
        int total = 0;
        int len = hexdata.length();
        if (len % 2 != 0) {
            return "00";
        }
        int num = 0;
        while (num < len) {
            String s = hexdata.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        String result;
        if (flag) {
            result = hexInt(total);
        } else {
            result = reverseOrder(hexInt(total));
        }
        return result;
    }

    /**
     * 计算 16 位求和校验
     * @param data 数据
     * @param start 开始位置
     * @param length 校验长度
     * @return
     */
    public static int calcChecksum16(byte[] data, int start, int length) {
        int sum = 0;
        int end = Math.min(start + length, data.length);
        for (int i = start; i < end; i++) {
            sum += (data[i] & 0xFF);
        }
        return sum & 0xFFFF;
    }

    /**
     * 异或校验
     *
     * @param data 十六进制串
     * @return checkData  十六进制串
     */
    public static String checkXor(String data) {
        int checkData = 0;
        for (int i = 0; i < data.length(); i = i + 2) {
            //将十六进制字符串转成十进制
            int start = Integer.parseInt(data.substring(i, i + 2), 16);
            //进行异或运算
            checkData = start ^ checkData;
        }
        return intToHex(checkData);
    }

    /**
     * 将十进制整数转为十六进制数大写，并补位
     */
    public static String intToHex(int s) {
        String ss = Integer.toHexString(s);
        if (ss.length() % 2 != 0) {
            ss = "0" + ss;
        }
        return ss.toUpperCase();
    }

    /**
     * 以unit为单位的内存大小转字节数
     *
     * @param memorySize 大小
     * @param unit       单位类型
     *                   <ul>
     *                   <li>{@link MemoryConstants#BYTE}: 字节</li>
     *                   <li>{@link MemoryConstants#KB}  : 千字节</li>
     *                   <li>{@link MemoryConstants#MB}  : 兆</li>
     *                   <li>{@link MemoryConstants#GB}  : GB</li>
     *                   </ul>
     * @return 字节数
     */
    public static long memorySize2Byte(final long memorySize, @MemoryConstants.Unit final int unit) {
        if (memorySize < 0) {
            return -1;
        }
        return memorySize * unit;
    }

    /**
     * 字节数转以unit为单位的内存大小
     *
     * @param byteNum 字节数
     * @param unit    单位类型
     *                <ul>
     *                <li>{@link MemoryConstants#BYTE}: 字节</li>
     *                <li>{@link MemoryConstants#KB}  : 千字节</li>
     *                <li>{@link MemoryConstants#MB}  : 兆</li>
     *                <li>{@link MemoryConstants#GB}  : GB</li>
     *                </ul>
     * @return 以unit为单位的size
     */
    public static double byte2MemorySize(final long byteNum, @MemoryConstants.Unit final int unit) {
        if (byteNum < 0) {
            return -1;
        }
        return (double) byteNum / unit;
    }

    /**
     * 字节数转合适内存大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    @SuppressLint("DefaultLocale")
    public static String byte2FitMemorySize(final long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < MemoryConstants.KB) {
            return String.format("%.3fB", (double) byteNum + 0.0005);
        } else if (byteNum < MemoryConstants.MB) {
            return String.format("%.3fKB", (double) byteNum / MemoryConstants.KB + 0.0005);
        } else if (byteNum < MemoryConstants.GB) {
            return String.format("%.3fMB", (double) byteNum / MemoryConstants.MB + 0.0005);
        } else {
            return String.format("%.3fGB", (double) byteNum / MemoryConstants.GB + 0.0005);
        }
    }

    /**
     * 以unit为单位的时间长度转毫秒时间戳
     *
     * @param timeSpan 毫秒时间戳
     * @param unit     单位类型
     *                 <ul>
     *                 <li>{@link TimeConstants#MSEC}: 毫秒</li>
     *                 <li>{@link TimeConstants#SEC }: 秒</li>
     *                 <li>{@link TimeConstants#MIN }: 分</li>
     *                 <li>{@link TimeConstants#HOUR}: 小时</li>
     *                 <li>{@link TimeConstants#DAY }: 天</li>
     *                 </ul>
     * @return 毫秒时间戳
     */
    public static long timeSpan2Millis(final long timeSpan, @TimeConstants.Unit final int unit) {
        return timeSpan * unit;
    }

    /**
     * 毫秒时间戳转以unit为单位的时间长度
     *
     * @param millis 毫秒时间戳
     * @param unit   单位类型
     *               <ul>
     *               <li>{@link TimeConstants#MSEC}: 毫秒</li>
     *               <li>{@link TimeConstants#SEC }: 秒</li>
     *               <li>{@link TimeConstants#MIN }: 分</li>
     *               <li>{@link TimeConstants#HOUR}: 小时</li>
     *               <li>{@link TimeConstants#DAY }: 天</li>
     *               </ul>
     * @return 以unit为单位的时间长度
     */
    public static long millis2TimeSpan(final long millis, @TimeConstants.Unit final int unit) {
        return millis / unit;
    }

    /**
     * 毫秒时间戳转合适时间长度
     *
     * @param millis    毫秒时间戳
     *                  <p>小于等于0，返回null</p>
     * @param precision 精度
     *                  <ul>
     *                  <li>precision = 0，返回null</li>
     *                  <li>precision = 1，返回天</li>
     *                  <li>precision = 2，返回天和小时</li>
     *                  <li>precision = 3，返回天、小时和分钟</li>
     *                  <li>precision = 4，返回天、小时、分钟和秒</li>
     *                  <li>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</li>
     *                  </ul>
     * @return 合适时间长度
     */
    @SuppressLint("DefaultLocale")
    public static String millis2FitTimeSpan(long millis, int precision) {
        if (millis <= 0 || precision <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
        precision = Math.min(precision, 5);
        for (int i = 0; i < precision; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
            }
        }
        return sb.toString();
    }

    /**
     * bytes转bits
     *
     * @param bytes 字节数组
     * @return bits
     */
    public static String bytes2Bits(final byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            for (int j = 7; j >= 0; --j) {
                sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    /**
     * bytes转bits
     *
     * @param bytes 字节数组
     * @return bits
     */
    public static String bytes2Bits(final byte[] bytes, int start, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + length; i++) {
            for (int j = 7; j >= 0; --j) {
                sb.append(((bytes[i] >> j) & 0x01) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    /**
     * bits转bytes
     *
     * @param bits 二进制
     * @return bytes
     */
    public static byte[] bits2Bytes(String bits) {
        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        // 不是8的倍数前面补0
        if (lenMod != 0) {
            StringBuilder bitsBuilder = new StringBuilder(bits);
            for (int i = lenMod; i < 8; i++) {
                bitsBuilder.insert(0, "0");
            }
            bits = bitsBuilder.toString();
            byteLen++;
        }
        byte[] bytes = new byte[byteLen];
        for (int i = 0; i < byteLen; ++i) {
            for (int j = 0; j < 8; ++j) {
                bytes[i] <<= 1;
                bytes[i] |= bits.charAt(i * 8 + j) - '0';
            }
        }
        return bytes;
    }

    /**
     * inputStream转outputStream
     *
     * @param is 输入流
     * @return outputStream子类
     */
    public static ByteArrayOutputStream input2OutputStream(final InputStream is) {
        if (is == null) {
            return null;
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] b = new byte[MemoryConstants.KB];
            int len;
            while ((len = is.read(b, 0, MemoryConstants.KB)) != -1) {
                os.write(b, 0, len);
            }
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(is);
        }
    }

    /**
     * outputStream转inputStream
     *
     * @param out 输出流
     * @return inputStream子类
     */
    public ByteArrayInputStream output2InputStream(final OutputStream out) {
        if (out == null) {
            return null;
        }
        return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
    }

    /**
     * inputStream转byteArr
     *
     * @param is 输入流
     * @return 字节数组
     */
    public static byte[] inputStream2Bytes(final InputStream is) {
        if (is == null) {
            return null;
        }
        return input2OutputStream(is).toByteArray();
    }

    /**
     * byteArr转inputStream
     *
     * @param bytes 字节数组
     * @return 输入流
     */
    public static InputStream bytes2InputStream(final byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        return new ByteArrayInputStream(bytes);
    }

    /**
     * outputStream转byteArr
     *
     * @param out 输出流
     * @return 字节数组
     */
    public static byte[] outputStream2Bytes(final OutputStream out) {
        if (out != null) {
            return ((ByteArrayOutputStream) out).toByteArray();
        }
        return null;
    }

    /**
     * outputStream转byteArr
     *
     * @param bytes 字节数组
     * @return 字节数组
     */
    public static OutputStream bytes2OutputStream(final byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            os.write(bytes);
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(os);
        }
    }

    /**
     * inputStream转string按编码
     *
     * @param is          输入流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String inputStream2String(final InputStream is, final String charsetName) {
        if (is == null || isSpace(charsetName)) {
            return null;
        }
        try {
            return new String(inputStream2Bytes(is), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转inputStream按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    public static InputStream string2InputStream(final String string, final String charsetName) {
        if (string == null || isSpace(charsetName)) {
            return null;
        }
        try {
            return new ByteArrayInputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * outputStream转string按编码
     *
     * @param out         输出流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String outputStream2String(final OutputStream out, final String charsetName) {
        if (out == null || isSpace(charsetName)) {
            return null;
        }
        try {
            return new String(outputStream2Bytes(out), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string转outputStream按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    public static OutputStream string2OutputStream(final String string, final String charsetName) {
        if (string == null || isSpace(charsetName)) {
            return null;
        }
        try {
            return bytes2OutputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bitmap转byteArr
     *
     * @param bitmap bitmap对象
     * @param format 格式
     * @return 字节数组
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byteArr转bitmap
     *
     * @param bytes 字节数组
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable drawable对象
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(final Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * bitmap转drawable
     *
     * @param bitmap bitmap对象
     * @return drawable
     */
    public static Drawable bitmap2Drawable(final Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(Utils.getContext().getResources(), bitmap);
    }

    /**
     * drawable转byteArr
     *
     * @param drawable drawable对象
     * @param format   格式
     * @return 字节数组
     */
    public static byte[] drawable2Bytes(final Drawable drawable, final Bitmap.CompressFormat format) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * byteArr转drawable
     *
     * @param bytes 字节数组
     * @return drawable
     */
    public static Drawable bytes2Drawable(final byte[] bytes) {
        return bytes == null ? null : bitmap2Drawable(bytes2Bitmap(bytes));
    }

    /**
     * view转Bitmap
     *
     * @param view 视图
     * @return bitmap
     */
    public static Bitmap view2Bitmap(final View view) {
        if (view == null) {
            return null;
        }
        Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(final float dpValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(final float pxValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(final float spValue) {
        final float fontScale = Utils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(final float pxValue) {
        final float fontScale = Utils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    private static boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 四舍五入向上取整，保留newScale位小数
     *
     * @param d        数
     * @param newScale 小数位
     * @return
     */
    public static String roundUp(double d, int newScale) {
        BigDecimal decimal = new BigDecimal(String.valueOf(d));
        return decimal.setScale(newScale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 四舍五入向上取整，保留newScale位小数
     *
     * @param d        数
     * @param newScale 小数位
     * @return
     */
    public static double roundUpd(double d, int newScale) {
        BigDecimal decimal = new BigDecimal(String.valueOf(d));
        return decimal.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 保留precision位有效数字
     * @param d
     * @param precision
     * @return
     */
    public static double significantFigures(double d, int precision) {
        BigDecimal bd = new BigDecimal(d, new MathContext(precision, RoundingMode.HALF_UP));
        return bd.doubleValue();
    }

    public static String formatSignificant(double d, int precision) {
        BigDecimal bd = BigDecimal
                .valueOf(d)
                .round(new MathContext(precision, RoundingMode.HALF_UP));

        return bd.toPlainString();
    }


    /**
     * 尾数补0
     * @param d
     * @param scale
     * @return
     */
    public static String format(double d, int scale) {
        return String.format(Locale.US, "%." + scale + "f", d);
    }

}
