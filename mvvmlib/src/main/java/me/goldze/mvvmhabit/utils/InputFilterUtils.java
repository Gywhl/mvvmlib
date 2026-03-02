package me.goldze.mvvmhabit.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Description：输入限制 无法跟 maxLength 通用
 *
 * @author Gej
 * @date 2024/11/28
 */
public class InputFilterUtils {

    /**
     * 空格
     */
    private static final String SPACE = " ";

    /**
     * 限制最大数
     * @param maxLength 限制最大字数
     * @return
     */
    public static InputFilter maxLength(int maxLength) {
        return new InputFilter.LengthFilter(maxLength);
    }

    /**
     * 限制中英文
     *
     * @return
     */
    public static InputFilter forbidCE() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 检查输入字符是否为中英文字符，如果不是，则返回空字符串，禁止输入
                for (int i = start; i < end; i++) {
                    char c = source.charAt(i);
                    if (!Character.toString(c).matches("[A-Za-z\\u4e00-\\u9fa5]")) {
                        return "";  // 如果字符不是中英文，返回空字符串，禁止输入
                    }
                }
                return null;  // 如果所有字符合法，则允许输入
            }
        };
    }

    /**
     * 创建一个 InputFilter 禁止输入空格
     *
     * @return
     */
    public static InputFilter forbidSpace() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 如果输入的是空格，返回空字符串，表示不允许输入
                if (source.toString().contains(SPACE)) {
                    return "";
                } else {
                    return null;
                }
            }
        };
    }

    /**
     * 创建一个 InputFilter 禁止开始和结束输入空格
     *
     * @return
     */
    public static InputFilter forbidStartEndSpace() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 如果插入的是空格字符，检查前后位置
                if (source.equals(SPACE)) {
                    // 检查输入前后的文本
                    if (dstart == 0 && source.equals(SPACE)) {
                        // 禁止在开始位置插入空格
                        return "";
                    }
                    if (dend == dest.length() && source.equals(SPACE)) {
                        // 禁止在结束位置插入空格
                        return "";
                    }
                }
                // 允许插入其他字符
                return null;
            }
        };
    }

    /**
     * 创建一个 InputFilter 限制只允许字母和数字
     *
     * @return
     */
    public static InputFilter letterNumber() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 匹配非字母和非数字的字符
                if (source.toString().matches("[a-zA-Z0-9]*")) {
                    return null; // 如果是字母或数字，允许输入
                } else {
                    return ""; // 否则返回空字符串，禁止输入
                }
            }
        };
    }

    /**
     * 创建一个 InputFilter 限制只允许数字、字母和符号，不包括空格
     *
     * @return
     */
    public static InputFilter letterSymbolNumberButSpace() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 允许数字、字母和符号，排除空格
                if (source.toString().matches("[a-zA-Z0-9!@#$%^&*(),.?\":{}|<>_+-=]*")) {
                    // 如果是数字、字母或符号，允许输入
                    return null;
                } else {
                    // 否则返回空字符串，禁止输入
                    return "";
                }
            }
        };

    }


    /**
     * 截取超过最大长度的部分，保留符合最大长度限制的文本
     *
     * @param input     文本
     * @param maxLength 最大长度
     * @return
     */
    public static String trimToMaxLength(String input, int maxLength) {
        int totalLength = 0;
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            totalLength += 1;

            if (totalLength > maxLength) {
                break;
            }

            result.append(c);
        }
        return result.toString();
    }

}
