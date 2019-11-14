package com.yjt.concurrent.netty.day08.server;

import cn.hutool.core.io.FileUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import static cn.hutool.http.HttpStatus.*;

/**
 * TODO
 * ClassName: HttpFileHandler
 * Date: 2019-11-13 22:23
 * author Administrator
 * version V1.0
 */
public class HttpFileHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Pattern INSECURE_URL = Pattern.compile(".*[<>&\"].*");

    private static  final Pattern ALLOW_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    private final String url;

    public HttpFileHandler(String url) {
        this.url = url;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if(ctx.channel().isActive()){
            sendError(ctx,HTTP_INTERNAL_ERROR);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        //校验 http请求 解码是否成功
        if (!msg.decoderResult().isSuccess()) {
            sendError(ctx, HTTP_BAD_REQUEST);
            return;
        }
        //校验方法是否为post请求
        if (msg.method() != HttpMethod.GET) {
            sendError(ctx, HTTP_BAD_METHOD);
            return;
        }
        final String uri = msg.uri();
        final String path = sanitizeUrl(uri);
        //校验资源路径是否存在
        if (path == null) {
            sendError(ctx, HTTP_FORBIDDEN);
            return;
        }
        File file = FileUtil.newFile(path);
        //校验文件是否存在
        if (file.isHidden() || !file.exists()) {
            sendError(ctx, HTTP_NOT_FOUND);
            return;
        }
        //如果是目录
        if (FileUtil.isDirectory(file)) {
            if (uri.endsWith("/")) {
                listFile(ctx,file);
            } else {
                sendRedirect(ctx, uri + "/");
            }
        }
        if (!file.isFile()) {
            sendError(ctx, HTTP_FORBIDDEN);
            return;
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(ctx,HTTP_NOT_FOUND);
            return;
        }

        DefaultHttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,randomAccessFile.length());
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_OCTET_STREAM);
        if(HttpUtil.isKeepAlive(msg)){
            httpResponse.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        }
        ctx.write(httpResponse);
        //写文件
        ChannelFuture channelFuture;
        channelFuture = ctx.write(new ChunkedFile(randomAccessFile,0,randomAccessFile.length(),8192),
                ctx.newProgressivePromise());
        //通道处理监听
        channelFuture.addListener(new ChannelProgressiveFutureListener(){
            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.out.println("#### 传输完成");
            }

            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                if(total<0){
                    System.out.println("传输异常:"+ progress);
                }else{
                    System.out.println("正在传输,进度:"+ progress + "/"+total);
                }
            }
        });
        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        //关闭监听
        if(!HttpUtil.isKeepAlive(msg)){
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
    //重定向
    private void sendRedirect(ChannelHandlerContext ctx, String uri) {
        DefaultHttpResponse response =  new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION,uri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void listFile(ChannelHandlerContext ctx,File file) {
        DefaultFullHttpResponse response =  new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html;charset=UTF-8");
        StringBuilder buffer = new StringBuilder();
        String dirPath = file.getPath();
        buffer.append("<!DOCTYPE html>\r\n")
              .append("<html><head><title>")
              .append(dirPath)
              .append("目录")
              .append("</title></head><body>\r\n")
              .append("<h3>")
              .append(dirPath).append(" 目录: ")
              .append("</h3>\r\n")
              .append("<ul>")
              .append("<li>链接:<a href=\"../\"/>..</a></li>\r\n");
                for (File f:file.listFiles()) {
                   if(f.isHidden() || !f.canRead()){
                       continue;
                   }
                   String name = f.getName();
                   if(!ALLOW_FILE_NAME.matcher(name).matches()){
                       continue;
                   }
                    buffer.append("<li>链接: <a href=\"");
                    buffer.append(name);
                    buffer.append("\">");
                    buffer.append(name);
                    buffer.append("</a></li>\r\n");

                }
            buffer.append("</ul>").append("</body>").append("</html>\r\n");
            ByteBuf byteBuffer = Unpooled.copiedBuffer(buffer,CharsetUtil.UTF_8);
            response.content().writeBytes(byteBuffer);
            byteBuffer.release();
            buffer.setLength(0);//释放buffer
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    // uri处理
    private String sanitizeUrl(String uri) {
        try {
            uri =  URLDecoder.decode(uri,CharsetUtil.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            try {
                uri =  URLDecoder.decode(uri,CharsetUtil.ISO_8859_1.name());
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }
        if(!uri.startsWith(url)){
            return null;
        }
        if(!uri.startsWith("/")){
            return null;
        }
        uri = uri.replace("/",File.separator);
        if(uri.contains(File.separator+".")
           ||uri.contains("."+File.separator)
           ||uri.startsWith(".")
           ||uri.endsWith(".")
           ||INSECURE_URL.matcher(uri).matches()
        ){
            return null;
        }
        return System.getProperty("user.dir")+ uri;
    }
    //错误请求头
    private void sendError(ChannelHandlerContext ctx, int status) {
        DefaultHttpResponse response =  new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.valueOf(status),
                Unpooled.copiedBuffer("Failure"+status+"\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.TEXT_PLAIN);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
