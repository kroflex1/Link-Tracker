package edu.java.retryPolicy;

import java.time.Duration;
import org.hibernate.service.spi.ServiceException;
import org.reactivestreams.Publisher;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class LinearRetry extends Retry {
    private final int maxAttempts;
    private final Duration stepTime;

    public LinearRetry(int maxAttempts, Duration stepTime) {
        this.maxAttempts = maxAttempts;
        this.stepTime = stepTime;
    }

    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> retrySignals) {
        return retrySignals.flatMap(this::getRetry);
    }

    private Mono<Long> getRetry(Retry.RetrySignal rs) {
        if (rs.totalRetries() < maxAttempts && rs.failure() instanceof ServiceException) {
            Duration delay = stepTime.multipliedBy(rs.totalRetries());
            return Mono.delay(delay).thenReturn(rs.totalRetries());
        } else {
            throw Exceptions.propagate(rs.failure());
        }
    }
}
