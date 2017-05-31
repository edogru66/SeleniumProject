import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class Main {

	public static void main(String[] args) {

		Properties props = new Properties();
		props.put("bootstrap.servers", "10.0.15.106:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		Producer<Integer, String> producer = new KafkaProducer<>(props);
		Scanner scan = null;
		try {
			scan = new Scanner(new File("file.txt"));
			Integer i = 0;
			while (scan.hasNextLine()){
				producer.send(new ProducerRecord<>("test", i++, scan.nextLine()));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scan.close();
		}

		producer.close();

	}
}
