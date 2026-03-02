package me.goldze.mvvmhabit.utils;

import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;

public class WindowUtils {

    public static final float Alpha_Default = 1f;  //窗体默认透明度
    public static final float Alpha_4 = 0.4f;  //弹出pop时窗体透明度
    public static final float Alpha_7 = 0.7f;  //弹出pop时窗体透明度

    /**
     * 修改窗口的透明度
     *
     * @param alpha
     */
    public static void changeWindowAlpha(Window window, float alpha) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
    }
}
