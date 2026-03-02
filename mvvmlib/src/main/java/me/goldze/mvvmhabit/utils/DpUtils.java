package me.goldze.mvvmhabit.utils;

import android.content.Context;

/**
 * Description :
 *
 * @author Novice
 * @date 2020/7/20 0020
 */
public class DpUtils {

    /**
     * dp->px
     * @param context context
     * @param dpValue dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px->dp
     * @param context context
     * @param pxValue pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp->dp
     *
     * @param context context
     * @param sp      sp
     * @return
     */
    public static float sp2px(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return scale * sp + 0.5f;
    }

}
