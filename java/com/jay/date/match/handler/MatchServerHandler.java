package com.jay.date.match.handler;

import com.alibaba.fastjson.JSON;
import com.jay.date.match.ChannelMap;
import com.jay.date.match.MessageSender;
import com.jay.date.match.pool.MatchPoolSelector;
import com.jay.date.match.pool.MatchPoolUserInfo;
import com.jay.date.util.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jay
 */
public class MatchServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static final int MATCH_POOL_BUSY = 0;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame frame) {
        String message = frame.text();
        // 获取selector
        MatchPoolSelector selector = (MatchPoolSelector)SpringUtil.getBean("matchPoolSelector");
        // 获取channelMap
        ChannelMap channelMap = (ChannelMap)SpringUtil.getBean("channelMap");
        // 解析json，获得用户信息
        MatchPoolUserInfo userInfo = JSON.parseObject(message, MatchPoolUserInfo.class);
        userInfo.setTimestamp(System.currentTimeMillis());
        // 保存用户channel
        channelMap.saveChannel(userInfo.getUserId(), channelHandlerContext.channel());
        logger.info("{} @ {} 提交匹配请求", userInfo.getUserId(), channelHandlerContext.channel().remoteAddress());
        // 为用户选择匹配池
        boolean userInMatchPool = selector.selectPool(userInfo);
        // 如果没有空闲匹配池，发送系统繁忙反馈
        if(!userInMatchPool){
            logger.info("匹配池已满, {} @ {} 无法加入", userInfo.getUserId(), channelHandlerContext.channel().remoteAddress());
            MessageSender sender = (MessageSender) SpringUtil.getBean("messageSender");
            sender.sendResponse(channelHandlerContext.channel(), MATCH_POOL_BUSY, "用户过多，系统繁忙", null);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)  {
        logger.info("{} 建立连接", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        // 获取channelMap
        ChannelMap channelMap = (ChannelMap)SpringUtil.getBean("channelMap");
        channelMap.removeChannel(ctx.channel());
        logger.info("{} 断开连接", ctx.channel().remoteAddress());
    }
}
