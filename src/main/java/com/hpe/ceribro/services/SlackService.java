package com.hpe.ceribro.services;

import com.hp.iris.sdk.api.RestRequest;
import com.hpe.ceribro.EventPublisher;
import com.hpe.ceribro.entities.Hit;
import com.hpe.ceribro.services.rest.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.util.List;

@Service
public class SlackService {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    @Autowired
    private RestClient restClient;

    public void postToSlack(List<String> uris, Hit hit) {

        uris.forEach(uri -> postToSlack(uri, hit));
    }

    private void postToSlack(String uri, Hit hit) {

        try {
            restClient.post(getPostToSlackRequest(uri, hit));
        } catch (Exception e) {
            log.error("Got error while sending slack post request", e);
        }
    }

    private RestRequest getPostToSlackRequest(String uri, Hit hit) {

        RestRequest ret = new RestRequest(uri,
                hit.getSource().getDefectFields().webhookToString(),
                MediaType.APPLICATION_JSON_TYPE.toString(),
                MediaType.APPLICATION_JSON_TYPE.toString());
        ret.addHeader("Content-Type", "application/json");

        return ret;
    }

}
