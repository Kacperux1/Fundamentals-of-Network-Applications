package pl.facility_rental.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Getter
@NoArgsConstructor
public abstract class CreateUserDto {

    private  String login;

    private String email;

    private boolean active;

}
