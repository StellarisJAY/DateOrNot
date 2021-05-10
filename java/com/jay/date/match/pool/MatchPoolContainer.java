package com.jay.date.match.pool;

import com.jay.date.util.SpringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 匹配池容器
 * 初始化和存储所有的匹配池
 * @author Jay
 */
@Component
public class MatchPoolContainer {
    /**
     * 匹配池个数
     */
    private static int POOL_COUNT;
    private final List<MatchPool> pools;

    public MatchPoolContainer(){
        pools = new ArrayList<>(POOL_COUNT);
    }

    @PostConstruct
    public void init(){
        for(int i = 0; i < POOL_COUNT; i++){
            pools.add(new MatchPool(i));
        }
    }

    public List<MatchPool> getPools() {
        return pools;
    }

    @Value("${matchpool.pool-count}")
    public void setPoolCount(int poolCount) {
        POOL_COUNT = poolCount;
    }
}
