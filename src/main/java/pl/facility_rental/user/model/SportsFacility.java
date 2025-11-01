package pl.facility_rental.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class SportsFacility  {
    @BsonId
    private Long id;
    @BsonProperty("name")
    private String name;
    @BsonProperty("address_id")
    private Address address;
    @BsonProperty("base_price")
    private BigDecimal basePrice;

    @BsonCreator
    public SportsFacility(@BsonProperty("name") String name, @BsonProperty("address_id") Address address,
                          @BsonProperty("base_price") BigDecimal basePrice) {
        this.name = name;
        this.address = address;
        this.basePrice = basePrice;
    }

    public BigDecimal getPricePerHour() {
        return basePrice;
    }
}
