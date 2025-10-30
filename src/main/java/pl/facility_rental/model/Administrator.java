package pl.facility_rental.model;


import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@BsonDiscriminator(key="_class", value="administrator")
public class Administrator extends User{
    @BsonProperty("first_name")
    private String firstName;
    @BsonProperty("last_name")
    private String lastName;

    @BsonCreator
    public Administrator(@BsonProperty("login") String login, @BsonProperty("email") String email,
            @BsonProperty("status") boolean status, @BsonProperty("first_name") String firstName,
            @BsonProperty("last_name") String lastName) {
        super(login, email, status);
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
