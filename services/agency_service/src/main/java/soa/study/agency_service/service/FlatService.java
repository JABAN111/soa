package soa.study.agency_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import soa.study.agency_service.jpa.domain.FlatStat;
import soa.study.agency_service.jpa.repository.FlatStatRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlatService {

    private final FlatStatRepository flatStatRepository;

    public FlatStat getMostExpensiveFlat(Long id1, Long id2, Long id3) {
        List<Long> ids = List.of(id1, id2, id3);
        var flats = flatStatRepository.findAllByFlatIdIn(ids);
        if (flats.size() != 3) {
            throw new IllegalArgumentException(
                    "One or more flats not found. Found: " + flats.size() + " out of 3"
            );
        }
        return flats.stream()
                .max(Comparator.comparing(FlatStat::getNumberOfRooms))
                .orElseThrow(() -> new IllegalStateException("Unable to find the most expensive flat"));
    }

    public Double getTotalCost() {
        double sum = 0f;

        var flats = flatStatRepository.findAll();
        for (FlatStat flat : flats) {
            sum += flat.getNumberOfRooms() * 9000d;
        }
        return sum;
    }


    public void pushFlatStats(FlatStat stat) {
        if (stat.getNumberOfRooms() == -1) {
            flatStatRepository.deleteFlatStatByFlatId(stat.getFlatId());
            log.info("delete flat stat by id {}", stat.getFlatId());
            return;
        }
        log.info("push flat stat by id {}", stat.getFlatId());
        flatStatRepository.save(stat);
    }
}