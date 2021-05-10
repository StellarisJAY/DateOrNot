package com.jay.date.match;

import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储用户id与channel的映射
 * @author Jay
 */
@Component
public class ChannelMap {
    private final Map<Integer, Channel> channelMap = new ConcurrentHashMap<>();

    public void saveChannel(Integer userId, Channel channel){
        channelMap.put(userId, channel);
    }

    public Channel getChannel(Integer userId){
        return channelMap.get(userId);
    }

    /**
     * 删除用户id对应的channel
     * @param userId 用户id
     */
    public void removeChannel(Integer userId){
        channelMap.remove(userId);
    }

    /**
     * 删除channel
     * @param channel 被删除的channel
     */
    public void removeChannel(Channel channel){
        Set<Integer> keySet = channelMap.keySet();
        for(Integer key : keySet){
            if(channelMap.get(key) == channel){
                channelMap.remove(key);
                break;
            }
        }
    }
}
