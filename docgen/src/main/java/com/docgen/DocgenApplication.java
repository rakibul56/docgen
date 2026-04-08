package com.docgen;

// SpringApplication — the class that bootstraps (starts) our app
import org.springframework.boot.SpringApplication;
// @SpringBootApplication — a powerful annotation that enables:
//   1. @Configuration — this class can define beans (objects Spring manages)
//   2. @EnableAutoConfiguration — Spring guesses what you need and configures it
//      (sees spring-boot-starter-web? → starts Tomcat automatically!)
//   3. @ComponentScan — scans com.docgen and all sub-packages for your classes
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DocgenApplication {

    public static void main(String[] args) {
        // This single line does ALL of this:
        // 1. Creates the Spring application context (dependency injection container)
        // 2. Scans for @RestController, @Service, @Component classes
        // 3. Starts embedded Tomcat on port 8080
        // 4. Registers all your REST endpoints
        // 5. App is ready to receive HTTP requests!
        SpringApplication.run(DocgenApplication.class, args);
    }
}