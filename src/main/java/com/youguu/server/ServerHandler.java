package com.youguu.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetAddress;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println("receive client msg:" + msg);
		ctx.writeAndFlush("hello client !\n");
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("have a new connection : ip[" + ctx.channel().remoteAddress() + "]");
		ctx.writeAndFlush("connection scuccess !\n");
		super.channelActive(ctx);
	}
}