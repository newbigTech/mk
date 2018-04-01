package com.hand.hmall.process.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * @author 马君
 * @version 0.1
 * @name AbstractProcessExecutor
 * @description 流程多线程执行对象
 * @date 2017/9/4 10:37
 */

public abstract class AbstractProcessExecutor<T> implements Callable<T> {

    // 流程节点操作数据
    protected T proccessData;

    // 流程管理器
    protected ProcessManager<T> processManager;

    abstract T execute(T proccessData);

    @Override
    public T call() throws Exception {
        return execute(proccessData);
    }
}
