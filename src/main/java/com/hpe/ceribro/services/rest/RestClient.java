package com.hpe.ceribro.services.rest;

import com.hp.iris.sdk.api.RestRequest;

import javax.ws.rs.core.GenericType;

public interface RestClient {

    RestClientResponse<String> put(RestRequest request);

    RestClientResponse<String> post(RestRequest request);

    <T> RestClientResponse<T> post(RestRequest request, GenericType<T> responseEntityType);

    RestClientResponse<String> get(RestRequest request);

    <T> RestClientResponse<T> get(RestRequest request, Class<T> responseEntityType);

    <T> RestClientResponse<T> get(RestRequest request, GenericType<T> responseEntityType);

    RestClientResponse<Object> getWithConnectionOpen(RestRequest request);
    
    void setAuthentcation(String userName, String password);
}
