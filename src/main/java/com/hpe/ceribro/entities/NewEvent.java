package com.hpe.ceribro.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewEvent implements Serializable {

    @NonNull
    private String domain;

    @NonNull
    private String project;

    @NonNull
    private String type;

    @NonNull
    private int id;

    @NonNull
    private String last_modified;

    @NonNull
    private String status;

    public String webhookToString() {
        return String.format("{         return \"{ \"text\": \"Event of type %s happened in project %s Domain %s.\\n" +
                "%s id %s.\"\n" +
                "%s status %s\"\n" +
                "}", type, project, domain, type, id, type, status);
    }
}
