package com.chadfield.multidata.dbconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private static final Logger LOGGER = LogManager.getLogger(DatabaseController.class);

    private final CacheManager cacheManager;
    private final ObjectMapper objectMapper;
    private final DatabaseClientConfiguration databaseClientConfiguration;

    public DatabaseController(CacheManager cacheManager, ObjectMapper objectMapper, DatabaseClientConfiguration databaseClientConfiguration) {
        this.cacheManager = cacheManager;
        this.objectMapper = objectMapper;
        this.databaseClientConfiguration = databaseClientConfiguration;
    }

    /**
     * Method to change the database
     * @param dbKey Database key to switch to
     * @return Mono with the result of the operation
     */
    @RequestMapping("/change")
    public Mono<ResponseEntity<String>> change(@RequestParam("dbKey") String dbKey, WebSession session) {
        // Check if the dbKey is valid
        List<String> validDbKeys = databaseClientConfiguration.registeredConnectionFactories().keySet().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        if (!validDbKeys.contains(dbKey.toLowerCase())) {
            LOGGER.warn("Attempted to switch to an invalid database: " + dbKey);
            return Mono.just(ResponseEntity.badRequest().body("Invalid database key: " + dbKey));
        }

        session.getAttributes().put(DataSourceConfig.DB_KEY, dbKey);
        LOGGER.info("Switched to database: " + dbKey);


        return Mono.just(ResponseEntity.ok("Switched to database: " + dbKey));
    }

    @GetMapping(value="/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getCurrentDb(WebSession session) {
        String dbKey = (String) session.getAttribute(DataSourceConfig.DB_KEY);
        LOGGER.info("DB_KEY in session: {}", dbKey);
        return Mono.just(String.format("{\"key\": \"%s\", \"value\": \"%s\"}", dbKey != null ? dbKey.toUpperCase() : "NONE", dbKey != null ? dbKey : "NONE"));
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getListDb() {
        LOGGER.info("Get list of all DBs");

        // Generate JSON response dynamically
        List<Map<String, String>> databases = databaseClientConfiguration.registeredConnectionFactories().keySet().stream()
                .map(dbName -> Map.of("key", dbName.toUpperCase(), "value", dbName.toLowerCase()))
                .collect(Collectors.toList());

        try {
            String jsonResponse = objectMapper.writeValueAsString(databases);
            return Mono.just(jsonResponse);
        } catch (Exception e) {
            LOGGER.error("Failed to convert database list to JSON", e);
            return Mono.error(new RuntimeException("Failed to generate database list"));
        }
    }
}