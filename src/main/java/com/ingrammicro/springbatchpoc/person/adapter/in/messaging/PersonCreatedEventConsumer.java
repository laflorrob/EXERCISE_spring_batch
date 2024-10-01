package com.ingrammicro.springbatchpoc.person.adapter.in.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import com.ingrammicro.springbatchpoc.person.port.in.CreatePersonService;
import com.ingrammicro.springbatchpoc.person.domain.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PersonCreatedEventConsumer {

    private final CreatePersonService createPersonService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PersonCreatedEventConsumer(CreatePersonService createPersonService) {
        this.createPersonService = createPersonService;
    }

    @ServiceActivator(inputChannel = "pubSubInputChannel")
    public void messageReceiver(String payload,
                                @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {

        log.info("Message arrived! Payload: {}", payload);

        try {
            createPersonService.create(objectMapper.readValue(payload, Person.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        message.ack();

        log.info("Message consumed!");

    }



}
