package com.jay.date.match.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 匹配池整理器
 * 压缩器定时执行，防止匹配池数据不均匀
 * 将所有用户靠前分布，提高匹配成功率
 * @author Jay
 */
@Component
public class Compressor {
    private final MatchPoolContainer poolContainer;
    private final Logger logger;
    private final ScheduledThreadPoolExecutor executor;

    private static final Boolean enabled = true;
    /**
     * 整理器执行周期
     */
    private static final int COMPRESSION_PERIOD = 20000;
    /**
     * 整理器开始延迟
     */
    private static final int INITIAL_DELAY = 20000;

    @Autowired
    public Compressor(MatchPoolContainer poolContainer) {
        this.poolContainer = poolContainer;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.executor = new ScheduledThreadPoolExecutor(1, r -> new Thread(r, "c-thread-0"));
    }

    @PostConstruct
    private void init(){
        // 提交定时任务
        if(!enabled){
            executor.scheduleAtFixedRate(this::compression, INITIAL_DELAY, COMPRESSION_PERIOD, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 定时整理匹配池
     * 尽量将用户整理到靠前的匹配池中
     */
    public void compression() {
        logger.info("匹配池 整理开始");
        List<MatchPool> pools = poolContainer.getPools();
        /*
            整理开始，先将所有匹配线程打断
            匹配线程被打断后会调用park()暂停
         */
        for(MatchPool pool : pools){
            pool.lock.lock();
        }
        long startTime = System.currentTimeMillis();
        /*
            整理匹配池
            保证每个匹配池的用户量在一个均匀的水平
         */

        logger.info("匹配池 整理完成，耗时：{}ms", System.currentTimeMillis() - startTime);

        // 重启匹配线程
        for(MatchPool pool : pools){
            pool.lock.unlock();
        }
    }
}
