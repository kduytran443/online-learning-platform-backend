package com.kduytran.categoryservice.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DebeziumConnectorConfig {

    private final RestTemplate restTemplate;

    private static final String KAFKA_CONNECT_URL = "http://localhost:8083";

    private static final String CONNECTOR_NAME = "category-postgres-connector";

    @PostConstruct
    public void setupConnector() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    KAFKA_CONNECT_URL + "/connectors/" + CONNECTOR_NAME,
                    String.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Connector [{}] already exists. Updating config...", CONNECTOR_NAME);
                updateConnector();
            }
        } catch (HttpClientErrorException.NotFound ex) {
            log.info("Connector [{}] does not exist. Creating new connector...", CONNECTOR_NAME);
            createConnector();
        } catch (Exception e) {
            log.error("Error while setting up connector [{}]: {}", CONNECTOR_NAME, e.getMessage());
        }
    }

    private void createConnector() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "name", CONNECTOR_NAME,
                "config", getConnectorConfig()
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(
                KAFKA_CONNECT_URL + "/connectors",
                request,
                String.class
        );

        log.info("Connector [{}] created successfully.", CONNECTOR_NAME);
    }

    private void updateConnector() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> config = getConnectorConfig();

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(config, headers);

        restTemplate.put(
                KAFKA_CONNECT_URL + "/connectors/" + CONNECTOR_NAME + "/config",
                request
        );

        log.info("Connector [{}] updated successfully.", CONNECTOR_NAME);
    }

    private Map<String, Object> getConnectorConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
        config.put("tasks.max", "1");
        config.put("database.hostname", "postgres_category_db");
        config.put("database.port", "5432");
        config.put("database.user", "user");
        config.put("database.password", "password");
        config.put("database.dbname", "category_db");
        config.put("topic.prefix", "category-service");
        config.put("database.server.name", "categorydb");
        config.put("table.include.list", "public.category");
        config.put("plugin.name", "pgoutput");
        config.put("snapshot.mode", "initial");
        return config;
    }
}
