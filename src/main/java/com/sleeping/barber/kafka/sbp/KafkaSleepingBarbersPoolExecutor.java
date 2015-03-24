package com.sleeping.barber.kafka.sbp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaSleepingBarbersPoolExecutor extends Thread {
	public static final int CHAIRS = 5;

	public static final long BARBER_TIME = 5000;

	private static final long CUSTOMER_TIME = 1500;

	public static final long OFFICE_CLOSE = BARBER_TIME * 2;

	private static final int BARBERS_NUMBER = 2;

	private static final String KAFKA_TOPIC = "barberShop";

	private ExecutorService barbersExecutor;
	private static ConsumerConnector consumer;

	class ClientsProducer extends Thread {
		public void run() {
			// run KAFKA message producer

			Properties props = new Properties();

			props.put("metadata.broker.list", "localhost:9092,localhost:9092");
			props.put("serializer.class", "kafka.serializer.StringEncoder");
			props.put("request.required.acks", "1");

			ProducerConfig config = new ProducerConfig(props);

			Producer<String, String> producer = new Producer<String, String>(
					config);
			// String topic = "barberShop" ;

			for (int i = 1; i <= 10; i++) {

				String msg = new Date().toString() + ":customer_" + i;
				System.out.println(msg);

				KeyedMessage<String, String> data = new KeyedMessage<String, String>(
						KAFKA_TOPIC, String.valueOf(i), msg);

				producer.send(data);
				try {
					Thread.sleep(CUSTOMER_TIME);
				} catch (InterruptedException ex) {
				}
			}
			producer.close();
		}
	}

	class Barber extends Thread {
		KafkaStream<byte[], byte[]> stream = null;
		private int name;

		public Barber(KafkaStream<byte[], byte[]> stream, int name) {
			this.name = name;
			this.stream = stream;
		}

		public void run() {
			ConsumerIterator<byte[], byte[]> it = stream.iterator();
			System.out.println("The barber  " + name + " starting"
					+ it.toString());

			while (it.hasNext()) {
				System.out.println("The barber  " + name
						+ ": is cutting hair for "
						+ new String(it.next().message()));
				try {
					Thread.sleep(BARBER_TIME);
				} catch (InterruptedException ex) {
				}
			}
			System.out.println("Shutting down Thread: " + name);
		}
	}

	private static ConsumerConfig createConsumerConfig(String a_zookeeper,
			String a_groupId) {
		Properties props = new Properties();
		props.put("zookeeper.connect", a_zookeeper);
		props.put("group.id", a_groupId);
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");

		return new ConsumerConfig(props);
	}

	public static void main(String args[]) {
		String zooKeeper = "localhost:2181";
		String groupId = "barberShop";

		consumer = kafka.consumer.Consumer
				.createJavaConsumerConnector(createConsumerConfig(zooKeeper,
						groupId));

		KafkaSleepingBarbersPoolExecutor barberShop = new KafkaSleepingBarbersPoolExecutor();

		barberShop.start(); // Let the simulation begin
	}

	public void run() {

		// barbers in the shop

		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(KAFKA_TOPIC, new Integer(BARBERS_NUMBER));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
				.createMessageStreams(topicCountMap);
		List<KafkaStream<byte[], byte[]>> streams = consumerMap
				.get(KAFKA_TOPIC);

		// now launch all the barber(s)

		barbersExecutor = Executors.newFixedThreadPool(BARBERS_NUMBER);

		// now create barbers to consume the messages
		System.out.println("Threads #: " + streams.size());

		int threadNumber = 0;
		for (final KafkaStream stream : streams) {
			System.out.println("Threads #: " + threadNumber);
			barbersExecutor.submit(new Barber(stream, threadNumber));
			threadNumber++;
		}

		// run KAFKA message producer
		ClientsProducer cp = new ClientsProducer();
		cp.start();
	}
}