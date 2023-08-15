//package com.example.springktabledemo;
//
//import org.apache.kafka.streams.KafkaStreams;
//import org.apache.kafka.streams.StoreQueryParameters;
//import org.apache.kafka.streams.StreamsBuilder;
//import org.apache.kafka.streams.kstream.KStream;
//import org.apache.kafka.streams.state.KeyValueIterator;
//import org.apache.kafka.streams.state.QueryableStoreTypes;
//import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaStreamInit {
//    @Bean
//    public KafkaStreams kafkaStreams() {
//        StreamsBuilder builder = new StreamsBuilder();
//        KStream<String, String> textLines = builder.stream(Constants.INPUT_TOPIC);
//
//        KafkaStreams kafkaStreams = new KafkaStreams(...);
//        kafkaStreams.start();
//        return kafkaStreams;
//    }
//
//    @Autowired
//    private KafkaStreams kafkaStreams;
//
//    public KeyValueIterator<String, Long> getKTableData() {
//        ReadOnlyKeyValueStore<String, Long> keyValueStore =
//                kafkaStreams.store(StoreQueryParameters.fromNameAndType("my-state-store", QueryableStoreTypes.keyValueStore()));
//        return keyValueStore.all();
//    }
//
//}
