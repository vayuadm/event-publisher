package com.hpe.ceribro.services;

import com.hp.iris.sdk.api.FieldsFilterDescriptor;
import com.hp.iris.sdk.api.IrisSearchRequest;
import com.hp.iris.sdk.api.SearchConfiguration;
import com.hpe.ceribro.exceptions.SearchException;
import com.hpe.ceribro.services.rest.RestClient;
import com.hpe.ceribro.utils.SearchUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IrisService {

    @Autowired
    private RestClient restClient;

    public String searchNewEntityEvents(String index, String type, int start, int size, String timeStamp, String now, String irisUrl) throws JSONException {

        List<String> indexes = new ArrayList<>(1);
        indexes.add(0, index);
        FieldsFilterDescriptor fieldsFilterDescriptor = new FieldsFilterDescriptor(null, null, SearchUtils.buildRange(timeStamp, now), null);

        SearchConfiguration searchConfiguration = new SearchConfiguration(indexes, fieldsFilterDescriptor);

        try {
            return restClient.post(new IrisSearchRequest(irisUrl,
                    searchConfiguration,
                    SearchUtils.buildQueryParams(type, start, size))).getEntity();
        } catch (JSONException e) {
            throw new SearchException(String.format(
                    "Failed create search request for index [%s] type [%s]",
                    index, type), e);
        }
    }
}
