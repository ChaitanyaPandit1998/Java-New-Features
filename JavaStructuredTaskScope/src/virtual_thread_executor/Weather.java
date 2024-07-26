package virtual_thread_executor;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public record Weather(String server, String weather) {
    private static Instant begin;
    private static void getResultsFromServers() throws ExecutionException, InterruptedException {
        try(ExecutorService service = Executors.newCachedThreadPool()){
            begin = Instant.now();

            Future<Weather> futureA = service.submit(Weather::readWeatherFromServerA);
            Future<Weather> futureB = service.submit(Weather::readWeatherFromServerB);
            Future<Weather> futureC = service.submit(Weather::readWeatherFromServerC);

            Stream.of(futureA.get(),futureB.get(),futureC.get()).forEach(System.out::println);
        }
    }

    private static Weather readWeatherFromServerA() throws Exception {
        Thread.sleep(Duration.ofSeconds(10));
        return new Weather("Server A", "Sunny");
    }

    private static Weather readWeatherFromServerB() throws Exception {
        Thread.sleep(Duration.ofSeconds(2));
        System.out.println(STR."Time taken in throwing the exception: \{Duration.between(begin, Instant.now())}");
        throw new Exception("Exception occurred while receiving data from server");
    }

    private static Weather readWeatherFromServerC() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3));
        return new Weather("Server C", "Rainy");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        getResultsFromServers();
    }
}
