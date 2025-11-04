package soa.study.agency_service.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import soa.study.agency_service.jpa.domain.Flat;
import soa.study.agency_service.jpa.domain.FlatStat;

import java.util.List;

public interface FlatStatRepository extends JpaRepository<FlatStat, Long> {
    List<FlatStat> findAllByFlatIdIn(List<Long> flatIds);

    @Transactional
    @Modifying
    void deleteFlatStatByFlatId(Long flatId);
}
