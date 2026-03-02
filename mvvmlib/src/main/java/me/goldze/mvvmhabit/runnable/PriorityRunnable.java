package me.goldze.mvvmhabit.runnable;

import android.renderscript.RenderScript;

/**
 * Description：带有优先级的Runnable线程
 *
 * @author Gej
 * @date 2023/12/15
 */
public class PriorityRunnable implements Runnable {

    /**
     * 任务优先级
     */
    public final RenderScript.Priority priority;
    /**
     * 任务真正执行者
     */
    private final Runnable runnable;
    /**
     * 任务唯一标示
     */
    public long SEQ;

    public PriorityRunnable(RenderScript.Priority priority, Runnable runnable) {
        this.priority = priority == null ? RenderScript.Priority.NORMAL : priority;
        this.runnable = runnable;
    }

    @Override
    public final void run() {
        this.runnable.run();
    }
}
