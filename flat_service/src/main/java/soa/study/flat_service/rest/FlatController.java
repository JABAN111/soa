package soa.study.flat_service.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soa.study.flat_service.jpa.domain.Flat;
import soa.study.flat_service.jpa.domain.Transport;
import soa.study.flat_service.service.FlatService;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FlatController {

    private final FlatService flatService;

    @GetMapping
    public ResponseEntity<List<Flat>> getAllFlats(
            @RequestParam Map<String, String> allParams,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (size < 1 || size > 100) {
            size = 10;
        }
        if (page < 0) {
            page = 0;
        }

        Page<Flat> flats = flatService.getAllFlats(allParams, page, size);
        return ResponseEntity.ok(flats.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flat> getFlatById(@PathVariable Integer id) {
        return flatService.getFlatById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Flat> createFlat(@Valid @RequestBody Flat flat) {
        Flat createdFlat = flatService.createFlat(flat);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFlat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flat> updateFlat(
            @PathVariable Integer id,
            @Valid @RequestBody Flat flat) {
        try {
            Flat updatedFlat = flatService.updateFlat(id, flat);
            return ResponseEntity.ok(updatedFlat);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlat(@PathVariable Integer id) {
        try {
            flatService.deleteFlat(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/utils/sum-rooms")
    public ResponseEntity<Map<String, Long>> sumRooms() {
        Long totalRooms = flatService.sumAllRooms();
        return ResponseEntity.ok(Map.of("totalRooms", totalRooms));
    }

    @GetMapping("/utils/group-by-rooms")
    public ResponseEntity<Map<Integer, Long>> groupByRooms() {
        Map<Integer, Long> grouped = flatService.groupByNumberOfRooms();
        return ResponseEntity.ok(grouped);
    }

    @GetMapping("/utils/transport-greater-than/{value}")
    public ResponseEntity<List<Flat>> getFlatsWithTransportGreaterThan(
            @PathVariable String value) {
        try {
            Transport transport = Transport.valueOf(value.toUpperCase());
            List<Flat> flats = flatService.findFlatsWithTransportGreaterThan(transport);
            return ResponseEntity.ok(flats);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}