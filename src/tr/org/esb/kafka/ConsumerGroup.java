package tr.org.esb.kafka;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumerGroup {
	
	static final ConcurrentHashMap.KeySetView<String, Boolean> urls = ConcurrentHashMap.newKeySet();
	
	private ConsumerGroup(){}
	
	public static void createConsumerGroup(int numConsumers, String topic){
		ExecutorService executor = Executors.newFixedThreadPool(numConsumers);
		for (int i = 0; i < numConsumers; i++) {
			executor.submit(new Consumer(topic));
		}
	}
}
