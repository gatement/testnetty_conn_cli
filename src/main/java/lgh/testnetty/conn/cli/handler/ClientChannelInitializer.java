package lgh.testnetty.conn.cli.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;;

@Component
public class ClientChannelInitializer extends ChannelInitializer<Channel> {
	@Autowired
	private ClientChannelHandler clientChannelHandler;

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1024, 0, 2, 0, 2));
		pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
		pipeline.addLast("bytesDecoder", new ByteArrayDecoder());
		pipeline.addLast("bytesEncoder", new ByteArrayEncoder());
		pipeline.addLast("busiHandler", clientChannelHandler);
	}
}
