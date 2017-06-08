package com.hpe.ceribro.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpe.ceribro.entities.defect.Source;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hit {


    @JsonProperty("_index")
    @NonNull
    private String index;

    @JsonProperty("_type")
    @NonNull
    private String type;

    @JsonProperty("_id")
    @NonNull
    private String id;

    @JsonProperty("_source")
    @NonNull
    private Source source;
}
