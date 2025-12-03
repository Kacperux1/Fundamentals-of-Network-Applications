package pl.facility_rental.user.dto.client.mappers;



import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.model.MongoDbClient;

@ApplicationScoped
public class ClientDataMapper {


    public MongoDbClient mapToDataLayer(Client client) {

        if(client.getId() ==null) {
            return new MongoDbClient(client.getLogin(), client.getEmail(), client.isActive(),
                    client.getFirstName(), client.getLastName(), client.getPhone());
        }
        return new MongoDbClient((new ObjectId(client.getId())),client.getLogin(), client.getEmail(), client.isActive(),
                client.getFirstName(), client.getLastName(), client.getPhone());
    }

    public Client mapToBusinessLayer(MongoDbClient client) {
        return new Client(client.getId().toHexString(),client.getLogin(), client.getEmail(), client.isActive(),
                client.getFirstName(), client.getLastName(), client.getPhone());
    }
}
