package spider;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConcurrentSpider {

	public static class Indexer implements Runnable {
		private BlockingQueue<Integer> dataQueue;

		public Indexer(BlockingQueue<Integer> dataQueue) {
			this.dataQueue = dataQueue;
		}

		@Override
		public void run() {
			Integer i;
			while (!Thread.interrupted()) {
				try {
					i = dataQueue.take();
					System.out.println("���ѣ�" + i);
					TimeUnit.MILLISECONDS.sleep(1100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class Spider implements Runnable {
		private BlockingQueue<Integer> dataQueue;
		private static int i = 0;

		public Spider(BlockingQueue<Integer> dataQueue) {
			this.dataQueue = dataQueue;
		}

		@Override
		public void run() {
			while (!Thread.interrupted()) {
				try {
					dataQueue.add(new Integer(++i));
					System.out.println("������" + i);
					TimeUnit.MILLISECONDS.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		BlockingQueue<Integer> dataQueue = new LinkedBlockingQueue<Integer>(5);

		Thread pt = new Thread(new Spider(dataQueue));
		pt.start();

		Thread ct = new Thread(new Indexer(dataQueue));
		ct.start();
	}

}
