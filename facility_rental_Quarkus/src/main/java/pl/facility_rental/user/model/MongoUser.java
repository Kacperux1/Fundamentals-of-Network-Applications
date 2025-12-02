package pl.facility_rental.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@NoArgsConstructor
@BsonDiscriminator(key = "_class", value = "user")
public abstract class MongoUser {

    @BsonProperty("_id")
    private ObjectId id;

    @NotNull
    @NotBlank
    @BsonProperty("login")
    private String login;

    @BsonProperty("email")
    private String email;

    @Setter
    @BsonProperty("active")
    private boolean active;

    @BsonCreator
    public MongoUser(@BsonProperty("_id") ObjectId id,
                     @BsonProperty("login") String login,
                     @BsonProperty("email") String email,
                     @BsonProperty("active") boolean active) {
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
