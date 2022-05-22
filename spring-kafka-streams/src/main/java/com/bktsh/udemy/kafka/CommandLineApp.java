//package com.bktsh.udemy.kafka;
//
//import org.apache.kafka.streams.KafkaStreams;
//import org.apache.kafka.streams.StoreQueryParameters;
//import org.apache.kafka.streams.state.QueryableStoreTypes;
//import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.kafka.config.StreamsBuilderFactoryBean;
//
//
//@SpringBootApplication
//public class CommandLineApp implements CommandLineRunner {
//    @Autowired
//    StreamsBuilderFactoryBean factoryBean;
//
//    public static void main(String[] args) {
//        System.out.println("STARTING THE APPLICATION");
//        SpringApplication.run(CommandLineApp.class, args);
//        System.out.println("APPLICATION FINISHED");
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
//        ReadOnlyKeyValueStore<String, Long> counts = kafkaStreams.store(StoreQueryParameters.fromNameAndType("counts", QueryableStoreTypes.keyValueStore())
//        );
//        System.out.println("count --> " + counts.get("bitcoin"));
//    }
//}
