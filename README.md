# Resilience4j Jitter & Backoff Demo (Java 21)

This project demonstrates how different Retry Jitter strategies protect microservices from the "Thundering Herd" problem. It uses Spring Boot 3.2, Resilience4j, and Java 21 Virtual Threads to simulate a high-load environment hitting an unstable downstream service.

## 🚀 Key Features

Java 21 Virtual Threads: Uses spring.threads.virtual.enabled to handle high-concurrency retries without blocking OS threads.

Dynamic Retry Strategies: Implements 4 distinct backoff algorithms:

1. no-jitter: Fixed exponential backoff (100ms, 200ms, 400ms...).

2. full-jitter: Exponential backoff with a 0-100% randomization factor.

3. equal-jitter: Half fixed delay, half randomized delay.

4. decorrelated: Advanced jitter that scales based on the previous sleep time to maximize "spread."

Callee Simulation: A service that mimics real-world failure with randomized latency (50-300ms) and a configurable failure rate (70%).

## 🛠 Project Structure

Caller Service (Port 8080): The orchestrator that applies the Retry policies.

Callee Service (Port 8081): The "unstable" service that fails randomly.


## 🚦 How to Run the Demo


### 1. Start the Services
Run both services using Maven:
````
Bash
mvn spring-boot:run
````

### 2. Trigger a Single Request
Observe the retry logs in the caller-service console:
````
Bash
curl http://localhost:8080/test/full-jitter
````

### 3. Run the Load Test (The "Thundering Herd" Test)
Simulate 20 concurrent requests to see how Jitter spreads the load:

````
Bash
curl http://localhost:8080/test/load/decorrelated

````


## 📊 Observation Guide
When running the load test, look at the timestamps in the Caller Service logs:

Strategy	Log Observation	Result
no-jitter	Threads retry at the exact same millisecond.	Dangerous. Creates spikes.
decorrelated	Threads drift apart rapidly (some wait 100ms, some 5s).	Safe. Distributes load perfectly.


## ⚙️ Configuration
The behavior can be tuned in application.properties:

### Properties
````
retry.max-attempts=5
retry.base-delay=100
retry.multiplier=2.0
unstable.failure-rate=70
````


## Summary: The Caller Service's "To-Do" List
````
1. Receive the request from you (the user).
2. Consult the RetryFactory for the math.
3. Attempt the call to the Callee.
4. Wait (efficiently using Virtual Threads) if it fails.
5. Calculate a unique, randomized "Jitter" time for the next try.
6 Report the final success or failure back to you.
````
