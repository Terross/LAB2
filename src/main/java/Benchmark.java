import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Benchmark {
    
    private static final int ELEMENT_COUNT = Integer.MAX_VALUE;

    public static void main(String[] args) throws InterruptedException {
        int threads = Integer.parseInt(args[0]);

        HarrisSet harrisSet = new HarrisSet();
        Random random1 = new Random();
        for (int i = 0; i < 10000; i++) {
            harrisSet.add(random1.nextInt(ELEMENT_COUNT));
        }
        ExecutorService es = Executors.newFixedThreadPool(threads);
        AtomicInteger addCount = new AtomicInteger(0);
        AtomicInteger removeCount = new AtomicInteger(0);
        for (int i = 0; i < threads; i ++) {
            es.execute(() -> {
                Random random = new Random();
                while (true) {
                    int rand = random.nextInt(0, 2);
                    if (rand == 1) {
                        if (harrisSet.add(random.nextInt(0, ELEMENT_COUNT))) {
                            addCount.getAndIncrement();
                        }
                    } else {
                        if (harrisSet.remove(random.nextInt(0, ELEMENT_COUNT - 1))) {
                            removeCount.getAndIncrement();
                        }
                    }
                }
            });
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.SECONDS);

        System.out.println(addCount.get() +removeCount.get());

    }
}
