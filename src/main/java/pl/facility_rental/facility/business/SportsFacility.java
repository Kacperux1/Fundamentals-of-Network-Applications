package pl.facility_rental.facility.business;


import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class SportsFacility {

    private  UUID id;
    private final String name;
    private final String streetNumber;
    private final String street;
    private final String city;
    private final String postalCode;
    private final BigDecimal basePrice;
    public SportsFacility(UUID id,String name,String streetNumber,
                               String street,  String city,
                                String postalCode, BigDecimal basePrice) {
        this.id = id;
        this.name = name;
        this.streetNumber = streetNumber;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.basePrice = basePrice;
    }

    public SportsFacility(String name, String streetNumber, String street, String city, String postalCode, BigDecimal basePrice) {
        this.name = name;
        this.streetNumber = streetNumber;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.basePrice = basePrice;
    }

    public BigDecimal getPricePerHour() {
        return basePrice;
    }
}
