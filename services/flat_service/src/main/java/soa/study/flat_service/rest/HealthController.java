package soa.study.flat_service.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class HealthController {
    @GetMapping("/health")
    public String data() {
        return "OK";
    }
}
