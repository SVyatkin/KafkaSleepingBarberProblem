# Kafka Apache and Dijkstra's Sleeping Barber Problem
Please see details in my blog: www.vyatkins.wordpress.com
<strong>Summary</strong>

In this article, I'm going to show you how to use the Kafka Apache message broker for Dijkstra's Sleeping Barber problem that we have already discussed in a previous article.

<a title="Sleeping Barber Problem " href="https://github.com/SVyatkin/KafkaSleepingBarberProblem" target="_blank">GitHub Project Link </a>

<strong>Install Kafka</strong>  by <a title="quickstart" href="http://kafka.apache.org/documentation.html#quickstart" target="_blank">following instructions from the Kafka Apache</a>

$ tar -xzf kafka_2.10-0.8.2.0.tgz

$ cd kafka_2.10-0.8.2.0

Start a single-node ZooKeeper server:

$ bin/zookeeper-server-start.sh config/zookeeper.properties

Start the Kafka server:

$ bin/kafka-server-start.sh config/server.properties

Create a topic named "barberShop"

$ bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic barberShop

Check that the topic barberShop exists in the Kafka server

$  bin/kafka-topics.sh --list --zookeeper localhost:2181

<strong>Run the Java program</strong> <a title="Kafka broker with Sleeping Barber Problem" href="https://github.com/SVyatkin/KafkaSleepingBarberProblem/blob/master/src/main/java/com/sleeping/barber/kafka/sbp/KafkaSleepingBarbersPoolExecutor.java" target="_blank">from GitHub repo.</a>

If you use default settings for your Kafka server you may expect result log like below

[code language="text" title="Log"]
Threads #: 2
Threads #: 0
Threads #: 1
Tue Mar 24 15:52:56 PDT 2015:customer_1
The barber 1 startingnon-empty iterator
The barber 1: is cutting hair for Tue Mar 24 13:18:30 PDT 2015:customer_1
Tue Mar 24 15:52:57 PDT 2015:customer_2
Tue Mar 24 15:52:59 PDT 2015:customer_3
Tue Mar 24 15:53:00 PDT 2015:customer_4
The barber 1: is cutting hair for Tue Mar 24 13:18:30 PDT 2015:customer_2
Tue Mar 24 15:53:02 PDT 2015:customer_5
Tue Mar 24 15:53:03 PDT 2015:customer_6
Tue Mar 24 15:53:05 PDT 2015:customer_7
The barber 1: is cutting hair for Tue Mar 24 13:18:30 PDT 2015:customer_3
Tue Mar 24 15:53:06 PDT 2015:customer_8
Tue Mar 24 15:53:08 PDT 2015:customer_9
Tue Mar 24 15:53:09 PDT 2015:customer_10
The barber 1: is cutting hair for Tue Mar 24 13:18:30 PDT 2015:customer_4
The barber 1: is cutting hair for Tue Mar 24 13:18:30 PDT 2015:customer_5
The barber 1: is cutting hair for Tue Mar 24 13:18:30 PDT 2015:customer_6
The barber 1: is cutting hair for Tue Mar 24 13:18:30 PDT 2015:customer_7
The barber 1: is cutting hair for Tue Mar 24 13:18:31 PDT 2015:customer_8
The barber 1: is cutting hair for Tue Mar 24 13:18:31 PDT 2015:customer_9
The barber 1: is cutting hair for Tue Mar 24 13:18:31 PDT 2015:customer_10
The barber 1: is cutting hair for Tue Mar 24 15:52:56 PDT 2015:customer_1
The barber 1: is cutting hair for Tue Mar 24 15:52:57 PDT 2015:customer_2
The barber 1: is cutting hair for Tue Mar 24 15:52:59 PDT 2015:customer_3
The barber 1: is cutting hair for Tue Mar 24 15:53:00 PDT 2015:customer_4
The barber 1: is cutting hair for Tue Mar 24 15:53:02 PDT 2015:customer_5
The barber 1: is cutting hair for Tue Mar 24 15:53:03 PDT 2015:customer_6
The barber 1: is cutting hair for Tue Mar 24 15:53:05 PDT 2015:customer_7
The barber 1: is cutting hair for Tue Mar 24 15:53:06 PDT 2015:customer_8
The barber 1: is cutting hair for Tue Mar 24 15:53:08 PDT 2015:customer_9
The barber 1: is cutting hair for Tue Mar 24 15:53:09 PDT 2015:customer_10
[/code]

Also, you may check the Kafka queue for the "barberShop" topic from the command line.

$ bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic barberShop --from-beginning

You may adjust your Kafka server settings to get more convenient results.
