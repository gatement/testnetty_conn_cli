package lgh.testnetty.conn.cli.handler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
@Sharable
public class ClientChannelHandler extends SimpleChannelInboundHandler<byte[]> {
	private Logger logger = LoggerFactory.getLogger(ClientChannelHandler.class);

	@Value("${app.ping.interval.sec}")
	private int pingIntervalSec;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("connected");

		// send 'ping' in periodically
		ctx.executor().scheduleAtFixedRate(() -> {
			ctx.writeAndFlush(new byte[] { 0x01 });
			logger.info("sent: ping");
		}, pingIntervalSec, pingIntervalSec, TimeUnit.SECONDS);

		ctx.fireChannelActive();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		byte cmd = msg[0];
		switch (cmd) {
		case 0x02:
			logger.info("got: pong");
			break;
		default:
			logger.info("got unknow cmd: {}", cmd);
		}
	}
}