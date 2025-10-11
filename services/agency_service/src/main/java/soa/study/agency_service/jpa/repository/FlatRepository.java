package soa.study.agency_service.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soa.study.agency_service.jpa.domain.Flat;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Integer> {

//    @Query("SELECT COALESCE(SUM(f.price), 0) FROM Flat f")
//    Float calculateTotalCost();
}
