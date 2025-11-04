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

import java.util.UUID;

@Getter
@NoArgsConstructor
@BsonDiscriminator(value="user")
public abstract class User {

    @BsonId
    @BsonProperty("_id")
    private UUID id;
    @BsonProperty("login")
    @NotNull
    @NotBlank
    private String login;
    @BsonProperty("email")
    private String email;
    @Setter
    @BsonProperty("active")
    private boolean active;
    @BsonCreator
    public User(@BsonProperty("login") String login,@BsonProperty("email") String email,
                @BsonProperty("active") boolean active) {
        this.id = UUID.randomUUID();
        this.login = login;
        this.email = email;
        this.active = active;
    }

}
