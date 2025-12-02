package pl.facility_rental.user.dto;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.facility_rental.user.dto.admin.ReturnedAdminDto;
import pl.facility_rental.user.dto.client.ReturnedClientDto;
import pl.facility_rental.user.dto.manager.ReturnedResourceMgrDto;

@JsonTypeInfo(
        use =  JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReturnedClientDto.class, name = "client"),
        @JsonSubTypes.Type(value = ReturnedAdminDto.class, name = "administrator"),
        @JsonSubTypes.Type(value = ReturnedResourceMgrDto.class, name = "resourceMgr")
})
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class ReturnedUserDto {

    private String id;

    private String login;

    private String email;

    private boolean active;

}
