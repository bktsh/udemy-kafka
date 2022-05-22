package com.bktsh.udemy.kafka.service;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.JsonParser;
import java.util.Arrays;

@Component
public class ImportantTweetFilter {

    private static final Serde<String> STRING_SERDE = Serdes.String();

//    @Autowired
//    void buildPipeline(StreamsBuilder streamsBuilder) {
//        KStream<String, String> messageStream = streamsBuilder
//                .stream("input-topic", Consumed.with(STRING_SERDE, STRING_SERDE));
//
//        KTable<String, Long> wordCounts = messageStream
//                .mapValues((ValueMapper<String, String>) String::toLowerCase)
//                .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
//                .groupBy((key, word) -> word, Grouped.with(STRING_SERDE, STRING_SERDE))
//                .count();
//
//        wordCounts.toStream().to("output-topic");
//    }

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        System.out.println(" 1------> ");
        KStream<String, String> inputTopic = streamsBuilder.stream("twitter_topic");
        KStream<String, String> filtered = inputTopic.filter(
                (k, jsonTweet) ->
                        //Filter tweets which has user of more than 10k followers
                        extractUserFollowersFromTweet(jsonTweet) > 10000);
        filtered.to("important_tweets");
        //3. Build  Topology
//        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), props);

        //4. Start our streams app
//        kafkaStreams.start();
        System.out.println(" 2------> ");

    }

    private static Integer extractUserFollowersFromTweet(String tweetJson) {
        try {
            return JsonParser.parseString(tweetJson)
                    //OR USE NOT-DEPRECATED VERSION
                    // return JsonParser.parseString(tweetJson)
                    .getAsJsonObject()
                    .get("user")
                    .getAsJsonObject()
                    .get("followers_count")
                    .getAsInt();
        }catch (Exception e){
            return 0;
        }
    }

}
