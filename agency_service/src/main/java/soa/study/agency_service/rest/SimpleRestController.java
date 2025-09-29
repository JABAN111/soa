package soa.study.agency_service.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import soa.study.agency_service.jpa.model.Model;
import soa.study.agency_service.jpa.repository.ModelRepository;

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
