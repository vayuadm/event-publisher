package com.hpe.ceribro;

import com.hpe.ceribro.configuration.EventPublisherConfiguration;
import com.hpe.ceribro.entities.Hit;
import com.hpe.ceribro.logic.DataHandler;
import com.hpe.ceribro.services.IrisService;
import com.hpe.ceribro.services.ManagerService;
import com.hpe.ceribro.services.SlackService;
import com.hpe.ceribro.utils.TimeUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventPublisher {
    
    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    @Autowired
    private IrisService irisService;

    @Autowired
    private SlackService _slackService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EventPublisherConfiguration configuration;

//    private final String managerUrl = "http://localhost:8080";
//    private final String irisUrl = "http://myd-vm25739.hpeswlab.net:8080";
//    private final String index = "alm@DEMO@demo@642e6eac-23ae-41e9-973d-81d900796606";
    
    @Scheduled(fixedRate = 1000)
    public void task() throws JSONException {

//        String now = TimeUtils.getCurrentTimePlusMinutesFormatted(-1, "yyyy-MM-dd HH:mm:ss");
//        String prev = TimeUtils.getCurrentTimePlusMinutesFormatted(-50, "yyyy-MM-dd HH:mm:ss");
        String now = TimeUtils.getCurrentTimePlusSecondsFormatted(-3, "yyyy-MM-dd HH:mm:ss");
        String prev = TimeUtils.getCurrentTimePlusSecondsFormatted(-3, "yyyy-MM-dd HH:mm:ss");

        try {
            String type = "defect";
            log.info(String.format("Fetching defects for interval [%s] - [%s].", prev, now));
            String irisData =
                    irisService.searchNewEntityEvents(configuration.getIrisIndex(), type, 0, 20, prev, now, configuration.getIrisUrl());
            List<Hit> hits = DataHandler.getEntities(irisData);

            if (!hits.isEmpty()) {
                log.info(String.format("Got %s hits.", hits.size()));

                String[] splitIndex = configuration.getIrisIndex().split("@");
                String domain = splitIndex[1];
                String project = splitIndex[2];

                log.info("Getting webhooks addresses.");
                List<String> webhookUris = managerService.getIndexEntityUris(configuration.getEventRegistratorUrl(), domain, project, type);


                if (!webhookUris.isEmpty()) {
                    log.info("Sending to webhooks.");
                    hits.forEach(hit -> _slackService.postToSlack(webhookUris, hit));
                } else {
                    log.info("No webhooks found.");
                }
            } else {
                log.info("No defects found.");
            }
        } catch (Exception e) {
            log.error(String.format("Exception while retrieving data from iris: [%s]", e.toString()), e);
        }

    }
    
}