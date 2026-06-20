plugins {
    `java-library`
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.7")
    }
}

dependencies {
    // Spring Kafka for event contracts and serialization
    api("org.springframework.kafka:spring-kafka")

    // Spring Data JPA for shared entity patterns
    api("org.springframework.data:spring-data-jpa")

    // Jackson for JSON serialization
    api("com.fasterxml.jackson.core:jackson-databind")
    api("com.fasterxml.jackson.core:jackson-annotations")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Jakarta Persistence API (for shared annotations if needed)
    api("jakarta.persistence:jakarta.persistence-api")

    // Jakarta Validation API
    api("jakarta.validation:jakarta.validation-api")

    // Spring Web for CorrelationIdFilter
    api("org.springframework:spring-web")
    api("org.springframework:spring-webmvc")

    // Jakarta Servlet API for OncePerRequestFilter
    compileOnly("jakarta.servlet:jakarta.servlet-api")

    // Micrometer for metric name constants
    api("io.micrometer:micrometer-core")

    // SLF4J for logging
    api("org.slf4j:slf4j-api")

    // Spring Context for @Component, @Order
    api("org.springframework:spring-context")
}
