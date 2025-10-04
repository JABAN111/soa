package soa.study.flat_service.jpa.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class House {

    private String name;

    @Min(value = 1, message = "Year must be at least 1")
    private Long year;

    @Min(value = 1, message = "Number of floors must be at least 1")
    private Integer numberOfFloors;
}