    package pl.facility_rental.user.dto.client;


    import com.fasterxml.jackson.annotation.JsonProperty;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import pl.facility_rental.user.dto.ReturnedUserDto;

    import java.util.UUID;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public class ReturnedClientDto extends ReturnedUserDto {
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;
        private String phone;

        public ReturnedClientDto(String id, String login, String email, boolean status, String firstName,
                                 String lastName,  String phone) {
            super(id, login, email, status);
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
        }
    }
