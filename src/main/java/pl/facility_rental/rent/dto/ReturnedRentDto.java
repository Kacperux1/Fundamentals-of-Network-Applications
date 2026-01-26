    package pl.facility_rental.rent.dto;

    import com.fasterxml.jackson.annotation.JsonAnyGetter;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.UUID;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReturnedRentDto{

        private String rentId;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String facilityName;
        private String streetNumber;
        private String street;
        private String city;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private BigDecimal totalPrice;
    }


