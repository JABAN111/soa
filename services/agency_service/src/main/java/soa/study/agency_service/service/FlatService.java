package soa.study.agency_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soa.study.agency_service.jpa.domain.Flat;
import soa.study.agency_service.jpa.repository.FlatRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlatService {

    private final FlatRepository flatRepository;

    public Flat getMostExpensiveFlat(Integer id1, Integer id2, Integer id3) {
        List<Integer> ids = List.of(id1, id2, id3);
        List<Flat> flats = flatRepository.findAllById(ids);

        if (flats.size() != 3) {
            throw new IllegalArgumentException(
                    "One or more flats not found. Found: " + flats.size() + " out of 3"
            );
        }

        return flats.get(0);
//        return flats.stream()
//                .max(Comparator.comparing(Flat::getPrice))
//                .orElseThrow(() -> new IllegalStateException("Unable to find the most expensive flat"));
    }

    public Float getTotalCost() {
        return flatRepository.calculateTotalCost();
    }

    public Optional<Flat> findById(Integer id) {
        return flatRepository.findById(id);
    }

    public Flat save(Flat flat) {
        return flatRepository.save(flat);
    }

    public List<Flat> findAll() {
        return flatRepository.findAll();
    }
}