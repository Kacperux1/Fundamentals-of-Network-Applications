package pl.facility_rental.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@BsonDiscriminator("facilities")
public class SportsFacility  {
    @BsonId
    @BsonProperty("_id")
    private UUID id;
    @BsonProperty("name")
    private String name;
    @BsonProperty("street_number")
    private String streetNumber;
    @BsonProperty("street")
    private String street;
    @BsonProperty("city")
    private String city;
    @BsonProperty("postal_code")
    private String postalCode;
    @BsonProperty("base_price")
    private BigDecimal basePrice;

    @BsonCreator
    public SportsFacility(@BsonProperty("name") String name, @BsonProperty("street_number") String streetNumber,
                          @BsonProperty("street") String street, @BsonProperty("city") String city,
                          @BsonProperty("postal_code") String postalCode, @BsonProperty("base_price") BigDecimal basePrice) {
        this.id = UUID.randomUUID();
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
