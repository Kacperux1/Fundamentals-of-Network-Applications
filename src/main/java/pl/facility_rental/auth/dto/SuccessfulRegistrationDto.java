package pl.facility_rental.auth.dto;

import lombok.Builder;


@Builder
public record SuccessfulRegistrationDto(String login, String id) {
}
