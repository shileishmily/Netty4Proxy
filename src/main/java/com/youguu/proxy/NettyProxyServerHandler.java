package com.youguu.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

public class NettyProxyServerHandler extends ChannelInboundHandlerAdapter {

	private String remoteHost;
	private int remotePort;

	private Channel outBoundChannel;

	public NettyProxyServerHandler(String remoteHost, int remotePort) {
		super();
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		final Channel inboundChannel = ctx.channel();

		if (outBoundChannel == null || !ctx.channel().isActive()) {
			/* 创建netty client,连接到远程地址 */
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(ctx.channel().eventLoop())
					.channel(ctx.channel().getClass())
					.option(ChannelOption.AUTO_READ, false)
					.handler(new RemoteInitalizer(ctx.channel()));

			ChannelFuture future = bootstrap.connect(remoteHost, remotePort);
			outBoundChannel = future.channel();

        	/* channel建立成功后,将请求发送给远程主机 */
			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						inboundChannel.read();
					} else {
						inboundChannel.close();
					}
				}

			});
		}
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		if (outBoundChannel.isActive() && outBoundChannel.isOpen()) {
			outBoundChannel.writeAndFlush(msg).addListener(
					new ChannelFutureListener() {
						@Override
						public void operationComplete(ChannelFuture future) {
							if (future.isSuccess()) {
								ctx.channel().read();
							} else {
								future.channel().close();
							}
						}
					});
		}
	}
}
