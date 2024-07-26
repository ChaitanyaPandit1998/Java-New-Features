package structuredtaskscope;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public record Weather(String server, String weather) {
    private static Instant begin;
    private static void getResultsFromServersWithFailure() throws ExecutionException, InterruptedException {
        try(var service = new StructuredTaskScope.ShutdownOnFailure()){
               begin = Instant.now();
               service.fork(Weather::readWeatherFromServerA);
               service.fork(Weather::readWeatherFromServerBThrowsException);
               service.fork(Weather::readWeatherFromServerC);

//             Blocking operation. Wait for all subtasks started in this task scope to complete or for a subtask to fail.
               service.join();

//            Throws exception if a subtask failed. Does nothing if no subtask fails
               service.throwIfFailed();

        }
    }

    private static void getResultsFromServersWithSuccess() throws ExecutionException, InterruptedException {
        try(var service = new StructuredTaskScope.ShutdownOnSuccess<Weather>()){
            begin = Instant.now();
            service.fork(Weather::readWeatherFromServerA);
            service.fork(Weather::readWeatherFromServerBWithSuccess);
            service.fork(Weather::readWeatherFromServerC);

            service.join();

            Weather result = service.result();

            System.out.println(result);
        }
    }

    private static Weather readWeatherFromServerA() throws Exception {
        Thread.sleep(Duration.ofSeconds(10));
        return new Weather("Server A", "Sunny");
    }

    private static Weather readWeatherFromServerBThrowsException() throws Exception {
        Thread.sleep(Duration.ofSeconds(2));
        System.out.println(STR."Time taken in throwing the exception: \{Duration.between(begin, Instant.now())}");
        throw new Exception("Exception occurred while receiving data from server");
    }

    private static Weather readWeatherFromServerBWithSuccess() throws Exception {
        Thread.sleep(Duration.ofSeconds(2));
        System.out.println(STR."Time taken in returning response: \{Duration.between(begin, Instant.now())}");
        return new Weather("Server B", "Overcast");
    }

    private static Weather readWeatherFromServerC() throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(3));
        return new Weather("Server C", "Rainy");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        getResultsFromServersWithSuccess();
        getResultsFromServersWithFailure();
    }
}
