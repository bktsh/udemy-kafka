package com.bktsh.udemy.kafka;

import com.google.gson.JsonParser;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ElasticSearchConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConsumer.class);

    public static final String bootstrapServer = "127.0.0.1:9092";
    public static final String groupId="elasticsearch-demo";
    public static final String topic="twitter_topic";

    private static JsonParser jsonParser = new JsonParser();

    public static final String hostName = "localhost";
    public static final String userName = "";//""elastic";
    public static final String password = "";//"password";



    public static void main(String[] args) throws IOException, InterruptedException {
        ElasticSearchConsumer consumer = new ElasticSearchConsumer();
        RestHighLevelClient client =  consumer.createClient();

        KafkaConsumer<String ,String> kafkaConsumer = consumer.createConsumer(topic);
        while(true){
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
            int count = records.count();
            logger.info("Number of received records: " + count);
            BulkRequest bulkRequest =  new BulkRequest();
            for(ConsumerRecord<String, String> record : records){
                //ID generation strategies
                //1. Kafka generic ID
                //String id = r.topic() + "_" + r.partition() + "_" + r.offset();
                //2. twitter feed specific approach
                try {
                    String id = extractIdFromtweet(record.value());
                    //logger.info(String.format("[key:%s, value: %s]", r.key(), r.value()));
                    IndexRequest indexRequest = new IndexRequest("twitter", "tweets", id).source(record.value(), XContentType.JSON);
                    bulkRequest.add(indexRequest);
                }catch (Exception e){
                    logger.error("Skipping bad data!");
                }
                Thread.sleep(5000);
//                NON-BULK-REQUEST
//                IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
//                String responseId =  indexResponse.getId();
//                logger.info("Inserted record to elastic search, id -> " + responseId);

            }
            if(count > 0 ) {
                BulkResponse responses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            }
            logger.info("Committing offsets!");
            kafkaConsumer.commitSync();
            logger.info("Committed!");
        }
        //        client.close();
    }

    private static String extractIdFromtweet(String tweetJson) {
        return jsonParser.parse(tweetJson).getAsJsonObject().get("id_str").getAsString();
    }

    public KafkaConsumer<String, String> createConsumer(String topic) {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_DOC, "earliest");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));
        return consumer;
    }
    public RestHighLevelClient createClient() {

        CredentialsProvider credProvider = new BasicCredentialsProvider();
        credProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(hostName, 9200, "http"))
                .setHttpClientConfigCallback(httpAsyncClientBuilder ->
                httpAsyncClientBuilder.setDefaultCredentialsProvider(credProvider)
        );
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }
}
