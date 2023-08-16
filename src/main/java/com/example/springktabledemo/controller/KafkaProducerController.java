package com.example.springktabledemo.controller;

import com.example.springktabledemo.config.Constants;
import com.example.springktabledemo.model.User;
import com.example.springktabledemo.service.TopicToKTableGenerator;
import com.example.springktabledemo.utils.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produce")
@Slf4j
public class KafkaProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String[] colors = {"red", "blue", "green"};
    public static Long messageCount = 0L;

    private final TopicToKTableGenerator topicToKTableGenerator;
    private final ObjectMapper objectMapper;


    public KafkaProducerController(KafkaTemplate<String, String> kafkaTemplate, TopicToKTableGenerator topicToKTableGenerator, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicToKTableGenerator = topicToKTableGenerator;
        this.objectMapper = objectMapper;
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

    @GetMapping("/user/count")
    @Async
    public void produceUserMessages(@RequestParam Integer from,@RequestParam Integer to) throws JsonProcessingException {
        log.info("=============Producing Messages========");
        KafkaStreams streams = topicToKTableGenerator.getStreams();

        ReadOnlyKeyValueStore<String, String> keyValueStore =
                streams.store(StoreQueryParameters.fromNameAndType(Constants.USER_KTABLE_STORE, QueryableStoreTypes.keyValueStore()));

        for (int i = from; i <= to; i++) {
//            messageCount++;
            String userString =  keyValueStore.get(String.valueOf(i));
            User user;
            if(userString == null){
                user = new User("user"+i, CommonUtils.getCurrentTime(),1);
            }else {
                user = objectMapper.readValue(userString,User.class);
                user.setStatus(user.getStatus()+1);
            }

            String data = objectMapper.writeValueAsString(user);

            kafkaTemplate.send(Constants.USER_INPUT_TOPIC , i+"",data);
            log.info("/produce/user/count/ ---"+messageCount+"=="+data);
        }

        log.info((to-from)+"   ******** MESSAGES Produced SUCCESSFULLY FROM /product/user/count/api ***********");
    }
}
