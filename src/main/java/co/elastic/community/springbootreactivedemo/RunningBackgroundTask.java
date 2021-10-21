package co.elastic.community.springbootreactivedemo;

import co.elastic.apm.api.CaptureSpan;
import co.elastic.apm.api.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RunningBackgroundTask {

    private static final Logger log = LoggerFactory.getLogger(RunningBackgroundTask.class);

    @Traced
    @Scheduled(fixedRate = 1000 * 60) // once per minute
    public void executeTask() {
        final long start = System.nanoTime();
        log.info("Starting background task");
        sleep(10);
        sleep(5);
        sleep(15);
        final long end = System.nanoTime();
        log.info("Finished background task in [{}]s", Duration.ofNanos(end - start).toSeconds());
    }

    @CaptureSpan
    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
        }
    }
}
