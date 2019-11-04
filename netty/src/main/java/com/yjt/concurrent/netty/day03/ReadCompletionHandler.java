package com.yjt.concurrent.netty.day03;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * TODO
 * ClassName: ReadCompletionHandler
 * Date: 2019-10-31 21:52
 * author Administrator
 * version V1.0
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel socketChannel;

    public ReadCompletionHandler(AsynchronousSocketChannel socketChannel) {
        if(this.socketChannel==null){
            this.socketChannel = socketChannel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
       byte[] body =  new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String request = new String(body,"UTF-8");
            System.out.println("服务器接收请求:"+request);
            String currenttime = "QUERY TIME".equalsIgnoreCase(request) ? new Date(System.currentTimeMillis()).toString() : "BAD REQUEST";
            doWrite(currenttime);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String currenttime) {
        if(currenttime!=null && currenttime.length()>0){
            byte[] bytes = currenttime.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            socketChannel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    if(buffer.hasRemaining()){
                        socketChannel.write(buffer,buffer,this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer buffer) {
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
