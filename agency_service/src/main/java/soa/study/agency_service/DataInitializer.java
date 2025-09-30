package soa.study.agency_service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import soa.study.agency_service.jpa.domain.Flat;
import soa.study.agency_service.jpa.repository.FlatRepository;

import java.util.List;

// FIXME delete before pass
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FlatRepository flatRepository;

    @Override
    public void run(String... args) {
        if (flatRepository.count() == 0) {
            List<Flat> flats = List.of(
                    new Flat(null, "Luxury Apartment", 85, 3, 2, 150000.50f),
                    new Flat(null, "Cozy Studio", 45, 1, 1, 75000.00f),
                    new Flat(null, "Family House", 120, 4, 3, 250000.75f),
                    new Flat(null, "Penthouse Suite", 200, 5, 4, 500000.00f),
                    new Flat(null, "Downtown Loft", 65, 2, 1, 120000.25f)
            );

            flatRepository.saveAll(flats);
            System.out.println("Initialized " + flats.size() + " test flats");
        }
    }
}