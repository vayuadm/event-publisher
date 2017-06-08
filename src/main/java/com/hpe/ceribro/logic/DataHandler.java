package com.hpe.ceribro.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpe.ceribro.entities.Hit;
import com.hpe.ceribro.entities.IrisData;
import com.hpe.ceribro.exceptions.ManagerException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DataHandler {

    public static List<Hit> getEntities(String irisData) throws IOException {

        List<Hit> ret = new LinkedList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(irisData).get(0).get("hits");
        } catch (IOException e) {
            throw new ManagerException("Failed to read Iris Data", e);
        }

        if (jsonNode.get("total").asInt() > 0) { //we have results
            ret = mapper.readValue(jsonNode.toString(), IrisData.class).getHits();
        }

        return ret;
    }
}
