package com.hand.common.job;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangwantao
 * @version 0.1
 * @name JobConcurrentManager
 * @description Job定时任务并发控制器，由于Quartz框架的调度器只能控制相同job定义的并发执行，但对于同一个Job的java类无法避免
 * 同时执行。此控制器使用Redis分布式锁实现对同一Java类做并发控制。
 * 使用说明：
 * 1、通过@Autowired注入到Job类中
 * 2、在safeExecute方法的第一行调用begin()方法或tryBegin()方法
 * 3、safeExecute方法内，必须try catch，在finally中调用finish()方法 ！！！！！！！！！！
 * @date 2017/10/10
 */
public class JobConcurrentManager {

    Logger logger = LoggerFactory.getLogger(JobConcurrentManager.class);

    private static final String JOB_LOCK_KEY_PREFIX = "hap_job_concurrent_lock::";

    @Autowired
    private RedissonClient redissonClient;


    /**
     * 获取锁，Job执行方法首行调用
     * 当有其他线程占用锁时，当前线程阻塞等待，否则继续向下执行
     */
    public void begin() {
        logger.info("{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}{}");
        logger.info("begin.getJobKey():" + getJobKey());
        RLock lock = redissonClient.getLock(getJobKey());
        logger.info(Thread.currentThread().getName() + ": Trying to lock...");
        lock.lock();
        logger.info(Thread.currentThread().getName() + " Locked");
    }

    /**
     * 获取锁，Job执行方法首行调用，锁被占用时放弃执行
     * 返回true时，表示当前线程已抢占锁成功，可以执行后续业务代码；返回false时，表示当前已经有其他线程正在占用锁，业务不可执行。
     */
    public boolean tryBegin() {
        String jobKey = getJobKey();
        logger.info(">>>>>>>>>>>  tryBegin: " + jobKey);
        RLock lock = redissonClient.getLock(jobKey);
        logger.info(Thread.currentThread().getName() + ": Trying to lock...");
        if (!lock.tryLock()) {
            logger.info(Thread.currentThread().getName() + ": Try lock failed, giving up...");
            return false;
        }
        logger.info(Thread.currentThread().getName() + " Locked");
        return true;
    }

    /**
     * 占用指定名称的锁，如果已被占用，则等待锁释放
     *
     * @param jobLockName
     */
    public void beginWithLock(String jobLockName) {
        logger.info("))))))))))  beginWithLock : " + jobLockName);
        RLock lock = redissonClient.getLock(getJobKey(jobLockName));
        logger.info(Thread.currentThread().getName() + ": Trying to lock with name: " + jobLockName);
        lock.lock();
        logger.info(Thread.currentThread().getName() + " Locked");
    }

    /**
     * 占用指定名称的锁，如果已被占用，则返回false，后续业务代码不可执行
     *
     * @param jobLockName
     * @return
     */
    public boolean tryBeginWithLock(String jobLockName) {
        logger.info("))))))))))  tryBeginWithLock : " + jobLockName);
        RLock lock = redissonClient.getLock(getJobKey(jobLockName));
        logger.info(Thread.currentThread().getName() + ": Trying to lock with name: " + jobLockName);
        if (!lock.tryLock()) {
            logger.info(Thread.currentThread().getName() + ": Try lock '" + jobLockName + "' failed, giving up...");
            return false;
        }
        return true;
    }

    /**
     * 释放锁，Job执行方法最后finally中调用
     */
    public void finish() {
        String jobKey = getJobKey();
        RLock lock = redissonClient.getLock(jobKey);
        finish(lock);
    }

    /**
     * 释放指定名称的job锁
     */
    public void finish(String jobLockName) {
        String jobKey = getJobKey(jobLockName);
        RLock lock = redissonClient.getLock(jobKey);
        finish(lock);
    }

    /**
     * 释放锁
     *
     * @param lock
     */
    private void finish(RLock lock) {
        try {
            if (lock.isHeldByCurrentThread() && lock.isLocked()) {
                lock.unlock();
                logger.info(Thread.currentThread().getName() + ". Job lock released.");
            }
        } catch (Exception e) {
        }
    }

    /**
     * 获得Job类redis锁的key（规则：prefix + 代码调用链中调用此类方法的class名称）
     *
     * @return
     */
    private String getJobKey() {
        return JOB_LOCK_KEY_PREFIX + getCaller();
    }

    /**
     * 根据指定job类名，获取Job redis锁的key
     *
     * @param jobClassName
     * @return
     */
    private String getJobKey(String jobClassName) {
        return JOB_LOCK_KEY_PREFIX + jobClassName;
    }

    /**
     * 获取当前调用该方法的class名称
     *
     * @return
     */
    private String getCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//        for (StackTraceElement e : stackTrace) {
//            logger.info(e.toString());
//        }
        StackTraceElement targetElement = null;
        // 从最后一条element开始查找，直到找到当前类（JobConcurrentManager）的那条，其下面一条则是调用该类的class
        for (int i = stackTrace.length - 1; i >= 0; i--) {
            StackTraceElement e = stackTrace[i];
            String className = e.getClassName();
            if (JobConcurrentManager.class.getCanonicalName().equals(className)) {
                targetElement = stackTrace[i + 1];
                break;
            }
        }
        return targetElement.getClassName();
    }
}
