package soa.study.agency_service.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soa.study.agency_service.jpa.domain.Flat;
import soa.study.agency_service.jpa.repository.FlatRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        return flats.stream()
                .max(Comparator.comparing(Flat::getNumberOfRooms))
                .orElseThrow(() -> new IllegalStateException("Unable to find the most expensive flat"));
    }

    public Double getTotalCost() {
        double sum = 0f;

        var flats = flatRepository.findAll();
        for (Flat flat : flats) {
            sum += flat.getNumberOfRooms()*9000d;
        }
        return sum;
    }
}