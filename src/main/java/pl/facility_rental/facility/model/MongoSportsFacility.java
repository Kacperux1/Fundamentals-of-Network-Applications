package pl.facility_rental.facility.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@BsonDiscriminator("facilities")
public class MongoSportsFacility {
    @BsonId
    private ObjectId id;
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
    public MongoSportsFacility(@BsonId ObjectId id, @BsonProperty("name") String name, @BsonProperty("street_number") String streetNumber,
                               @BsonProperty("street") String street, @BsonProperty("city") String city,
                               @BsonProperty("postal_code") String postalCode, @BsonProperty("base_price") BigDecimal basePrice) {
        this.id = id;
        this.name = name;
        this.streetNumber = streetNumber;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.basePrice = basePrice;
    }

    public MongoSportsFacility(String name, String streetNumber, String street, String city, String postalCode, BigDecimal basePrice) {
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
