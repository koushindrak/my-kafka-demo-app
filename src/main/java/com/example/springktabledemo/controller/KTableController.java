package com.example.springktabledemo.controller;

import com.example.springktabledemo.config.Constants;
import com.example.springktabledemo.model.User;
import com.example.springktabledemo.service.TopicToKTableGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/ktable/states/")
public class KTableController {


    private final TopicToKTableGenerator topicToKTableGenerator;
    private final ObjectMapper objectMapper;

    @Autowired
    public KTableController(TopicToKTableGenerator topicToKTableGenerator, ObjectMapper objectMapper) {
        this.topicToKTableGenerator = topicToKTableGenerator;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/color")
    public Map<String, Long> getAllFromKTable() {
        KafkaStreams streams = topicToKTableGenerator.getStreams();

        ReadOnlyKeyValueStore<String, Long> keyValueStore =
                streams.store(StoreQueryParameters.fromNameAndType("CountsByColours", QueryableStoreTypes.keyValueStore()));

        Map<String, Long> result = new HashMap<>();

        try (KeyValueIterator<String, Long> iterator = keyValueStore.all()) {
            while (iterator.hasNext()) {
                KeyValue<String, Long> entry = iterator.next();
                result.put(entry.key, entry.value);
                log.info("/ktable/state/color---" + entry.key + "-----" + entry.value);
            }
        }
        return result;
    }

    @GetMapping("/temp")
    public Map<String, String> getAllFromTempKTable() {
        KafkaStreams streams = topicToKTableGenerator.getStreams();

        ReadOnlyKeyValueStore<String, String> keyValueStore =
                streams.store(StoreQueryParameters.fromNameAndType(Constants.TEMP_KTABLE_STORE, QueryableStoreTypes.keyValueStore()));

        Map<String, String> result = new HashMap<>();

        try (KeyValueIterator<String, String> iterator = keyValueStore.all()) {
            while (iterator.hasNext()) {
                KeyValue<String, String> entry = iterator.next();
                result.put(entry.key, entry.value);
                log.info("/ktable/state/temp---" + entry.key + "-----" + entry.value);
            }
        }
        return result;
    }

    @GetMapping("/user")
    public Map<String, String> getAllUsersFromKTable() {
        KafkaStreams streams = topicToKTableGenerator.getStreams();

        ReadOnlyKeyValueStore<String, String> keyValueStore =
                streams.store(StoreQueryParameters.fromNameAndType(Constants.USER_KTABLE_STORE, QueryableStoreTypes.keyValueStore()));

        Map<String, String> result = new HashMap<>();

        try (KeyValueIterator<String, String> iterator = keyValueStore.all()) {
            while (iterator.hasNext()) {
                KeyValue<String, String> entry = iterator.next();
                result.put(entry.key, objectMapper.readValue(entry.value, User.class).toString());
                log.info("/ktable/state/user---" + entry.key + "-----" + entry.value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Return the result sorted by key
        return result.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getKey())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @GetMapping("/user-ranges")
    public Map<String, String> getAllUsersFromKTable(@RequestParam String from, @RequestParam String to) {
        KafkaStreams streams = topicToKTableGenerator.getStreams();

        ReadOnlyKeyValueStore<String, String> keyValueStore =
                streams.store(StoreQueryParameters.fromNameAndType(Constants.USER_KTABLE_STORE, QueryableStoreTypes.keyValueStore()));

        Map<String, String> result = new HashMap<>();

        try (KeyValueIterator<String, String> iterator = keyValueStore.range(from, to)) {
            while (iterator.hasNext()) {
                KeyValue<String, String> entry = iterator.next();
                result.put(entry.key, objectMapper.readValue(entry.value, User.class).toString());
                log.info("/ktable/state/user-ranges---" + entry.key + "-----" + entry.value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Return the result sorted by key
        return result.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getKey())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @GetMapping("/user-by-key/{key}")
    public Map<String, String> getAllUsersFromKTable(@RequestParam String key) throws JsonProcessingException {
        KafkaStreams streams = topicToKTableGenerator.getStreams();

        ReadOnlyKeyValueStore<String, String> keyValueStore =
                streams.store(StoreQueryParameters.fromNameAndType(Constants.USER_KTABLE_STORE, QueryableStoreTypes.keyValueStore()));

        Map<String, String> result = new HashMap<>();

        String value = keyValueStore.get(key);
        result.put(key, objectMapper.readValue(value, User.class).toString());
        log.info("/ktable/state/user-by-key/{key}---" + key + "-----" + value);


        // Return the result sorted by key
        return result.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getKey())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}
