package edu.java.bot.configuration;

import edu.java.RateLimitInterceptor;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import java.time.Duration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class RateLimitConfig implements WebMvcConfigurer {
    private static final long BUCKET_CAPACITY = 100;
    private static final Duration BUCKET_REFILL_TIME = Duration.ofMinutes(1);
    private final BucketConfiguration configuration;

    public RateLimitConfig() {
        configuration = BucketConfiguration.builder()
            .addLimit(Bandwidth.simple(BUCKET_CAPACITY, BUCKET_REFILL_TIME))
            .build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        RateLimitInterceptor rateLimitInterceptor = new RateLimitInterceptor(configuration);
        registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/updates");
    }
}
