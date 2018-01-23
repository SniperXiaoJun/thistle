/*
 * Copyright (C) 2015-2017 S.Violet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Project GitHub: https://github.com/shepherdviolet/thistle
 * Email: shepherdviolet@163.com
 */

package sviolet.thistle.util.common;

import sviolet.thistle.compat.CompatThreadFactoryBuilder;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.*;

/**
 * 线程池工具
 * @author S.Violet
 */
public class ThreadPoolExecutorUtils {

    private static final Set<ExecutorService> POOL = Collections.newSetFromMap(new WeakHashMap<ExecutorService, Boolean>());

    /**
     * 创建一个线程池
     * @param corePoolSize 核心线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveSeconds 线程保活时间(秒)
     * @param threadNameFormat 线程名称格式(rpc-pool-%d)
     */
    public static ExecutorService newInstance(int corePoolSize,
                                       int maximumPoolSize,
                                       long keepAliveSeconds,
                                       String threadNameFormat){
        return newInstance(
                corePoolSize,
                maximumPoolSize,
                keepAliveSeconds,
                threadNameFormat,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadPoolExecutor.AbortPolicy(),
                null);
    }

    /**
     * 创建一个线程池
     * @param corePoolSize 核心线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveSeconds 线程保活时间(秒)
     * @param threadNameFormat 线程名称格式(rpc-pool-%d)
     * @param workQueue 等待队列, new LinkedBlockingQueue<Runnable>(1024), new SynchronousQueue<Runnable>()
     * @param rejectHandler 拒绝处理器, new ThreadPoolExecutor.AbortPolicy()
     * @param executeListener nullable, 监听执行前执行后的事件
     */
    public static ExecutorService newInstance(int corePoolSize,
                                              int maximumPoolSize,
                                              long keepAliveSeconds,
                                              String threadNameFormat,
                                              BlockingQueue<Runnable> workQueue,
                                              RejectedExecutionHandler rejectHandler,
                                              final ExecuteListener executeListener){

        if (executeListener == null) {
            ExecutorService executorService = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveSeconds,
                    TimeUnit.SECONDS,
                    workQueue,
                    new CompatThreadFactoryBuilder().setNameFormat(threadNameFormat).build(),
                    rejectHandler != null ? rejectHandler : new ThreadPoolExecutor.AbortPolicy());
            synchronized (POOL) {
                POOL.add(executorService);
            }
            return executorService;
        } else {
            ExecutorService executorService = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveSeconds,
                    TimeUnit.SECONDS,
                    workQueue,
                    new CompatThreadFactoryBuilder().setNameFormat(threadNameFormat).build(),
                    rejectHandler != null ? rejectHandler : new ThreadPoolExecutor.AbortPolicy()) {
                @Override
                protected void beforeExecute(Thread t, Runnable r) {
                    super.beforeExecute(t, r);
                    executeListener.beforeExecute(t, r);
                }

                @Override
                protected void afterExecute(Runnable r, Throwable t) {
                    super.afterExecute(r, t);
                    executeListener.afterExecute(r, t);
                }
            };
            synchronized (POOL) {
                POOL.add(executorService);
            }
            return executorService;
        }
    }

    /**
     * 将所有通过此工具创建的ExecutorService停止(shutdownNow, 实际上是向线程发送interrupt信号, 并不是直接杀死线程),
     * 谨慎使用此方法, 调用后之前所有创建的ExecutorService都将无法使用, 通常在停止服务时调用.
     */
    public static void allShutdownNow(){
        synchronized (POOL) {
            for (ExecutorService executorService : POOL) {
                if (executorService != null) {
                    try {
                        executorService.shutdownNow();
                    } catch (Throwable ignore){
                    }
                }
            }
        }
    }

    public interface ExecuteListener {

        /**
         * 在Runnable执行前调用
         * @param t 线程
         * @param r runnable
         */
        void beforeExecute(Thread t, Runnable r);

        /**
         * 在Runnable执行后调用
         * @param r runnable
         * @param t 线程
         */
        void afterExecute(Runnable r, Throwable t);

    }

}
