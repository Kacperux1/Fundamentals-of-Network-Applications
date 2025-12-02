package pl.facility_rental.rent.business;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.user.business.model.Client;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rent {
    private String id;
    private Client client;
    private SportsFacility sportsFacility;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalPrice;

    @JsonCreator
    public Rent(@JsonProperty("id") String id,
                @JsonProperty("client") Client client,
                @JsonProperty("sportsFacility") SportsFacility sportsFacility,
                @JsonProperty("startDate") LocalDateTime startDate,
                @JsonProperty("endDate") LocalDateTime endDate,
                @JsonProperty("totalPrice") BigDecimal totalPrice) {
        this.id =  id;
        this.client = client;
        this.sportsFacility = sportsFacility;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        try {
            this.totalPrice = sportsFacility.getPricePerHour().multiply(BigDecimal.valueOf(Duration.between(startDate, endDate).toHours()));
        } catch (NullPointerException e) {
            this.totalPrice = BigDecimal.ZERO;
        }
    }

    public Rent(Client client, SportsFacility sportsFacility, LocalDateTime startDate, LocalDateTime endDate) {
        this.client = client;
        this.sportsFacility = sportsFacility;
        this.startDate = startDate;
        this.endDate = endDate;
        if(endDate == null) {
            this.totalPrice = BigDecimal.ZERO;
        } else {
            this.totalPrice = BigDecimal.valueOf(Duration.between(startDate, endDate).toHours()).multiply(sportsFacility.getPricePerHour());
        }
    }
}
