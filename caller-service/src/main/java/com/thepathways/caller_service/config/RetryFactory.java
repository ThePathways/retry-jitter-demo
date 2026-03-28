package com.thepathways.caller_service.config;

import io.github.resilience4j.core.IntervalFunction; // The correct import
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;    

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@EnableConfigurationProperties({RetryProperties.class, DownstreamProperties.class})
public class RetryFactory {

    private final RetryProperties props;

    public RetryFactory(RetryProperties props) {
        this.props = props;
    }

    public Retry createRetry(String strategy) {
        // This is where resolveIntervalFunction is called
        IntervalFunction intervalFunction = resolveIntervalFunction(strategy);

        RetryConfig config = RetryConfig.custom()
                .maxAttempts(props.getMaxAttempts())
                .intervalFunction(intervalFunction)
                .retryExceptions(Exception.class)
                .build();

        // 1. Create the Retry object
        Retry retry = Retry.of("retry-" + strategy + "-" + UUID.randomUUID(), config);

        // 2. ADD THIS BLOCK HERE (The Logging Listener)
        retry.getEventPublisher().onRetry(event -> {
            System.out.printf("[STRATEGY: %s] Attempt #%d | Wait: %dms%n", 
                strategy, 
                event.getNumberOfRetryAttempts(), 
                event.getWaitInterval().toMillis());
        });

        // 3. Finally return it
        return retry;
    }

    
    // This is the method that was likely missing or unrecognized
    private IntervalFunction resolveIntervalFunction(String strategy) {
        long base = props.getBaseDelay();
        double multiplier = props.getMultiplier();

        return switch (strategy) {
            case "no-jitter" ->
                    IntervalFunction.ofExponentialBackoff(base, multiplier);
            case "full-jitter" -> 
                    IntervalFunction.ofExponentialRandomBackoff(base, multiplier, props.getJitter().getFullFactor());
            case "equal-jitter" -> attempt -> {
                long max = (long) (base * Math.pow(multiplier, attempt));
                long half = max / 2;
                return half + ThreadLocalRandom.current().nextLong(half);
            };
            case "decorrelated" -> createDecorrelated(base);
            default -> throw new IllegalArgumentException("Invalid strategy: " + strategy);
        };
    }

    private IntervalFunction createDecorrelated(long base) {
        return new IntervalFunction() {
            private long previous = base;

            @Override
            public Long apply(Integer attempt) {
                // ThreadLocalRandom needs (min, max). Max must be > min.
                long next = ThreadLocalRandom.current().nextLong(base, Math.max(base + 1, previous * 3));
                previous = next;
                return next;
            }
        };
    }
}