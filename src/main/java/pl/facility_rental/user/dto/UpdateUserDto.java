package pl.facility_rental.user.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.facility_rental.user.dto.admin.UpdateAdminDto;
import pl.facility_rental.user.dto.client.UpdateClientDto;
import pl.facility_rental.user.dto.manager.UpdateResourceMgrDto;


@JsonTypeInfo(
        use =  JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateClientDto.class, name = "client"),
        @JsonSubTypes.Type(value = UpdateAdminDto.class, name = "administrator"),
        @JsonSubTypes.Type(value = UpdateResourceMgrDto.class, name = "resourceMgr")
})
@AllArgsConstructor
@Getter
@NoArgsConstructor
public abstract class UpdateUserDto {

    @NotBlank
    @Size(min=1, message = "login jest zbyt krotki")
    @Size(min=50, message = "login jest zbyt dlugi")
    private  String login;

    @NotBlank
    @Size(min=1, message = "email jest zbyt krotki")
    @Size(min=50, message = "email jest zbyt dlugi")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message="Niepoprawny format emiala")
    private String email;

}
