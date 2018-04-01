package com.hand.promotion.job;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.hand.promotion.util.ThreadPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * @author XinyangMei
 * @Title BaseJob
 * @Description 抽象job模板，这里的job只是为了开机启动
 * @date 2017/12/11 16:33
 */
public abstract class BaseJob implements CommandLineRunner, Runnable {

    /**
     * 执行job对应的业务逻辑
     */
    public abstract void doExecute();

    @Override
    public void run() {
        doExecute();
    }

    @Override
    public void run(String... strings) {
        ThreadPoolUtil.submit(this);
    }
}
