package com.jay.date.match;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jay
 */
@Component
public class MessageSender {

    private final ChannelMap channelMap;

    @Autowired
    public MessageSender(ChannelMap channelMap) {
        this.channelMap = channelMap;
    }

    public void sendResponse(Channel channel, int code, String message, Object data){
        if(channel != null){
            String response = generateJsonResponse(code, message, data);
            TextWebSocketFrame frame = new TextWebSocketFrame(response);
            channel.writeAndFlush(frame);
        }
    }

    public void sendResponse(Integer userId, int code, String message, Object data){
        Channel channel = channelMap.getChannel(userId);
        if(channel != null){
            String response = generateJsonResponse(code, message, data);
            TextWebSocketFrame frame = new TextWebSocketFrame(response);
            channel.writeAndFlush(frame);
        }
    }

    private String generateJsonResponse(int code, String message, Object data){
        Map<String, Object> response = new HashMap<>(16);
        response.put("code", code);
        response.put("message", message);
        response.put("data", data);
        return JSON.toJSONString(response);
    }
}
