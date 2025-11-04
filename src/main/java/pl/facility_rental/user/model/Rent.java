package pl.facility_rental.user.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@BsonDiscriminator("rents")
public class Rent {
    @BsonId
    private UUID id;
    @BsonProperty("client")
    private Client client;
    @BsonProperty("facility")
    private SportsFacility sportsFacility;
    @BsonProperty("start_date")
    private LocalDateTime startDate;
    @Setter
    @BsonProperty("end_date")
    private LocalDateTime endDate;
    @BsonProperty("total_price")
    private BigDecimal totalPrice;

    @BsonCreator
    public Rent(@BsonProperty("client") Client client, @BsonProperty("facility") SportsFacility sportsFacility,
                @BsonProperty("start_date") LocalDateTime startDate, @BsonProperty("end_date") LocalDateTime endDate) {
        this.id =  UUID.randomUUID();
        this.client = client;
        this.sportsFacility = sportsFacility;
        this.startDate = startDate;
        this.endDate = endDate;
        try {
            this.totalPrice = sportsFacility.getPricePerHour().multiply(BigDecimal.valueOf(Duration.between(startDate, endDate).toHours()));
        } catch (NullPointerException e) {
            this.totalPrice = BigDecimal.ZERO;
        }
    }

}
