package structuredtaskscope;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.stream.Stream;

public record Weather(String server, String weather) {

    private static void getResultsFromServers() throws ExecutionException, InterruptedException {
        try(var service = new StructuredTaskScope.ShutdownOnFailure()){

            Subtask<Weather> futureA = service.fork(Weather::readWeatherFromServerA);
            Subtask<Weather> futureB = service.fork(Weather::readWeatherFromServerB);
            Subtask<Weather> futureC = service.fork(Weather::readWeatherFromServerC);

            service.join();

            service.throwIfFailed();

            Stream.of(futureA.get(),futureB.get(),futureC.get()).forEach(System.out::println);
        }
    }

    private static Weather readWeatherFromServerA() throws Exception {
        Thread.sleep(Duration.ofSeconds(10));
//        throw new Exception("Exception occurred while receiving data from server");
        return new Weather("Server A", "Sunny");
    }
    private static Weather readWeatherFromServerB() throws Exception {
        Thread.sleep(Duration.ofSeconds(2));
        throw new Exception("Exception occurred while receiving data from server");
//        return new Weather("Server B", "Sunny");
    }
    private static Weather readWeatherFromServerC() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3));
        return new Weather("Server C", "Rainy");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        getResultsFromServers();
    }
}
