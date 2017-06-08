package com.hpe.ceribro.configuration;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "eventPublisherConfiguration")
public class EventPublisherConfiguration {

    private static final Logger _logger = LoggerFactory.getLogger(EventPublisherConfiguration.class);

    private String eventRegistratorUrl;

    private String irisUrl;

    private String irisIndex;

}
