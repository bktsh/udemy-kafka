
# Kafka Udemy course
~~~~~
# 1.
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties
# 2. 
kafka-server-start /usr/local/etc/kafka/server.properties
# 3.
#deprecated: #kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 6 --topic twitter_topic
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 6 --topic twitter_delete_connect   
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 6 --topic twitter_status_connect
kafka-console-consumer --bootstrap-server localhost:9092 --topic twitter_status_connect --from-beginning
kafka-console-consumer --bootstrap-server localhost:9092 --topic twitter_delete_connect --from-beginning
kafka-console-consumer --bootstrap-server localhost:9092 --topic important_tweets --from-beginning
~~~~~

---
## Elastic Search 

* home
  * Elasticsearch home directory or $ES_HOME   
  * /usr/local/var/homebrew/linked/elasticsearch-full

* bin
  *  Binary scripts including elasticsearch to start a node and elasticsearch-plugin to install plugins
  * /usr/local/var/homebrew/linked/elasticsearch-full/bin

* conf
    *   Configuration files including elasticsearch.yml
    *   /usr/local/etc/elasticsearch
    *   ES_PATH_CONF

* data
    * The location of the data files of each index / shard allocated on the node.
    * /usr/local/var/lib/elasticsearch
    * path.data

* logs
  *  Log files location.
  *  /usr/local/var/log/elasticsearch
  *  path.logs

* plugins
    * Plugin files location. Each plugin will be contained in a subdirectory.
    * /usr/local/var/homebrew/linked/elasticsearch/plugins

---
## Kibana
* home 
    *  Kibana home directory or $KIBANA_HOME
    * /usr/local/var/homebrew/linked/kibana-full

* bin
    * Binary scripts including kibana to start a node and kibana-plugin to install plugins
    * /usr/local/var/homebrew/linked/kibana-full/bin

* conf
  * Configuration files including kibana.yml
  * /usr/local/etc/kibana

* data
    *  The location of the data files of each index / shard allocated on the node. Can hold multiple locations.    
    *  /usr/local/var/lib/kibana
   * path.data

* logs
    *  Log files location.
    *    /usr/local/var/log/kibana
    *    path.logs

* plugins
    * Plugin files location. Each plugin will be contained in a subdirectory.
    * /usr/local/var/homebrew/linked/kibana-full/plugins
## Passwords 
Reserved users :
  * elastic/password
  * apm_system/password
  * kibana/password
  * kibana_system/password
  * logstash_system/password 
  * beats_system/password 
  * remote_monitoring_user/password