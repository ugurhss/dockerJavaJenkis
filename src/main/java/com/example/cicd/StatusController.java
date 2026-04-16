package com.example.cicd;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @Value("${app.message:CI/CD ortami hazir}")
    private String appMessage;
//ccc
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "cicd-demo");
        response.put("message", appMessage);
        response.put("timestamp", ZonedDateTime.now().toString());
        return response;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
