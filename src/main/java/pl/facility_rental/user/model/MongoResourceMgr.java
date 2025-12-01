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
@BsonDiscriminator(key = "_class", value="resourceMgr")
public class MongoResourceMgr extends MongoUser {
//    @BsonProperty
//    private String firstName;
//    @BsonProperty("last_name")
//    private String lastName;

    @BsonCreator
    public MongoResourceMgr(@BsonProperty("_id")ObjectId id,@BsonProperty("login") String login, @BsonProperty("email") String email,
                            @BsonProperty("active") boolean active //@BsonProperty("first_name") String firstName,
                            //@BsonProperty("last_name") String lastName
    ) {
        super(id, login, email, active);
//        this.firstName = firstName;
//        this.lastName = lastName;
    }

    public MongoResourceMgr( String login, String email, boolean active) {
        super(login, email, active);
    }
}
