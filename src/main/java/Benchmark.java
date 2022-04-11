import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Benchmark {

//    private static final int ELEMENT_COUNT = 256;

    private static final int ELEMENT_COUNT = 1 << 16;
    private static final List<Integer> threadsCount = List.of(1, 2, 4, 8, 16, 32, 64);

    public static void main(String[] args) throws InterruptedException {
//        insertTest();
//        removeTest();
    }

    private static void insertTest() throws InterruptedException {
        for (Integer threads:
             threadsCount) {
            ExecutorService es = Executors.newFixedThreadPool(threads);
            HarrisAMRLinkedList harrisAMRLinkedList = new HarrisAMRLinkedList();
            final int BATCH_SIZE = ELEMENT_COUNT/threads;
            long time = System.currentTimeMillis();
            for (int i = 0; i < threads; i++) {
                int finalI = i;
                es.execute(() -> {
                    for (int j = BATCH_SIZE; j < (finalI + 1) * BATCH_SIZE; j++) {
                        harrisAMRLinkedList.add(j);
                    }
                });
            }
            es.shutdown();
            es.awaitTermination(100, TimeUnit.SECONDS);
            String answer = "%d elements has been inserted in %d ms with %d threads".formatted(ELEMENT_COUNT, (System.currentTimeMillis() - time), threads);
            System.out.println(answer);
            harrisAMRLinkedList.printSet();
        }
    }

    private static void removeTest() throws InterruptedException {

        for (Integer threads:
                threadsCount) {
            ExecutorService es = Executors.newFixedThreadPool(threads);
            HarrisAMRLinkedList harrisAMRLinkedList = new HarrisAMRLinkedList();
            fillSet(harrisAMRLinkedList);
            final int BATCH_SIZE = ELEMENT_COUNT/threads;
            long time = System.currentTimeMillis();
            for (int i = 0; i < threads; i++) {
                int finalI = i;
                es.execute(() -> {
                    for (int j = finalI * BATCH_SIZE; j < (finalI + 1) * BATCH_SIZE; j++) {
                        harrisAMRLinkedList.remove(j);
                    }
                });
            }
            es.shutdown();
            es.awaitTermination(100, TimeUnit.SECONDS);
            String answer = "%d elements has been removed in %d ms with %d threads".formatted(ELEMENT_COUNT, (System.currentTimeMillis() - time), threads);
            System.out.println(answer);
            harrisAMRLinkedList.printSet();
        }
    }

    private static void fillSet(HarrisAMRLinkedList harrisAMRLinkedList) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(64);
        final int BATCH_SIZE = ELEMENT_COUNT/64;
        long time = System.currentTimeMillis();
        for (int i = 0; i < 64; i++) {
            int finalI = i;
            es.execute(() -> {
                for (int j = finalI * BATCH_SIZE; j < (finalI + 1) * BATCH_SIZE; j++) {
                    harrisAMRLinkedList.add(j);
                }
            });
        }
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
        String answer = "%d elements has been inserted in %d ms with %d threads".formatted(ELEMENT_COUNT, (System.currentTimeMillis() - time), 64);
        System.out.println(answer);
    }

}
