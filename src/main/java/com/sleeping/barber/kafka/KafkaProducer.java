package com.sleeping.barber.kafka;



import java.util.Date;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer {

	 public static final long BARBER_TIME = 100;

	public static void main(String[] args) {
		
		Properties props = new Properties();
		 
		props.put("metadata.broker.list", "localhost:9092,localhost:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		 
		ProducerConfig config = new ProducerConfig(props);
		
		Producer<String, String> producer = new Producer<String, String>(config);
		String topic = "barberShop" ;
		
		for (int i = 1 ; i <= 10; i++) {
			
			String msg = new Date().toString() + ":customer_" + i;
			System.out.println(msg) ;
			
			KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, String.valueOf(i), msg);
			 
			producer.send(data);
            try {
                Thread.sleep(BARBER_TIME);
            } catch (InterruptedException ex) {
            }
		}
		producer.close();
	}
}
