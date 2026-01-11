import {useState, useEffect} from 'react';
import type {Client, Rent} from '../../utils/typedefs.ts';

import {getUserById} from "./services/UserService.ts";
import {useParams} from "react-router-dom";
import {getClientsRents} from "../rents/services/RentService.ts";

//toDo: popsuty cykl zycia i sie nie odswieza tak samo jak lista usero

function ClientDetails() {

    const [client, setClient] = useState<Client | null>(null);
    const {clientId} = useParams<{ clientId: string }>();

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
        <div>
            <h1>Szczegóły klienta:</h1>
            <p>Identyfikator: {client?.id}</p>
            <p>Imię: {client?.first_name}</p>
            <p>Nazwisko: {client?.last_name}</p>
            <p>login: {client?.login}</p>
            <p>email: {client?.email}</p>
            <p>telefon: {client?.phone}</p>
            <p>stan: {client?.active? "aktywny": "nieaktwyny"}</p>
            <h2>Wypożyczenia klienta:</h2>
            <ul>
                {clientsRents.map((rent: Rent) => (
                    <li key={rent.rentId}> {rent.rentId}, {rent.facilityName}, {rent.startDate.toLocaleString()},
                        {rent.endDate.toLocaleString()}
                        {rent.totalPrice}</li>
                ))}
            </ul>
        </div>
    )
}


export default ClientDetails;