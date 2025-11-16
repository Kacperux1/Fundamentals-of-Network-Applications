package pl.facility_rental.rent.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.facility_rental.user.business.model.Client;

import pl.facility_rental.facility.model.MongoSportsFacility;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@BsonDiscriminator("rents")
public class MongoRent {
    @BsonId
    private UUID id;
    @BsonProperty("client")
    private Client client;
    @BsonProperty("facility")
    private MongoSportsFacility sportsFacility;
    @BsonProperty("start_date")
    private LocalDateTime startDate;
    @Setter
    @BsonProperty("end_date")
    private LocalDateTime endDate;
    @BsonProperty("total_price")
    private BigDecimal totalPrice;

    @BsonCreator
    public MongoRent(@BsonId UUID id, @BsonProperty("client") Client client, @BsonProperty("facility") MongoSportsFacility sportsFacility,
                     @BsonProperty("start_date") LocalDateTime startDate, @BsonProperty("end_date") LocalDateTime endDate,
                     @BsonProperty("total_price") BigDecimal totalPrice) {
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

    public MongoRent(Client client, MongoSportsFacility sportsFacility, LocalDateTime startDate, LocalDateTime endDate) {
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
