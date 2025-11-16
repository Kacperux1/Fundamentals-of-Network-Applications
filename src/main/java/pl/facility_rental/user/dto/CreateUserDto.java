package pl.facility_rental.user.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
        @JsonSubTypes.Type(value = CreateResourceMgrDto.class, name = "manager")
})
@AllArgsConstructor
@Getter
@NoArgsConstructor
public abstract class CreateUserDto {

    private  String login;

    private String email;

    private boolean active;

}
