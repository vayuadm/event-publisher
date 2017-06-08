package com.hpe.ceribro.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IrisData {

    @JsonProperty("hits")
    @NonNull
    private List<Hit> hits;
}
