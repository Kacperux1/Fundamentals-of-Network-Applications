package pl.facility_rental.rent.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateRentDto(UUID clientId,
                            UUID facilityId,
                            LocalDateTime startDate,
                            LocalDateTime endDate) {
}
