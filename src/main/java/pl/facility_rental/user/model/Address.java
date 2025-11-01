package pl.facility_rental.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@NoArgsConstructor
@Getter
@Setter
public class Address {
    @BsonId
    private Long id;
    @BsonProperty("street_number")
    private String streetNumber;
    @BsonProperty("street")
    private String street;
    @BsonProperty("city")
    private String city;
    @BsonProperty("postal_code")
    private String postalCode;

    @BsonCreator
    public Address(@BsonProperty("street_number") String streetNumber, @BsonProperty("street") String street,
                   @BsonProperty("city") String city, @BsonProperty("postal_code") String postalCode) {
        this.streetNumber = streetNumber;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
    }
}

