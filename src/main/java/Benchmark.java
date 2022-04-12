import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Benchmark {
    
    private static final int ELEMENT_COUNT = Integer.MAX_VALUE;
//    private static final List<Integer> threadsCount = List.of(512);

    public static void main(String[] args) throws InterruptedException {
        List<Integer> threadsCount = List.of(Integer.parseInt(args[0]));
        for (Integer threads: threadsCount) {
            int totalAdd = 0;
            int totalRemove = 0;
            HarrisAMRLinkedList harrisAMRLinkedList = new HarrisAMRLinkedList();
            Random random1 = new Random();
            for (int i = 0; i < 10000; i++) {
                harrisAMRLinkedList.add(random1.nextInt(ELEMENT_COUNT));
            }
//            fillSet(harrisAMRLinkedList, 1);
            for (int k = 0; k < 1; k++) {

                ExecutorService es = Executors.newFixedThreadPool(threads);
                AtomicInteger addCount = new AtomicInteger(0);
                AtomicInteger removeCount = new AtomicInteger(0);
                for (int i = 0; i < threads; i ++) {
                    es.execute(() -> {
                        Random random = new Random();
                        while (true) {
                            int rand = random.nextInt(0, 2);
                            if (rand == 1) {
                                if (harrisAMRLinkedList.add(random.nextInt(0, ELEMENT_COUNT))) {
                                    addCount.getAndIncrement();
                                }
                            } else {
                                if (harrisAMRLinkedList.remove(random.nextInt(0, ELEMENT_COUNT - 1))) {
                                    removeCount.getAndIncrement();
                                }
                            }
                        }
                    });
                }
                es.shutdown();
                es.awaitTermination(1, TimeUnit.SECONDS);
                totalAdd += addCount.get();
                totalRemove += removeCount.get();
//                System.out.println(addCount.get());
//                System.out.println(removeCount.get());
            }

            System.out.printf("Thread count = %d%n", threads);
//            System.out.printf("Add operations: %d%n", totalAdd/1);
//            System.out.printf("Remove operations: %d%n", totalRemove/1);
            System.out.println(totalAdd + totalRemove);
//            harrisAMRLinkedList.printSetElementsCount();
//            harrisAMRLinkedList.printSet();
        }
        System.exit(0);
    }



    private static void fillSet(HarrisAMRLinkedList harrisAMRLinkedList, int thread) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(thread);
        final int BATCH_SIZE = ELEMENT_COUNT/thread;
        long time = System.currentTimeMillis();
        for (int i = 0; i < thread; i++) {
            int finalI = i;
            es.execute(() -> {
                for (int j = finalI * BATCH_SIZE; j < (finalI + 1) * BATCH_SIZE; j++) {
                    harrisAMRLinkedList.add(j);
                }
            });
        }
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
    }



//    private static void insertTest() throws InterruptedException {
//        for (Integer threads:
//                threadsCount) {
//            ExecutorService es = Executors.newFixedThreadPool(threads);
//            HarrisAMRLinkedList harrisAMRLinkedList = new HarrisAMRLinkedList();
//            final int BATCH_SIZE = ELEMENT_COUNT/threads;
//            long time = System.currentTimeMillis();
//            for (int i = 0; i < threads; i++) {
//                int finalI = i;
//                es.execute(() -> {
//                    for (int j = finalI * BATCH_SIZE; j < (finalI + 1) * BATCH_SIZE; j++) {
//                        harrisAMRLinkedList.add(j);
//                    }
//                });
//            }
//            es.shutdown();
//            es.awaitTermination(100, TimeUnit.SECONDS);
//            String answer = "%d elements has been inserted in %d ms with %d threads".formatted(ELEMENT_COUNT, (System.currentTimeMillis() - time), threads);
//            System.out.println(answer);
////            harrisAMRLinkedList.printSet();
//        }
//    }

//    private static void removeTest() throws InterruptedException {
//
//        for (Integer threads:
//                threadsCount) {
//            ExecutorService es = Executors.newFixedThreadPool(threads);
//            HarrisAMRLinkedList harrisAMRLinkedList = new HarrisAMRLinkedList();
//            fillSet(harrisAMRLinkedList, 64);
//            final int BATCH_SIZE = ELEMENT_COUNT/threads;
//            long time = System.currentTimeMillis();
//            for (int i = 0; i < threads; i++) {
//                int finalI = i;
//                es.execute(() -> {
//                    for (int j = finalI * BATCH_SIZE; j < (finalI + 1) * BATCH_SIZE; j++) {
//                       harrisAMRLinkedList.remove(j);
//                    }
//                });
//            }
//            es.shutdown();
//            es.awaitTermination(100, TimeUnit.SECONDS);
//            String answer = "%d elements has been removed in %d ms with %d threads".formatted(ELEMENT_COUNT, (System.currentTimeMillis() - time), threads);
//            System.out.println(answer);
////            harrisAMRLinkedList.printSet();
//        }
//    }
}
