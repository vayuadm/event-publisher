package com.hpe.ceribro.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.iris.sdk.api.RestRequest;
import com.hpe.ceribro.entities.Webhook;
import com.hpe.ceribro.exceptions.ManagerException;
import com.hpe.ceribro.services.rest.RestClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class ManagerService {

    @Autowired
    private RestClient restClient;

    public List<String> getIndexEntityUris(String uri, String domain, String project, String entity) throws IOException, JSONException {

        String retEntity;
        try {
            retEntity = restClient.get(new RestRequest(String.format("%s/webhooks/%s/%s/%s", uri, domain, project, entity),
                    null,
                    MediaType.APPLICATION_JSON_TYPE.toString(),
                    MediaType.APPLICATION_JSON_TYPE.toString())).getEntity();
            return getWebhooks(retEntity);
        } catch (Exception e) {
            throw new ManagerException(String.format(
                    "Failed get uri's for domain [%s] project [%s] entiry [%s]",
                    domain, project, entity), e);
        }

    }

    private List<String> getWebhooks(String json) throws JSONException, IOException {

        List<String> ret = new LinkedList<>();
        JSONArray jsonArray = new JSONArray(json);

        ObjectMapper mapper = new ObjectMapper();
        for (int i=0; i<jsonArray.length(); i++){
            ret.add(mapper.readValue(jsonArray.get(i).toString(), Webhook.class).getUrl());
        }

        return ret;
    }
}
