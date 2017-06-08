package com.hpe.ceribro.entities.defect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpe.ceribro.utils.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefectFields {

    @JsonProperty("description")
    @NonNull
    private String description;

    @JsonProperty("project")
    @NonNull
    private String project;

    @JsonProperty("last-modified")
    @NonNull
    private String lastModified;

    @JsonProperty("creation-time")
    @NonNull
    private String creationTime;

    @JsonProperty("severity")
    @NonNull
    private String severity;

    @JsonProperty("owner")
    @NonNull
    private String owner;

    @JsonProperty("detected-by")
    @NonNull
    private String detectedBy;

    @JsonProperty("name")
    @NonNull
    private String name;

    @JsonProperty("status")
    @NonNull
    private String status;

    public String getProject() {

        return StringUtils.isNullOrEmpty(project)?"":project.split("@")[2];
    }

    public String webhookToString() {

        return "{ \"text\": \"Event of type Defect happened in project: " + project +".\\n" +
                "Defect name: " + name +".\\n" +
                "Defect status: " + status + ".\"" +
                "}";
    }
}
