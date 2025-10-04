package soa.study.flat_service.jpa.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Coordinates {

    @NotNull(message = "X coordinate is required")
    @Max(value = 324, message = "X coordinate must not exceed 324")
    private Integer x;

    @NotNull(message = "Y coordinate is required")
    @Max(value = 832, message = "Y coordinate must not exceed 832")
    private Integer y;
}