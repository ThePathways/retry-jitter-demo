package com.thepathways.caller_service.service;

import com.thepathways.caller_service.config.DownstreamProperties;
import com.thepathways.caller_service.config.RetryFactory;
import io.github.resilience4j.retry.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Service
public class RemoteServiceClient {

    private final RestTemplate restTemplate;
    private final RetryFactory retryFactory;
    private final DownstreamProperties downstreamProps;

    public RemoteServiceClient(RestTemplate restTemplate,
                               RetryFactory retryFactory,
                               DownstreamProperties downstreamProps) {
        this.restTemplate = restTemplate;
        this.retryFactory = retryFactory;
        this.downstreamProps = downstreamProps;
    }

    public String call(String strategy) {
        Retry retry = retryFactory.createRetry(strategy);

        retry.getEventPublisher()
            .onRetry(event -> System.out.println(
                java.time.LocalTime.now() + " | " + 
                Thread.currentThread().getName() + 
                " | strategy=" + strategy +
                " | attempt=" + event.getNumberOfRetryAttempts() +
                " | wait=" + event.getWaitInterval().toMillis() + "ms"
            ));

        Supplier<String> supplier =
                Retry.decorateSupplier(retry, this::invoke);

        return supplier.get();
    }

    private String invoke() {
        return restTemplate.getForObject(downstreamProps.getBaseUrl(), String.class);
    }
}