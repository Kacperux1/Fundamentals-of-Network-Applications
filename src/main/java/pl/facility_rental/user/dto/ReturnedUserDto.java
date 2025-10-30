package pl.facility_rental.user.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnedUserDto {

    private UUID uuid ;

    private String login;

    private String email;

    private boolean status;

}
