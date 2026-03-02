package me.goldze.mvvmhabit.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Description：动画工具类
 *
 * @author Gej
 * @date 2025/5/14
 */
public class AnimationUtil {

    public static ObjectAnimator startAlphaAnimator(View view){
        return starAnimator(view,"alpha",500);
    }

    /**
     * 开始动画
     * @param view view
     * @param name 动画类型
     * @param duration 动画时间
     */
    public static ObjectAnimator starAnimator(View view,String name,int duration){
        //闪烁
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, name, 1f, 0f);
        //每次动画的时长（毫秒）
        animator.setDuration(duration);
        //动画结束后，反方向播放（从结束状态回到起始状态）
        animator.setRepeatMode(ValueAnimator.REVERSE);
        //动画无限次重复播放
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
        return animator;
    }

    /**
     * 取消动画
     * @param animator 动画
     */
    public static void cancel(ObjectAnimator animator){
        if(animator!=null){
            animator.cancel();
        }
    }
}
