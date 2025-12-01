package pl.facility_rental.rent.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import pl.facility_rental.user.business.model.Client;

import pl.facility_rental.facility.model.MongoSportsFacility;
import pl.facility_rental.user.model.MongoDbClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class MongoRent {
    @BsonId
    private ObjectId id;
    @BsonProperty("client")
    private MongoDbClient client;
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
    public MongoRent(@BsonId ObjectId id, @BsonProperty("client") MongoDbClient client, @BsonProperty("facility") MongoSportsFacility sportsFacility,
                     @BsonProperty("start_date") LocalDateTime startDate, @BsonProperty("end_date") LocalDateTime endDate,
                     @BsonProperty("total_price") BigDecimal totalPrice) {
        this.id =  id;
        this.client = client;
        this.sportsFacility = sportsFacility;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
    }

    public MongoRent(MongoDbClient client, MongoSportsFacility sportsFacility, LocalDateTime startDate, LocalDateTime endDate) {
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

    public BigDecimal calculateTotalPriceIfEndedNow() {
        return BigDecimal.valueOf(
                Math.ceil(Duration.between(startDate, LocalDateTime.now()).toMinutes())/60.0).multiply(sportsFacility.getPricePerHour());
    }

}
