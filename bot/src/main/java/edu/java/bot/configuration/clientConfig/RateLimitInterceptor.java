package edu.java.bot.configuration.clientConfig;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {
    private final BucketConfiguration bucketConfiguration;
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public RateLimitInterceptor(BucketConfiguration bucketConfiguration) {
        this.bucketConfiguration = bucketConfiguration;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = request.getRemoteAddr();
        cache.putIfAbsent(ip, createBucket());
        Bucket clientBucket = cache.get(ip);

        ConsumptionProbe probe = clientBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader(
                "X-Rate-Limit-Remaining",
                Long.toString(probe.getRemainingTokens())
            );
            return true;
        }

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.addHeader(
            "X-Rate-Limit-Retry-After-Milliseconds",
            Long.toString(TimeUnit.NANOSECONDS.toMillis(probe.getNanosToWaitForRefill()))
        );
        return false;
    }

    private Bucket createBucket() {
        return Bucket.builder().addLimit(bucketConfiguration.getBandwidths()[0]).build();
    }
}
