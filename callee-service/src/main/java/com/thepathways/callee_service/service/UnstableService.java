package com.thepathways.callee_service.service;
import com.thepathways.callee_service.config.UnstableProperties;
import org.springframework.stereotype.Service;
import java.util.concurrent.ThreadLocalRandom;

import java.time.Duration;

@Service
public class UnstableService {

    private final UnstableProperties props;

    public UnstableService(UnstableProperties props) {
        this.props = props;
    }

    public boolean processWithPotentialFailure() {
        var random = ThreadLocalRandom.current();
        
        int latency = random.nextInt(props.getLatencyMin(), props.getLatencyMax() + 1);
        
        try {
            Thread.sleep(Duration.ofMillis(latency));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        // 3. Logic: If random(0-99) < failureRate, it's a failure.
        // e.g. If rate is 70, 0-69 fail (70%), 70-99 succeed (30%).
        return random.nextInt(100) >= props.getFailureRate();
    }
}
