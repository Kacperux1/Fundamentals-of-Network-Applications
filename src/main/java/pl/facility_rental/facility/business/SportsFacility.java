package pl.facility_rental.facility.business;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class SportsFacility {

    private String id;
    private String name;
    private String streetNumber;
    @Setter
    private String street;
    private String city;
    private String postalCode;
    @Setter
    private BigDecimal basePrice;
    public SportsFacility(String id,String name,String streetNumber,
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
