package soa.study.agency_service.jpa.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import soa.study.agency_service.jpa.domain.FlatStat;

import java.util.List;
import java.util.UUID;

public interface FlatStatRepository extends CassandraRepository<FlatStat, UUID> {
    List<FlatStat> findAllByFlatIdIn(List<Long> flatIds);

    @Transactional
    @Modifying
    void deleteFlatStatByFlatId(Long flatId);
}
