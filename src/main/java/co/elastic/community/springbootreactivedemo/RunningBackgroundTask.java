package co.elastic.community.springbootreactivedemo;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RunningBackgroundTask {

    private static final Logger log = LoggerFactory.getLogger(RunningBackgroundTask.class);
    private final Counter invocationCounter;
    private final Counter sleepCounter;

    public RunningBackgroundTask(MeterRegistry registry) {
        this.invocationCounter = registry.counter("tasks.RunningBackgroundTask.runs");
        this.sleepCounter = registry.counter("tasks.RunningBackgroundTask.sleepRuns");
    }

    @Scheduled(fixedRate = 1000 * 60) // once per minute
    public void executeTask() {
        final long start = System.nanoTime();
        log.info("Starting background task");
        sleep(10);
        sleep(5);
        sleep(15);
        final long end = System.nanoTime();
        log.info("Finished background task in [{}]s", Duration.ofNanos(end - start).toSeconds());
        invocationCounter.increment();
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
        }
        sleepCounter.increment();
    }
}
