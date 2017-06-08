package com.hpe.ceribro.services.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.hp.iris.sdk.api.RestRequest;
import org.apache.commons.lang.CharEncoding;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

@Component
public class RestClientImpl implements RestClient {

    private static final Logger _logger = LoggerFactory.getLogger(RestClientImpl.class);
    private final int READ_TIMEOUT;
    private Client client;

    public RestClientImpl() {
        this(300000);
    }

    public RestClientImpl(int readTimeout) {

        READ_TIMEOUT = readTimeout;
        final JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        client =
                ClientBuilder.newBuilder().register(JacksonFeature.class).property(
                        ClientProperties.CONNECT_TIMEOUT,
                        30000).property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT).
                        withConfig(new ClientConfig(jacksonJsonProvider)).build();
    }

    /**
     * post the request, response entity is read as GenericType<T> and input stream is closed.
     */
    @Override
    public <T> RestClientResponse<T> post(RestRequest request, GenericType<T> responseEntityType) {

        Response response = doPost(request);

        return new RestClientResponseImpl<>(
                response.readEntity(responseEntityType),
                response.getCookies(),
                response.getStatusInfo());
    }

    /**
     * post the request, response entity is read as string and input stream is closed.
     */
    @Override
    public RestClientResponse<String> post(RestRequest request) {

        return doPost(request, String.class);
    }

    /**
     * perform get, response entity is read as string and input stream is closed.
     */
    @Override
    public RestClientResponse<String> get(RestRequest request) {

        return doGet(request, String.class);
    }

    /**
     * perform get, response entity is read as T and input stream is closed.
     */
    @Override
    public <T> RestClientResponse<T> get(RestRequest request, Class<T> responseEntityType) {

        return doGet(request, responseEntityType);
    }

    /**
     * get the request, response entity is read as GenericType<T> and input stream is closed.
     */
    @Override
    public <T> RestClientResponse<T> get(RestRequest request, GenericType<T> responseEntityType) {

        Response response = doGet(request);

        return new RestClientResponseImpl<>(
                response.readEntity(responseEntityType),
                response.getCookies(),
                response.getStatusInfo());
    }

    /**
     * perform get, response entity returned as input-stream. caller is responsible for closing the
     * response stream.
     */
    @Override
    public RestClientResponse<Object> getWithConnectionOpen(RestRequest request) {

        Response response = doGet(request);

        return new RestClientResponseImpl<>(response.hasEntity()
                ? (InputStream) response.getEntity()
                : null, response.getCookies(), response.getStatusInfo());
    }

    @Override
    public RestClientResponse<String> put(RestRequest request) {

        _logger.debug(String.format("Calling HTTP PUT, URI: %s", request.getUri()));
        WebTarget webResource = client.target(request.getUri());
        Entity<Object> entity = Entity.entity(request.getEntity(), request.getRequestMediaType());
        Response response =
                webResource.request().accept(request.getResponseMediaType()).put(entity);
        checkResponse(response);

        return new RestClientResponseImpl<>(
                response.readEntity(String.class),
                response.getCookies(),
                response.getStatusInfo());
    }

    @Override
    public void setAuthentcation(String userName, String password) {

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(userName, password);
        client.register(feature);
    }

    private <T> RestClientResponse<T> doGet(RestRequest request, Class<T> responseEntityType) {

        Response response = doGet(request);

        return new RestClientResponseImpl<>(
                response.readEntity(responseEntityType),
                response.getCookies(),
                response.getStatusInfo());
    }

    private Response doGet(RestRequest request) {

        logHttpGet(request);
        Builder builder = prepareRequest(request);
        Response response = builder.accept(request.getResponseMediaType()).get();
        checkResponse(response);

        return response;
    }

    private <T> RestClientResponse<T> doPost(RestRequest request, Class<T> responseEntityType) {

        Response response = doPost(request);

        return new RestClientResponseImpl<>(
                response.readEntity(responseEntityType),
                response.getCookies(),
                response.getStatusInfo());
    }

    private Response doPost(RestRequest request) {

        _logger.debug(String.format("Calling HTTP POST, URI: %s", request.getUri()));
        Builder builder = prepareRequest(request);
        Entity<Object> entity = Entity.entity(request.getEntity(), request.getRequestMediaType());
        Response response = builder.accept(request.getResponseMediaType()).post(entity);
        checkResponse(response);

        return response;
    }

    private void logHttpGet(RestRequest request) {

        _logger.debug(String.format(
                "Calling HTTP GET, URI: %s, Parameters: %s",
                request.getUri(),
                request.getQueryParams().toString()));
    }

    private void checkResponse(javax.ws.rs.core.Response response) throws RestClientException {

        Response.StatusType statusInfo = response.getStatusInfo();
        if (statusInfo.getFamily() != Family.SUCCESSFUL) {
            String responseStr = "";
            try (Scanner scanner =
                         new Scanner((InputStream) response.getEntity(), CharEncoding.UTF_8)) {
                responseStr = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            }
            throw new RestClientException(
                    String.format(
                            "Failed : HTTP error code: %d, response message: %s",
                            statusInfo.getStatusCode(),
                            responseStr.isEmpty() ? statusInfo.getReasonPhrase() : responseStr),
                    statusInfo.getStatusCode(),
                    responseStr);
        }
    }

    private Builder prepareRequest(RestRequest request) {

        WebTarget webTarget = client.target(request.getUri());
        Map<String, String[]> queryParams = request.getQueryParams();
        if (queryParams != null) {
            for (Entry<String, String[]> param : queryParams.entrySet()) {
                webTarget = webTarget.queryParam(param.getKey(), param.getValue());
            }
        }
        Builder builder = webTarget.request();
        for (NewCookie currCookie : request.getCookies()) {
            builder = builder.cookie(currCookie.toCookie());
        }
        for (Entry<String, Object> currEntry : request.getHeaders().entrySet()) {
            builder = builder.header(currEntry.getKey(), currEntry.getValue());
        }

        return builder;
    }
}
