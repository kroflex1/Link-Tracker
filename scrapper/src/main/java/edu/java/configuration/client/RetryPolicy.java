package edu.java.configuration.client;

import edu.java.client.retry.LinearRetry;
import java.time.Duration;
import org.hibernate.service.spi.ServiceException;
import reactor.util.retry.Retry;

public enum RetryPolicy {
    LINEAR,
    CONSTANT,
    EXPONENTIAL;

    public Retry getRetry(int maxAttempts, Duration duration) {
        if (this == RetryPolicy.LINEAR) {
            return new LinearRetry(maxAttempts, duration);
        }
        if (this == RetryPolicy.EXPONENTIAL) {
            return Retry.backoff(maxAttempts, duration).filter(throwable -> throwable instanceof ServiceException);
        }
        return Retry.fixedDelay(maxAttempts, duration);
    }
}
