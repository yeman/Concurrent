package com.yjt.concurrent.netty.day03;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * TODO
 * ClassName: AcceptCompleteHandler
 * Date: 2019-10-31 21:39
 * author Administrator
 * version V1.0
 */
public class AcceptCompleteHandler implements CompletionHandler<AsynchronousSocketChannel,AsyncServerTimeHandler> {

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncServerTimeHandler attachment) {
        attachment.asynchronousServerSocketChannel.accept(attachment,this);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        result.read(byteBuffer,byteBuffer,new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncServerTimeHandler attachment) {
        attachment.countDownLatch.countDown();

    }
}
