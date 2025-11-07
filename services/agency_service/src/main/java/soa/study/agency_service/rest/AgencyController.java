package soa.study.agency_service.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soa.study.agency_service.rest.dto.FlatStatResponse;
import soa.study.agency_service.rest.dto.TotalCostResponse;
import soa.study.agency_service.rest.error.ErrorResponse;
import soa.study.agency_service.service.FlatService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AgencyController {

    private final FlatService flatService;

    @GetMapping("/get-most-expensive/{id1}/{id2}/{id3}")
    public ResponseEntity<?> getMostExpensive(
            @PathVariable Long id1,
            @PathVariable Long id2,
            @PathVariable Long id3
    ) {
        try {
            var mostExpensive = flatService.getMostExpensiveFlat(id1, id2, id3);
            return ResponseEntity.ok(new FlatStatResponse(mostExpensive));
        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(
                    "NOT_FOUND",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/push")
    public void push(@RequestBody FlatStatResponse flatStat) {
        flatService.pushFlatStats(flatStat.toFlatStat());
    }

    @GetMapping("/get-total-cost")
    public ResponseEntity<TotalCostResponse> getTotalCost() {
        var totalCost = flatService.getTotalCost();
        TotalCostResponse response = new TotalCostResponse(totalCost);
        return ResponseEntity.ok(response);
    }
}