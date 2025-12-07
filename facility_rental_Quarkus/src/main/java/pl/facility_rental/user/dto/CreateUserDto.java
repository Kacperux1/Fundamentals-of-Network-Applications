package pl.facility_rental.user.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.facility_rental.user.dto.admin.CreateAdminDto;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;

@JsonTypeInfo(
        use =  JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateClientDto.class, name = "client"),
        @JsonSubTypes.Type(value = CreateAdminDto.class, name = "administrator"),
        @JsonSubTypes.Type(value = CreateResourceMgrDto.class, name = "resourceMgr")
})
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public abstract class CreateUserDto {
    @NotBlank
    @Size(min=1, message = "login jest zbyt krotki")
    @Size(max=50, message = "login jest zbyt dlugi")
    private  String login;

    @NotBlank
    @Size(min=1, message = "email jest zbyt krotki")
    @Size(max=50, message = "email jest zbyt dlugi")
    @Email(message="Niepoprawny format emiala")
    private String email;

    @NotNull
    private boolean active;
}
