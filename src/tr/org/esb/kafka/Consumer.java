package tr.org.esb.kafka;

import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.openqa.selenium.TimeoutException;
import tr.org.esb.connection.DatabaseConnection;
import tr.org.esb.webpagecategorization.WebpageCategorization;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Properties;

public class Consumer extends ShutdownableThread {

	private final KafkaConsumer<Integer, String> consumer;
	private final String topic;
	private DatabaseConnection dbconn;

	public Consumer(String topic) {
		super("KafkaConsumer", false);
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.0.15.106:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "123");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");        
		consumer = new KafkaConsumer<>(props);
		this.topic = topic;
	}

	@Override
	public void doWork() {
		consumer.subscribe(Collections.singletonList(topic));
        try {
            dbconn = new DatabaseConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (true) {
			ConsumerRecords<Integer, String> records = consumer.poll(100);
			//if(records.isEmpty()) break;
			for (ConsumerRecord<Integer, String> record : records) {
				consumer.commitSync(); 
				System.out.println(record.offset() + ": " + record.value());
				if(ConsumerGroup.urls.contains(record.value())) {
					System.out.println("contains true");
					continue;
				}
				try {
					ConsumerGroup.urls.add(record.value());
					WebpageCategorization wc = new WebpageCategorization(record.value());
					dbconn.insertIntoDb(record.value(), wc);

				} catch (MalformedURLException | TimeoutException | SQLException e) {
					System.out.println(e.getClass().getName() + ": " + record.value());
				}
			}
		}
	}
}
