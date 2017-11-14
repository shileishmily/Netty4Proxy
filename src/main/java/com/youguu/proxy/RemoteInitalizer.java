package com.youguu.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by leo on 2017/11/14.
 */
public class RemoteInitalizer extends ChannelInitializer<SocketChannel> {
	private Channel inboundChannel;

	public RemoteInitalizer(Channel inboundChannel){
		this.inboundChannel = inboundChannel;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new NettyProxyClientHandler(this.inboundChannel));
	}
}
