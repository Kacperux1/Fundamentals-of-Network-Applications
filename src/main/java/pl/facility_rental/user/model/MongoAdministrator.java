package pl.facility_rental.user.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.UUID;

@Getter
@NoArgsConstructor
@BsonDiscriminator(key = "_class", value="administrator")
public class MongoAdministrator extends MongoUser {
//    @BsonProperty("first_name")
//    private String firstName;
//    @BsonProperty("last_name")
//    private String lastName;

    @BsonCreator
    public MongoAdministrator(@BsonProperty("_id") ObjectId id, @BsonProperty("login") String login, @BsonProperty("email") String email,
                              @BsonProperty("password") String password,@BsonProperty("active") boolean status //@BsonProperty("first_name") String firstName,
                              //@BsonProperty("last_name") String lastName//
                ) {
        super(id, login, email, password, status);
//        this.firstName = firstName;
//        this.lastName = lastName;
    }

    public MongoAdministrator(String login, String email,String password, boolean status) {
        super(login, email, password,status);
    }

}
