package soa.study.agency_service.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalCostResponse {
    private Double totalCost;
    private String currency;

    public TotalCostResponse(Double totalCost) {
        this.totalCost = totalCost;
        this.currency = "USD";
    }
}