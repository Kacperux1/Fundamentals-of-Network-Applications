package pl.facility_rental.user.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;
import pl.facility_rental.user.dto.client.CreateClientDto;
import pl.facility_rental.user.dto.admin.CreateAdminDto;
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
@NoArgsConstructor
public abstract class CreateUserDto {
    @NotBlank
    private  String login;

    @NotBlank
    private String email;

    @NotNull
    private boolean active;
}
