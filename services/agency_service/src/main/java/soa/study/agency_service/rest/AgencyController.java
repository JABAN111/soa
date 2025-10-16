package soa.study.agency_service.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soa.study.agency_service.rest.dto.FlatResponse;
import soa.study.agency_service.rest.error.ErrorResponse;
import soa.study.agency_service.rest.dto.TotalCostResponse;
import soa.study.agency_service.jpa.domain.Flat;
import soa.study.agency_service.service.FlatService;

@RestController
@RequiredArgsConstructor
public class AgencyController {

    private final FlatService flatService;

    @GetMapping("/get-most-expensive/{id1}/{id2}/{id3}")
    public ResponseEntity<?> getMostExpensive(
            @PathVariable Integer id1,
            @PathVariable Integer id2,
            @PathVariable Integer id3
    ) {
        try {
            Flat mostExpensive = flatService.getMostExpensiveFlat(id1, id2, id3);
            return ResponseEntity.ok(new FlatResponse(mostExpensive));
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                    "NOT_FOUND",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/get-total-cost")
    public ResponseEntity<TotalCostResponse> getTotalCost() {
        var totalCost = flatService.getTotalCost();
        TotalCostResponse response = new TotalCostResponse(totalCost);
        return ResponseEntity.ok(response);
    }
}