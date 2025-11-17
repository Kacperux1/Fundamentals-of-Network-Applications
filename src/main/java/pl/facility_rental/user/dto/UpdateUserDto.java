package pl.facility_rental.user.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
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

    private  String login;

    private String email;

}
