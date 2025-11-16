package pl.facility_rental.user.model;


import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@BsonDiscriminator(key="_class", value="administrator")
public class MongoAdministrator extends MongoUser {
//    @BsonProperty("first_name")
//    private String firstName;
//    @BsonProperty("last_name")
//    private String lastName;

    @BsonCreator
    public MongoAdministrator(@BsonProperty("login") String login, @BsonProperty("email") String email,
                              @BsonProperty("status") boolean status //@BsonProperty("first_name") String firstName,
                              //@BsonProperty("last_name") String lastName//
                ) {
        super(login, email, status);
//        this.firstName = firstName;
//        this.lastName = lastName;
    }

    public MongoAdministrator(String uuid, String login, String email, boolean status) {
        super(uuid, login, email, status);
    }

}
