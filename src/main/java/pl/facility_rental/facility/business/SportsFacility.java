package pl.facility_rental.facility.business;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SportsFacility {

    private String id;
    private String name;
    private String streetNumber;
    private String street;
    private String city;
    private String postalCode;
    private BigDecimal basePrice;

    @JsonCreator
    public SportsFacility(@JsonProperty("id") String id,
                          @JsonProperty("name") String name,
                          @JsonProperty("streetNumber") String streetNumber,
                          @JsonProperty("street") String street,
                          @JsonProperty("city") String city,
                          @JsonProperty("postalCode") String postalCode,
                          @JsonProperty("basePrice") BigDecimal basePrice) {
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
