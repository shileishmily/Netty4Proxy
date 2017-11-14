package com.youguu.proxy;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by leo on 2017/11/14.
 */
public class NettyProxyInitalizer extends ChannelInitializer<SocketChannel> {

	private final String remoteHost;
	private final int remotePort;

	public NettyProxyInitalizer(String remoteHost, int remotePort) {
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new NettyProxyServerHandler(this.remoteHost, this.remotePort));
	}
}
