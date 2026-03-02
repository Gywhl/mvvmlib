package me.goldze.mvvmhabit.executor;

import android.os.Handler;
import android.os.Looper;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import me.goldze.mvvmhabit.runnable.PriorityRunnable;

/**
 * Description：优先级线程池
 *
 * @author Gej
 * @date 2023/12/15
 */
public class PriorityExecutor extends ThreadPoolExecutor {

    /**
     * 核心线程池大小
     */
    private static final int CORE_POOL_SIZE = 5;
    /**
     * 最大线程池队列大小
     */
    private static final int MAXIMUM_POOL_SIZE = 256;
    /**
     * 保持存活时间，当线程数大于corePoolSize的空闲线程能保持的最大时间。
     */
    private static final int KEEP_ALIVE = 1;
    /**
     * 主要获取添加任务
     */
    private static final AtomicLong SEQ_SEED = new AtomicLong(0);
    /**
     * 拒绝策略，当线程池任务队列饱和 , 或者没有空闲线程时 , 线程池被关闭时 , 会调用该方法
     */
    private static final RejectedExecutionHandler HANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    /**
     * 主线程handler
     */
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    /**
     * 创建线程工厂
     */
    private static final ThreadFactory FACTORY = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "优先级 Thread#" + mCount.getAndIncrement());
        }
    };


    /**
     * 线程队列方式 先进先出
     */
    private static final Comparator<Runnable> FIFO = new Comparator<Runnable>() {
        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable) {
                PriorityRunnable lpr = ((PriorityRunnable) lhs);
                PriorityRunnable rpr = ((PriorityRunnable) rhs);
                int result = lpr.priority.ordinal() - rpr.priority.ordinal();
                return result == 0 ? (int) (lpr.SEQ - rpr.SEQ) : result;
            } else {
                return 0;
            }
        }
    };

    /**
     * 线程队列方式 后进先出
     */
    private static final Comparator<Runnable> LIFO = new Comparator<Runnable>() {
        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof PriorityRunnable && rhs instanceof PriorityRunnable) {
                PriorityRunnable lpr = ((PriorityRunnable) lhs);
                PriorityRunnable rpr = ((PriorityRunnable) rhs);
                int result = lpr.priority.ordinal() - rpr.priority.ordinal();
                return result == 0 ? (int) (rpr.SEQ - lpr.SEQ) : result;
            } else {
                return 0;
            }
        }
    };

    /**
     * 默认工作线程数5
     *
     * @param fifo 优先级相同时, 等待队列的是否优先执行先加入的任务.
     */
    public PriorityExecutor(boolean fifo) {
        this(CORE_POOL_SIZE, fifo);
    }

    /**
     * @param poolSize 工作线程数
     * @param fifo     优先级相同时, 等待队列的是否优先执行先加入的任务.
     */
    public PriorityExecutor(int poolSize, boolean fifo) {
        this(poolSize, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>(MAXIMUM_POOL_SIZE, fifo ? FIFO : LIFO), FACTORY, HANDLER);
    }

    public PriorityExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler HANDLER) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, HANDLER);
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
     * 在IO线程更新数据
     * @param runnable runnable
     */
    public void executeOnUiThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    /**
     * 在IO线程更新数据
     * @param runnable 线程
     * @param delay 延迟时间
     */
    public void executeOnUiThread(Runnable runnable,long delay) {
        mainHandler.postDelayed(runnable,delay);
    }

    /**
     * 提交任务
     *
     * @param runnable
     */
    @Override
    public void execute(Runnable runnable) {
        if (runnable instanceof PriorityRunnable) {
            ((PriorityRunnable) runnable).SEQ = SEQ_SEED.getAndIncrement();
        }
        super.execute(runnable);
    }
}
