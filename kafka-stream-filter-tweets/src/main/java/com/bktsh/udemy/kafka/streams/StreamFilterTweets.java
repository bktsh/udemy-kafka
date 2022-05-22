package com.bktsh.udemy.kafka.streams;

import com.google.gson.JsonParser;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

import static org.apache.kafka.streams.StreamsConfig.*;

public class StreamFilterTweets {

    private static JsonParser jsonParser = new JsonParser();
    //kafka-console-consumer --bootstrap-server localhost:9092 --topic important_tweets --from-beginning
    public static void main(String[] args) {
        //1. Create properties
        Properties props = new Properties();
        props.setProperty(BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        props.setProperty(APPLICATION_ID_CONFIG,"demo-kafka-streams");
        props.setProperty(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
        props.setProperty(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());

        //2. Create Topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> inputTopic = streamsBuilder.stream("twitter_topic");
        KStream<String, String> filtered = inputTopic.filter(
                (k,jsonTweet) ->
                        //Filter tweets which has user of more than 10k followers
                        extractUserFollowersFromTweet(jsonTweet) > 10000);
        filtered.to("important_tweets");
        //3. Build  Topology
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), props);

        //4. Start our streams app
        kafkaStreams.start();

    }

    private static Integer extractUserFollowersFromTweet(String tweetJson) {
        try {
            return jsonParser.parse(tweetJson)
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
