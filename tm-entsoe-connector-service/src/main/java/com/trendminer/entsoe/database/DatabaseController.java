package com.trendminer.entsoe.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DatabaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseController.class);
    private final Map<Integer, HistorianDTO> databases = new HashMap<>();

    @Autowired
    public DatabaseController() {
        databases.put(1, new HistorianDTO(1,
                "ENTSOE Platform",
                "",
                "ENTSOE",
                "",
                "",
                "",
                "1.0",
                ""));
    }

    @GetMapping("/database")
    public Iterable<HistorianDTO> getHistorians() {
        return databases.values();
    }

    @GetMapping("/database/{id}")
    public HistorianDTO getHistorian(@PathVariable("id") int id) {
        return databases.get(id);
    }

    @PostMapping(value = "/database", consumes = MediaType.APPLICATION_JSON_VALUE)
    public int createHistorian(@RequestBody HistorianDTO historianDTO) {
        return 3;
    }

    @PutMapping(
            value = "/database/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public HistorianDTO updateHistorian(
            @PathVariable("id") int id, @RequestBody HistorianDTO historianDTO) {
        return databases.get(id);
    }

    @DeleteMapping("/database/{id}")
    public ResponseEntity<Void> deleteHistorian(@PathVariable("id") int id) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/database/{id}/test")
    public ResponseEntity<Boolean> testConnection(@PathVariable("id") int id) {
        return ResponseEntity.ok(true);
    }
}
