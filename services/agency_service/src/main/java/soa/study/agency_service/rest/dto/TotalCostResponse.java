package soa.study.agency_service.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalCostResponse {
    private Float totalCost;
    private String currency;

    public TotalCostResponse(Float totalCost) {
        this.totalCost = totalCost;
        this.currency = "USD";
    }
}