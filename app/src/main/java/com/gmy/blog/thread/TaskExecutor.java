package com.gmy.blog.thread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 */
public class TaskExecutor {

    private final static ExecutorService executorService = Executors
            .newFixedThreadPool(5);

    /**
     * @Title 2014年11月26日
     * @Description 私有构造函数
     * @author LiuSiQing
     * @Time 2014年11月26日上午10:14:26
     */
    private TaskExecutor() {
    }

    /**
     * @param task
     * @Title Execute
     * @Description 执行现场
     */
    public static void Execute(Runnable task) {
        // if( null == executorService || executorService.isShutdown() ||
        // executorService.isTerminated() )
        // executorService = Executors.newFixedThreadPool(5);
        executorService.submit(task);
    }

    /**
     * @return
     * @Title IsTerminated
     * @Description 线程是否执行完成
     */
    public static boolean IsTerminated() {
        executorService.shutdown();
        while (true)
            if (executorService.isTerminated())
                break;
        return true;
    }

    /**
     * @return
     * @Title shutdownNow
     * @Description 立即关闭线程池
     */
    @Deprecated
    public static List<Runnable> shutdownNow() {
        List<Runnable> list = executorService.shutdownNow();
        // executorService = null;
        return list;
    }
}
