package com.hpe.ceribro.services.rest;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.StatusType;
import java.io.InputStream;
import java.util.Map;

public class RestClientResponseImpl<TEntity> implements RestClientResponse<TEntity> {
    
    private final TEntity entity;
    private final InputStream entityStream;
    private final Map<String, NewCookie> cookies;
    private final StatusType status;

    public RestClientResponseImpl(TEntity entity, Map<String, NewCookie> cookies, StatusType status) {

        this.entity = entity;
        this.cookies = cookies;
        this.status = status;
        entityStream = null;
    }

    public RestClientResponseImpl(InputStream entityStream, Map<String, NewCookie> cookies, StatusType status) {

        entity = null;
        this.cookies = cookies;
        this.status = status;
        this.entityStream = entityStream;
    }

    /**
     *
     * @return TEntity, can be null.
     */
    @Override
    public TEntity getEntity() {

        return entity;
    }

    /**
     *
     * @return entity as stream, can be null.
     */
    @Override
    public InputStream getEntityStream() {

        return entityStream;
    }

    @Override
    public StatusType getStatusInfo() {
        
        return status;
    }
    
    @Override
    public Map<String, NewCookie> getCookies() {
        
        return cookies;
    }
}
