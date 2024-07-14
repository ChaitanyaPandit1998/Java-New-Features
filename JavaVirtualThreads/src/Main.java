import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        getTotalTimeUsingVirtualThreadPool();
        getTotalTimeUsingCachedThreadPool();
    }

    private static void getTotalTimeUsingVirtualThreadPool(){
        var begin = Instant.now();
        try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
            IntStream.range(0,100_000).forEach(task-> executor.submit(()->{
                Thread.sleep(Duration.ofSeconds(1));
                return task;
            }));
        }
        var end = Instant.now();
        System.out.println("Time Taken Using Virtual Threads: " + Duration.between(begin,end));
    }

    private static void getTotalTimeUsingCachedThreadPool(){
        var begin = Instant.now();
        try(ExecutorService executor = Executors.newCachedThreadPool()){
            IntStream.range(0,100_000).forEach(task-> executor.submit(()->{
                Thread.sleep(Duration.ofSeconds(1));
                return task;
            }));
        }
        var end = Instant.now();
        System.out.println("Time Taken Using Platform Threads: " + Duration.between(begin,end));
    }
}