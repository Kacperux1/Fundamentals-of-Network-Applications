package pl.facility_rental.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;



import java.util.UUID;

@Getter
@NoArgsConstructor
@BsonDiscriminator(key = "_class", value = "user")
public abstract class MongoUser {


    private ObjectId id;
    @NotNull
    @NotBlank
    private String login;
    private String email;
    @Setter
    private boolean active;
    protected MongoUser(ObjectId id, String login,String email,
                     boolean active) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.active = active;
    }

    public MongoUser(String login, String email, boolean active) {
        this.login = login;
        this.email = email;
        this.active = active;
    }

}
