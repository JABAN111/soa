package soa.study.agency_service;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SimpleRestController {
    private ModelRepository modelRepository;
    @GetMapping("/data")
    public String data() {
        var m = new Model();
        m.setName("hate payara");
        modelRepository.save(m);
        return "hello world";
    }
}
