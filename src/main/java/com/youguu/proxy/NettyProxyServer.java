package com.youguu.proxy;

import com.youguu.config.ProxyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

public class NettyProxyServer {
	private String remoteHost = ProxyConfig.getString("server.ip");
	private int remotePort = ProxyConfig.getInteger("server.port");

	public void start() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(ProxyConfig.getInteger("proxy.port")))
					.childOption(ChannelOption.AUTO_READ, false)
					.childHandler(new NettyProxyInitalizer(remoteHost, remotePort));

			ChannelFuture future = bootstrap.bind().sync();
			future.channel().closeFuture().sync();

		} finally {
			bossGroup.shutdownGracefully().sync();
			workerGroup.shutdownGracefully().sync();
		}
	}


	public static void main(String args[]) {
		NettyProxyServer server = new NettyProxyServer();
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
