package soa.study.agency_service.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import soa.study.agency_service.jpa.model.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
}
