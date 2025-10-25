package soa.study.agency_service.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soa.study.agency_service.jpa.domain.Flat;

@Repository
public interface FlatRepository extends JpaRepository<Flat, Integer> { }
