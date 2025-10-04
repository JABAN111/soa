package soa.study.flat_service.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import soa.study.flat_service.jpa.domain.Flat;
import soa.study.flat_service.jpa.domain.Transport;

import java.util.List;
import java.util.Map;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Integer>, JpaSpecificationExecutor<Flat> {

    @Query("SELECT SUM(f.numberOfRooms) FROM Flat f")
    Long sumAllRooms();

    @Query("SELECT f.numberOfRooms as rooms, COUNT(f) as count FROM Flat f GROUP BY f.numberOfRooms")
    List<Object[]> groupByNumberOfRooms();

    List<Flat> findByTransportGreaterThan(Transport transport);
}