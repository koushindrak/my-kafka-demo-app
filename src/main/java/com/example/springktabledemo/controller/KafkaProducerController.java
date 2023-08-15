package com.example.springktabledemo.controller;

import com.example.springktabledemo.config.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produce")
@Slf4j
public class KafkaProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String[] colors = {"red", "blue", "green"};
    public static Long messageCount = 0L;

    public KafkaProducerController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/count/{count}")
    @Async
    public void produceMessages(@PathVariable Long count) {
        log.info("=============Producing Messages========");

        for (int i = 1; i <= count; i++) {
            messageCount++;
            String favoriteColor = colors[i % colors.length];
            String data = "user"+i+","+favoriteColor;
            kafkaTemplate.send(Constants.INPUT_TOPIC , data);
            log.info("/produce/count/ ---"+messageCount+"=="+data);
        }

        log.info(count+"   ******** MESSAGES Produced SUCCESSFULLY FROM /product/count/api ***********");
    }
}
