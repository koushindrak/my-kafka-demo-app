package com.example.springktabledemo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class KTableController {


    private final StreamsConfig kafkaStreamsConfig;

    @GetMapping("/ktable/all")
    public Map<String, Long> getAllFromKTable() {
        StreamsBuilder builder = new StreamsBuilder();
        KTable<String, String> usersAndColoursTable = builder.table(Constants.OUTPUT_TOPIC);
        KafkaStreams streams = new KafkaStreams(builder.build(), kafkaStreamsConfig);

        ReadOnlyKeyValueStore<String, Long> keyValueStore =
               streams.store(StoreQueryParameters.fromNameAndType("CountsByColours", QueryableStoreTypes.keyValueStore()));

//        KeyValueStore<String, Long> store = streams.store("CountsByColours", QueryableStoreTypes.keyValueStore());
        Map<String, Long> result = new HashMap<>();

        try (KeyValueIterator<String, Long> iterator = keyValueStore.all()) {
            while (iterator.hasNext()) {
                KeyValue<String, Long> entry = iterator.next();
                result.put(entry.key, entry.value);
                log.info("--------key----value"+entry.key+"-----"+entry.value);
            }
        }
        return result;
    }
}
