package com.youguu.proxy;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;


public class NettyProxyClientHandler extends ChannelInboundHandlerAdapter {
	private Channel inBoundChannel;

	public NettyProxyClientHandler(Channel inBoundChannel) {
		this.inBoundChannel = inBoundChannel;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.read();
		ctx.write(Unpooled.EMPTY_BUFFER);
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {

		inBoundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
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
