package com.jay.date.match;

import com.jay.date.match.handler.MatchServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Jay
 */
@Component
public class MatchServer {
    private static final int PORT = 9002;
    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workerGroup;
    private final Logger logger;

    public MatchServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostConstruct
    public void init(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new MatchServerInitializer());

        try{
            ChannelFuture future = serverBootstrap.bind(PORT).sync();
            if(future.isSuccess()){
                logger.info("匹配服务器已启动，端口：" + PORT);
            }
        } catch (InterruptedException e) {
            logger.info("匹配服务器启动失败：" + e.getLocalizedMessage());
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
