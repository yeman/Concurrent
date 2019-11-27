package com.yjt.concurrent.netty.day09;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * TODO
 * ClassName: HttpXmlRequest
 * Date: 2019-11-20 22:04
 * author Administrator
 * version V1.0
 */
public class HttpXmlRequest {

    private FullHttpRequest httpRequest;
    private Object body;

    public HttpXmlRequest(FullHttpRequest httpRequest, Object body) {
        this.httpRequest = httpRequest;
        this.body = body;
    }

    public final FullHttpRequest getHttpRequest() {
        return httpRequest;
    }

    public final void setHttpRequest(FullHttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public Object getBody() {
        return body;
    }

    public final void setBody(Object body) {
        this.body = body;
    }

}
