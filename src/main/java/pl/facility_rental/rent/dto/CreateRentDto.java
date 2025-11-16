package pl.facility_rental.rent.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateRentDto(String clientId,
                            String facilityId,
                            LocalDateTime startDate,
                            LocalDateTime endDate) {
}
