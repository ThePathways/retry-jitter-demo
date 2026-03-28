package com.thepathways.callee_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "unstable") // Matches 'unstable.failure-rate' in properties
public class UnstableProperties {

    private int failureRate;
    private int latencyMin;
    private int latencyMax;

    // Getters and Setters (REQUIRED for Spring to inject values)
    public int getFailureRate() { return failureRate; }
    public void setFailureRate(int failureRate) { this.failureRate = failureRate; }

    public int getLatencyMin() { return latencyMin; }
    public void setLatencyMin(int latencyMin) { this.latencyMin = latencyMin; }

    public int getLatencyMax() { return latencyMax; }
    public void setLatencyMax(int latencyMax) { this.latencyMax = latencyMax; }
}