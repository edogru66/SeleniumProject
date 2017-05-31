import tr.org.esb.kafka.ConsumerGroup;

public class Test {

	public static void main(String[] args) {

		/*
		 * Producer Thread
		 */
		new Thread(() -> Main.main(null)).start();
		
		/*
		 * Consumer Thread
		 */
		new Thread(() -> ConsumerGroup.createConsumerGroup(1, "test")).start();
	}
}
