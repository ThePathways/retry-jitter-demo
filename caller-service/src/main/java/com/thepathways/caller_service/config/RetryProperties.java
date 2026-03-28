package com.thepathways.caller_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "retry")
public class RetryProperties {
    private int maxAttempts;
    private long baseDelay;
    private double multiplier;
    private String defaultStrategy;
    private Jitter jitter = new Jitter();


    public static class Jitter {
        private double fullFactor;

         public double getFullFactor() {
            return fullFactor;
        }

        public void setFullFactor(double fullFactor) {
            this.fullFactor = fullFactor;
        }
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public long getBaseDelay() {
        return baseDelay;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getDefaultStrategy() {
        return defaultStrategy;
    }

    public Jitter getJitter() {
        return jitter;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public void setBaseDelay(long baseDelay) {
        this.baseDelay = baseDelay;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public void setDefaultStrategy(String defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
    }

    public void setJitter(Jitter jitter) {
        this.jitter = jitter;
    }
}