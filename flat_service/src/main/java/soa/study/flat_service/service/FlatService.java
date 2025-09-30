package soa.study.flat_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soa.study.flat_service.jpa.domain.Flat;
import soa.study.flat_service.jpa.domain.Transport;
import soa.study.flat_service.jpa.repository.FlatRepository;
import soa.study.flat_service.jpa.spec.FlatSpecification;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlatService {

    private final FlatRepository flatRepository;

    public Page<Flat> getAllFlats(Map<String, String> filters, int page, int size) {
        Specification<Flat> spec = FlatSpecification.withFilters(filters);
        Sort sort = buildSort(filters);
        Pageable pageable = PageRequest.of(page, size, sort);
        return flatRepository.findAll(spec, pageable);
    }

    public Optional<Flat> getFlatById(Integer id) {
        return flatRepository.findById(id);
    }

    @Transactional
    public Flat createFlat(Flat flat) {
        flat.setId(null);
        flat.setCreationDate(null);
        return flatRepository.save(flat);
    }

    @Transactional
    public Flat updateFlat(Integer id, Flat flatDetails) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Flat not found with id: " + id));

        flat.setName(flatDetails.getName());
        flat.setCoordinates(flatDetails.getCoordinates());
        flat.setArea(flatDetails.getArea());
        flat.setNumberOfRooms(flatDetails.getNumberOfRooms());
        flat.setNumberOfBathrooms(flatDetails.getNumberOfBathrooms());
        flat.setFurnish(flatDetails.getFurnish());
        flat.setTransport(flatDetails.getTransport());
        flat.setHouse(flatDetails.getHouse());

        return flatRepository.save(flat);
    }

    @Transactional
    public void deleteFlat(Integer id) {
        if (!flatRepository.existsById(id)) {
            throw new NoSuchElementException("Flat not found with id: " + id);
        }
        flatRepository.deleteById(id);
    }

    public Long sumAllRooms() {
        Long sum = flatRepository.sumAllRooms();
        return sum != null ? sum : 0L;
    }

    public Map<Integer, Long> groupByNumberOfRooms() {
        List<Object[]> results = flatRepository.groupByNumberOfRooms();
        return results.stream()
                .collect(Collectors.toMap(
                        arr -> (Integer) arr[0],
                        arr -> (Long) arr[1]
                ));
    }

    public List<Flat> findFlatsWithTransportGreaterThan(Transport transport) {
        if (transport == null) {
            return flatRepository.findAll();
        }
        return flatRepository.findAll().stream()
                .filter(flat -> flat.getTransport() != null &&
                        flat.getTransport().isGreaterThan(transport))
                .collect(Collectors.toList());
    }

    private Sort buildSort(Map<String, String> filters) {
        List<Sort.Order> orders = new ArrayList<>();

        if (filters.containsKey("sortId")) {
            orders.add(getOrder("id", filters.get("sortId")));
        }
        if (filters.containsKey("sortName")) {
            orders.add(getOrder("name", filters.get("sortName")));
        }
        if (filters.containsKey("sortCoordinates")) {
            orders.add(getOrder("coordinates.x", filters.get("sortCoordinates")));
        }
        if (filters.containsKey("sortCreationDate")) {
            orders.add(getOrder("creationDate", filters.get("sortCreationDate")));
        }
        if (filters.containsKey("sortArea")) {
            orders.add(getOrder("area", filters.get("sortArea")));
        }
        if (filters.containsKey("sortNumberOfRooms")) {
            orders.add(getOrder("numberOfRooms", filters.get("sortNumberOfRooms")));
        }
        if (filters.containsKey("sortFurnish")) {
            orders.add(getOrder("furnish", filters.get("sortFurnish")));
        }
        if (filters.containsKey("sortTransport")) {
            orders.add(getOrder("transport", filters.get("sortTransport")));
        }
        if (filters.containsKey("sortHouse")) {
            orders.add(getOrder("house.name", filters.get("sortHouse")));
        }

        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }

    private Sort.Order getOrder(String property, String direction) {
        return "desc".equalsIgnoreCase(direction) ?
                Sort.Order.desc(property) : Sort.Order.asc(property);
    }
}