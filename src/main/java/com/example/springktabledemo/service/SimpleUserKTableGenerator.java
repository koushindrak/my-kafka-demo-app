package com.example.springktabledemo.service;

import com.example.springktabledemo.config.Constants;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
@Slf4j
public class SimpleUserKTableGenerator {


    private final StreamsBuilder builder;

    @Autowired
    public SimpleUserKTableGenerator(StreamsBuilder builder) {
        this.builder = builder;
    }

    public void generateUserKTable(){
      builder.table(Constants.USER_INPUT_TOPIC,getUserMetTable());
    }

    public static Materialized<String, String, KeyValueStore<Bytes, byte[]>> getUserMetTable(){
        Materialized<String, String, KeyValueStore<Bytes, byte[]>> materializedStore =
                Materialized.<String, String, KeyValueStore<Bytes, byte[]>>as(Constants.USER_KTABLE_STORE)
                        .withKeySerde(Serdes.String())
                        .withValueSerde(Serdes.String());
        return materializedStore;

    }
}
