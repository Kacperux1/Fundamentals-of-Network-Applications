package pl.facility_rental.user.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.UUID;

@Getter
@BsonDiscriminator(key = "_class", value = "client")
@NoArgsConstructor
public class MongoDbClient extends MongoUser {
    @BsonProperty("first_name")
    private String firstName;
    @BsonProperty("last_name")
    private String lastName;
    @BsonProperty("phone")
    private String phone;

    @BsonCreator
    public MongoDbClient(@BsonProperty("_id") ObjectId id, @BsonProperty("login") String login, @BsonProperty("email")String email,
                         @BsonProperty("password") String password,@BsonProperty("active") boolean active, @BsonProperty("first_name") String firstName,
                         @BsonProperty("last_name") String lastName, @BsonProperty("phone") String phone) {
        super(id, login, email, password, active);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public MongoDbClient(String login, String email, String password,boolean active, String firstName, String lastName, String phone) {
        super(login, email, password, active);
        this.phone = phone;
        this.lastName = lastName;
        this.firstName = firstName;
    }
}
