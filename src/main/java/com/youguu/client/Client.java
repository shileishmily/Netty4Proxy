package com.youguu.client;

import com.youguu.config.ProxyConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

	/**
	 * 服务器IP和端口，如果使用代理，可以配置成代理服务器的IP和端口
	 */
	public static String host = ProxyConfig.getString("server.ip");
	public static int port = ProxyConfig.getInteger("server.port");

//	public static String host = ProxyConfig.getString("proxy.ip");
//	public static int port = ProxyConfig.getInteger("proxy.port");

	public static void main(String[] args) throws InterruptedException, IOException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ClientInitializer());

			Channel ch = b.connect(host, port).sync().channel();

			//控制台输入，模拟向服务器发送消息
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			for (; ; ) {
				String line = in.readLine();
				if (line == null) {
					continue;
				}
				ch.writeAndFlush(line + "\r\n");
			}
		} finally {
			group.shutdownGracefully();
		}
	}
}