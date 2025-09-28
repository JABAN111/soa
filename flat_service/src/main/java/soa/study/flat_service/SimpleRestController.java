package soa.study.flat_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleRestController {
    @GetMapping("/data")
    public String data() {
        return "hello world";
    }
}
