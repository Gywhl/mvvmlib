package me.goldze.mvvmhabit.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Description：文本校验
 *
 * @author Gej
 * @date 2024/11/28
 */
public class StringCheckUtils {
    /**
     * 规则：必须同时包含8-16位大小写字母、数字和符号
     * 是否包含
     *
     * @param pwd 密码
     * @return
     */
    public static boolean isPasswordCorrect(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return false;
        }
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[`_~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\-])[\\da-zA-Z`_~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\-]{8,16}$";
        return pwd.matches(regex);
    }

    /**
     * 规则：6-12位字母或数字
     * 是否包含
     *
     * @param string 文本
     * @return
     */
    public static boolean letterNumber(String string) {
        // 动态生成正则表达式
        String regex = "^[a-zA-Z0-9]{6,12}$";
        return string.matches(regex);
    }

    //是否包含数字，
    public static boolean hasNum(String content) {
        String pattern = ".*[0-9]+.*$";
        return Pattern.matches(pattern, content);
    }

    //是否包含大写字母
    public static boolean hasUpLetter(String content) {
        String pattern = ".*[A-Z]+.*";
        return Pattern.matches(pattern, content);
    }

    //是否包含小写字母
    public static boolean hasLowLetter(String content) {
        String pattern = ".*[a-z]+.*";
        return Pattern.matches(pattern, content);
    }

    //是否包含特殊字符  没有需要的可以增加，多余了可以删除掉
    public static boolean hasSpecialChar(String content) {
        String pattern = ".*[`_~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+.*";
        return Pattern.matches(pattern, content);
    }

}
