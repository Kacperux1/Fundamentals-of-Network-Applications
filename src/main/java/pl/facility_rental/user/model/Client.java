package pl.facility_rental.user.model;


import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@BsonDiscriminator(key="_clazz", value = "Client")
public class Client extends User {
    @BsonProperty("first_name")
    private String firstName;
    @BsonProperty("last_name")
    private String lastName;
    @BsonProperty("phone")
    private String phone;

    @BsonCreator
    public Client(@BsonProperty("login") String login, @BsonProperty("email")String email,
                  @BsonProperty("status") boolean status, @BsonProperty("first_name") String firstName,
                  @BsonProperty("last_name") String lastName,@BsonProperty("phone") String phone) {
        super(login, email, status);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
