// src/main/java/com/example/demo/DemoApplication.java
    package com.example.demo;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RestController;
    import org.springframework.web.bind.annotation.RequestParam;
    import java.util.Random;

    @SpringBootApplication
    @RestController
    public class DemoApplication {

        public static void main(String[] args) {
            SpringApplication.run(DemoApplication.class, args);
        }

        @GetMapping("/")
        public String hello() {
            return "Hello from my Java 21 App!";
        }

        @GetMapping("/health")
        public String healthCheck() {
            return "OK";
        }

        @GetMapping("/simulate-error")
        public String simulateError(@RequestParam(value = "type", defaultValue = "runtime") String errorType) {
            if ("runtime".equalsIgnoreCase(errorType)) {
                throw new RuntimeException("This is a simulated RuntimeException!");
            } else if ("io".equalsIgnoreCase(errorType)) {
                throw new java.io.IOException("This is a simulated IOException");
            } else if ("numberformat".equalsIgnoreCase(errorType)) {
                throw new NumberFormatException("This is a simulated NumberFormatException");
            } else {
                return "No error simulated.  Try /simulate-error?type=runtime, /simulate-error?type=io, or /simulate-error?type=numberformat";
            }
        }

        @GetMapping("/cpu-spike")
        public String cpuSpike(@RequestParam(value = "duration", defaultValue = "10") int duration) throws InterruptedException {
            // Simulate CPU intensive task for the specified duration in seconds
            long startTime = System.currentTimeMillis();
            final long endTime = startTime + (duration * 1000L);
            Random random = new Random();

            while (System.currentTimeMillis() < endTime) {
                // Waste CPU cycles with some random calculations
                double a = random.nextDouble();
                double b = random.nextDouble();
                Math.pow(a, b); // Some calculation
            }
            return "CPU spike simulated for " + duration + " seconds";
        }
    }
