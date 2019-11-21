package com.yjt.concurrent.netty.day09;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * TODO
 * ClassName: HttpXmlResponse
 * Date: 2019-11-20 22:04
 * author Administrator
 * version V1.0
 */
public class HttpXmlResponse {

    private FullHttpResponse httpResponse;
    private Object result;

    public HttpXmlResponse(FullHttpResponse httpResponse, Object result) {
        this.httpResponse = httpResponse;
        this.result = result;
    }

    public final FullHttpResponse getHttpResponse() {
        return httpResponse;
    }

    public final void setHttpResponse(FullHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
