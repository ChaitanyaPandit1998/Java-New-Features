import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public record Weather(String server, String weather) {
    private static final ExecutorService service = Executors.newCachedThreadPool();

    private static void getResultsFromServers() throws ExecutionException, InterruptedException {
        Future<Weather> futureA = service.submit(Weather::readWeatherFromServerA);
        Future<Weather> futureB = service.submit(Weather::readWeatherFromServerB);
        Future<Weather> futureC = service.submit(Weather::readWeatherFromServerC);
        Stream.of(futureA.get(),futureB.get(),futureC.get()).forEach(System.out::println);

    }

    private static Weather readWeatherFromServerA() throws Exception {
        Thread.sleep(Duration.ofSeconds(10));
//        throw new Exception("Exception occurred while receiving data from server");
        return new Weather("Server A", "Sunny");
    }

    private static Weather readWeatherFromServerB() throws Exception {
        Thread.sleep(Duration.ofSeconds(2));
        throw new Exception("Exception occurred while receiving data from server");
//        return new Weather("Server B", "Overcast");
    }

    private static Weather readWeatherFromServerC() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3));
        return new Weather("Server C", "Rainy");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        getResultsFromServers();
    }
}
