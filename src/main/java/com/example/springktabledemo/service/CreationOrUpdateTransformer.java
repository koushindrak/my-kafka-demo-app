//package com.example.springktabledemo.service;
//
//import com.example.springktabledemo.config.Constants;
//import com.example.springktabledemo.model.User;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.kafka.streams.KeyValue;
//import org.apache.kafka.streams.StoreQueryParameters;
//import org.apache.kafka.streams.kstream.Transformer;
//import org.apache.kafka.streams.processor.ProcessorContext;
//import org.apache.kafka.streams.state.KeyValueStore;
//import org.apache.kafka.streams.state.QueryableStoreTypes;
//import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CreationOrUpdateTransformer implements Transformer<String, String, KeyValue<String, String>> {
//
//    private KeyValueStore<String, String> stateStore;
//    private ProcessorContext context;
//
//    private final ObjectMapper objectMapper;
//
//    public CreationOrUpdateTransformer(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public void init(ProcessorContext context) {
//        this.context = context;
//        this.stateStore = (KeyValueStore<String, String>) this.context.getStateStore(Constants.USER_KTABLE_STORE);
//    }
//
//    @Override
//    public KeyValue<String, String> transform(String key, String value) {
//        if(stateStore == null){
//            return  null;
//        }
//        String previousValue = stateStore.get(key);
//
//        if (previousValue == null) {
//            return null;
//        } else {
//            return new KeyValue<>(key, previousValue);
//        }
//    }
//
//    public User getUser(Integer key){
//        ReadOnlyKeyValueStore<String, String> keyValueStore =
//                streams.store(StoreQueryParameters.fromNameAndType(Constants.USER_KTABLE_STORE, QueryableStoreTypes.keyValueStore()));
//        keyValueStore.ge
//        transform(key+"",null);
//        if(stateStore == null){
//            return  null;
//        }
//        String previousValue = stateStore.get(key+"");
//
//        if (previousValue == null) {
//            return null;
//        } else {
//            User user = objectMapper.convertValue(previousValue,User.class);
//            return user;
//        }
//    }
//
//    @Override
//    public void close() {
//        // Any cleanup can be done here
//    }
//}
