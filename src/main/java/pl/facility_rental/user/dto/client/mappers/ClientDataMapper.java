package pl.facility_rental.user.dto.client.mappers;



import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.model.MongoDbClient;

@Component
public class ClientDataMapper {


    public MongoDbClient mapToDataLayer(Client client) {

        if(client.getId() ==null) {
            return new MongoDbClient(client.getLogin(), client.getEmail(), client.getPassword(),client.isActive(),
                    client.getFirstName(), client.getLastName(), client.getPhone());
        }
        return new MongoDbClient(new ObjectId(client.getId()),client.getLogin(), client.getEmail(),client.getPassword(), client.isActive(),
                client.getFirstName(), client.getLastName(), client.getPhone());
    }

    public Client mapToBusinessLayer(MongoDbClient client) {
        return new Client(client.getId().toHexString(),client.getLogin(), client.getEmail(),client.getPassword(), client.isActive(),
                client.getFirstName(), client.getLastName(), client.getPhone());
    }
}
