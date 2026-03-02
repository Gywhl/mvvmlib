package me.goldze.mvvmhabit.executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Description：定时线程池
 *
 * @author Gej
 * @date 2023/12/17
 */
public class ScheduledExecutor extends ScheduledThreadPoolExecutor {

    /**
     * 核心线程池大小
     */
    private static final int CORE_POOL_SIZE = 5;
    /**
     * 主要获取添加任务
     */
    private static final AtomicLong SEQ_SEED = new AtomicLong(0);
    /**
     * 拒绝策略，当线程池任务队列饱和 , 或者没有空闲线程时 , 线程池被关闭时 , 会调用该方法
     */
    private static final RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();
    /**
     * 创建线程工厂
     */
    private static final ThreadFactory FACTORY = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "定时 Thread#" + mCount.getAndIncrement());
        }
    };

    /**
     * 默认工作线程数5
     *
     */
    public ScheduledExecutor() {
        this(CORE_POOL_SIZE);
    }

    /**
     * @param poolSize 工作线程数
     */
    public ScheduledExecutor(int poolSize) {
        this(poolSize, FACTORY, HANDLER);
    }

    public ScheduledExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    /**
     * 判断当前线程池是否繁忙
     *
     * @return
     */
    public boolean isBusy() {
        return getActiveCount() >= getCorePoolSize();
    }

    /**
     * 提交任务
     *
     * @param runnable
     */
    @Override
    public void execute(Runnable runnable) {
        super.execute(runnable);
    }
}