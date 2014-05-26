package com.seovic.chronos.http;


import com.seovic.chronos.Request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.lang.Override;
import java.lang.String;

import java.net.URI;


/**
 */
public class HttpRequest
        implements Request
    {
    private static final HttpClient CLIENT;

    static
        {
        PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
        cm.setMaxTotal(100);
        CLIENT = new DefaultHttpClient(cm);
        }

    private String name;
    private HttpUriRequest request;

    public HttpRequest(String name, HttpMethod method, URI uri)
        {
        this.name = name;
        this.request = createRequest(method, uri);
        }

    /**
     * Prefix for the statistics published by this request.
     *
     * @return request name
     */
    @Override
    public String getName()
        {
        return name;
        }

    public void addHeader(String name, String value)
        {
        request.addHeader(name, value);
        }

    protected void setEntity(HttpEntity entity)
        {
        if (request instanceof HttpEntityEnclosingRequest)
            {
            ((HttpEntityEnclosingRequest) request).setEntity(entity);
            }
        else
            {
            throw new IllegalStateException("Entity cannot be set for a " + request.getMethod() + " request");
            }
        }

    public void setEntity(byte[] entity, ContentType contentType)
        {
        setEntity(new ByteArrayEntity(entity, contentType));
        }

    public void setEntity(File entity, ContentType contentType)
        {
        setEntity(new FileEntity(entity, contentType));
        }

    public void setEntity(InputStream entity, ContentType contentType)
        {
        setEntity(new InputStreamEntity(entity, -1L, contentType));
        }

    public void setEntity(String entity, ContentType contentType)
        {
        setEntity(new StringEntity(entity, contentType));
        }

    /**
     * Execute request.
     *
     * @return true if successful, false if failed
     */
    @Override
    public boolean execute()
        {
        try
            {
            HttpResponse response = CLIENT.execute(request);
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);

            return response.getStatusLine().getStatusCode() < 400;
            }
        catch (IOException e)
            {
            return false;
            }
        }

    private HttpUriRequest createRequest(HttpMethod method, URI uri)
        {
        switch (method)
            {
            case GET:
                return new HttpGet(uri);
            case POST:
                return new HttpPost(uri);
            case PUT:
                return new HttpPut(uri);
            case DELETE:
                return new HttpDelete(uri);
            case HEAD:
                return new HttpHead(uri);
            case OPTIONS:
                return new HttpOptions(uri);
            case PATCH:
                return new HttpPatch(uri);
            case TRACE:
                return new HttpTrace(uri);
            default:
                throw new IllegalArgumentException("Unknown HTTP method: " + method);
            }
        }
    }
