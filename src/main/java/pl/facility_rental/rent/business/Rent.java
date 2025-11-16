package pl.facility_rental.rent.business;

import lombok.Getter;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.user.business.model.Client;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Rent {
    private UUID id;
    private Client client;
    private SportsFacility sportsFacility;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalPrice;


    public Rent(UUID id, Client client,  SportsFacility sportsFacility,
                      LocalDateTime startDate,  LocalDateTime endDate,
                      BigDecimal totalPrice) {
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
        this.id =  UUID.randomUUID();
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
