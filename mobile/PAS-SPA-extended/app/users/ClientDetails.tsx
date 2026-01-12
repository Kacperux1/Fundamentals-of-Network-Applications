import {useState, useEffect} from 'react';
import type {Client, Rent} from '../../src/utils/typedefs.ts';
import {getClientsRents} from "@/src/api/rent/RentService";
import {getUserById} from "@/src/api/user/UserService";
import {Text, View} from 'react-native';
import {useRoute} from "@react-navigation/core";

//toDo: popsuty cykl zycia i sie nie odswieza tak samo jak lista usero

function ClientDetails() {

    const [client, setClient] = useState<Client | null>(null);


    const route = useRoute();
    const {clientId} = route.params;

    console.log("render ClientDetails", clientId);


    const [clientsRents, setClientsRents] = useState<Rent[]>([]);

    //ponizej do zmiany: nulle   use effect sa tyko dlatego zeby dane sie odsiezały
    function getClientInfo() {
        // if (!clientId) {
        //     throw new Error("Client ID is missing");
        // }
        getUserById(clientId).then((client: Client) => {
            setClient(client);
        })
    }

    function updateClientsRents() {
        // if (!clientId) {
        //     throw new Error("Client ID is missing");
        // }
        console.log("Rents from API:", clientsRents);
        getClientsRents(clientId).then((rents: Rent[]) => {
            setClientsRents(rents);
        })
    }

    useEffect(() => {

        if(!clientId) return;

        setClient(null);
        setClientsRents([]);

        getClientInfo();
        updateClientsRents();
    }, [clientId])

    return (
        <View>
            <Text>Szczegóły klienta:</Text>
            <Text>Identyfikator: {client?.id}</Text>
            <Text>Imię: {client?.first_name}</Text>
            <Text>Nazwisko: {client?.last_name}</Text>
            <Text>login: {client?.login}</Text>
            <Text>email: {client?.email}</Text>
            <Text>telefon: {client?.phone}</Text>
            <Text>stan: {client?.active? "aktywny": "nieaktywny"}</Text>
            <Text>Wypożyczenia klienta:</Text>
            <ul>
                {clientsRents.map((rent: Rent) => (
                    <li key={rent.rentId}> {rent.rentId}, {rent.facilityName}, {rent.startDate.toLocaleString()},
                        {rent.endDate.toLocaleString()}
                        {rent.totalPrice}</li>
                ))}
            </ul>
        </View>
    )
}


export default ClientDetails;