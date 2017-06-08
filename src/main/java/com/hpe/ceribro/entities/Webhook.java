package com.hpe.ceribro.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Webhook implements Serializable {

    private Long id;

    @NonNull
    private String url;

    @NonNull
    private String domain;

    @NonNull
    private String project;

    @NonNull
    private String module;


}
