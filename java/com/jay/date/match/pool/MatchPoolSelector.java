package com.jay.date.match.pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 匹配池选择器
 * 为匹配请求选择匹配池
 * @author Jay
 */
@Component
public class MatchPoolSelector {
    private final MatchPoolContainer poolContainer;
    private final Logger logger;

    @Autowired
    public MatchPoolSelector(MatchPoolContainer poolContainer) {
        this.poolContainer = poolContainer;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * 为新请求选择匹配池
     * @param userInfo 封装的用户信息
     * @return 选择结果
     */
    public boolean selectPool(MatchPoolUserInfo userInfo){
        List<MatchPool> pools = poolContainer.getPools();
        for(MatchPool pool : pools){
            if(pool.addLast(userInfo)){
                return true;
            }
        }
        logger.info("userId={} 无法进入任何匹配池", userInfo.getUserId());
        return false;
    }
}
