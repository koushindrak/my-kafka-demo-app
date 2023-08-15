package com.example.springktabledemo;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class TopicToKTableGenerator {

    private final StreamsConfig kafkaStreamsConfig;

    private final StreamsBuilder builder;

    private KafkaStreams streams;

    @PostConstruct
    public void generateKTable() {
        log.info("======== INSIDE generateKTable METHOD ===========");
        // Step 1: Read from Stream apply some operation and publish to temp topic
        inputToTemp(builder);

        // Step 2: read from Temp, apply aggregations and return KTable
        KTable<String, Long> favouriteColoursKTable = tempToKTable(builder);

        //Step 3: - Publish KTable Result to Output Topic
//        kTableToOutput(favouriteColoursKTable);

        streams = new KafkaStreams(builder.build(), kafkaStreamsConfig);
        // only do this in dev - not in prod
//        streams.cleanUp();
        streams.start();

        // print the topology
        streams.localThreadsMetadata().forEach(data -> System.out.println(data));

        // shutdown hook to correctly close the streams application
//        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }

    private static void kTableToOutput(KTable<String, Long> favouriteColoursKTable) {
        favouriteColoursKTable.toStream().to(Constants.OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
    }

    private static KTable<String, Long> tempToKTable(StreamsBuilder builder) {
        Serde<String> stringSerde = Serdes.String();
        Serde<Long> longSerde = Serdes.Long();

        // step 2 - we read that topic as a KTable so that updates are read correctly
        KTable<String, String> usersAndColoursTable = builder.table(Constants.TEMP_TOPIC);

        // step 3 - we count the occurences of colours
        KTable<String, Long> favouriteColours = usersAndColoursTable
                // 5 - we group by colour within the KTable
                .groupBy((user, colour) -> new KeyValue<>(colour, colour))
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("CountsByColours")
                        .withKeySerde(stringSerde)
                        .withValueSerde(longSerde));
        return favouriteColours;
    }

    private static void inputToTemp(StreamsBuilder builder) {
        KStream<String, String> textLines = builder.stream(Constants.INPUT_TOPIC);
        textLines.peek((key,value)-> log.info(String.format("==== KEY======>>>%s,    VALUE =======>>>>>>%s",key,value)));
        KStream<String, String> usersAndColours = textLines
                // 1 - we ensure that a comma is here as we will split on it
                .filter((key, value) -> value.contains(","))
                // 2 - we select a key that will be the user id (lowercase for safety)
                .selectKey((key, value) -> value.split(",")[0].toLowerCase())
                // 3 - we get the colour from the value (lowercase for safety)
                .mapValues(value -> value.split(",")[1].toLowerCase())
                // 4 - we filter undesired colours (could be a data sanitization step
                .filter((user, colour) -> Arrays.asList("green", "blue", "red").contains(colour));

        usersAndColours.to(Constants.TEMP_TOPIC);
    }

    public KafkaStreams getStreams() {
        return streams;
    }
}
