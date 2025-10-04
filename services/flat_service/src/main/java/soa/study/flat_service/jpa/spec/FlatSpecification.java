package soa.study.flat_service.jpa.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import soa.study.flat_service.jpa.domain.Flat;
import soa.study.flat_service.jpa.domain.Furnish;
import soa.study.flat_service.jpa.domain.Transport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlatSpecification {

    public static Specification<Flat> withFilters(Map<String, String> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.containsKey("idMin")) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("id"),
                        Integer.parseInt(filters.get("idMin"))));
            }
            if (filters.containsKey("idMax")) {
                predicates.add(cb.lessThanOrEqualTo(root.get("id"),
                        Integer.parseInt(filters.get("idMax"))));
            }

            if (filters.containsKey("name")) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + filters.get("name").toLowerCase() + "%"));
            }

            if (filters.containsKey("minX")) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("coordinates").get("x"),
                        Integer.parseInt(filters.get("minX"))));
            }
            if (filters.containsKey("maxX")) {
                predicates.add(cb.lessThanOrEqualTo(root.get("coordinates").get("x"),
                        Integer.parseInt(filters.get("maxX"))));
            }
            if (filters.containsKey("minY")) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("coordinates").get("y"),
                        Integer.parseInt(filters.get("minY"))));
            }
            if (filters.containsKey("maxY")) {
                predicates.add(cb.lessThanOrEqualTo(root.get("coordinates").get("y"),
                        Integer.parseInt(filters.get("maxY"))));
            }

            if (filters.containsKey("minCreationDate")) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("creationDate"),
                        LocalDate.parse(filters.get("minCreationDate"))));
            }
            if (filters.containsKey("maxCreationDate")) {
                predicates.add(cb.lessThanOrEqualTo(root.get("creationDate"),
                        LocalDate.parse(filters.get("maxCreationDate"))));
            }

            if (filters.containsKey("minArea")) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("area"),
                        Integer.parseInt(filters.get("minArea"))));
            }
            if (filters.containsKey("maxArea")) {
                predicates.add(cb.lessThanOrEqualTo(root.get("area"),
                        Integer.parseInt(filters.get("maxArea"))));
            }

            if (filters.containsKey("minNumberOfRooms")) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("numberOfRooms"),
                        Integer.parseInt(filters.get("minNumberOfRooms"))));
            }
            if (filters.containsKey("maxNumberOfRooms")) {
                predicates.add(cb.lessThanOrEqualTo(root.get("numberOfRooms"),
                        Integer.parseInt(filters.get("maxNumberOfRooms"))));
            }

            if (filters.containsKey("furnish")) {
                predicates.add(cb.equal(root.get("furnish"),
                        Furnish.valueOf(filters.get("furnish"))));
            }

            if (filters.containsKey("transport")) {
                predicates.add(cb.equal(root.get("transport"),
                        Transport.valueOf(filters.get("transport"))));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}