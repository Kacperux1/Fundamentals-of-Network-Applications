import {useState, useEffect, useContext} from 'react';
import type {Client, Rent} from '../../utils/typedefs.ts';

import {getUserByLogin} from "./services/UserService.ts";
import {getClientsRents} from "../rents/services/RentService.ts";
import {UserContext} from "./context/UserContext.ts";

//toDo: popsuty cykl zycia i sie nie odswieza tak samo jak lista usero

function ClientOwnDetails() {

    const [client, setClient] = useState<Client | null>(null);

    const context = useContext(UserContext);
    const {payload} = context!;

    console.log("render ClientDetails", payload);


    const [clientsRents, setClientsRents] = useState<Rent[]>([]);


    useEffect(() => {
        if (!payload || !payload.sub) return;

        getUserByLogin(payload.sub!)
            .then((client: Client) => {
                setClient(client);
               getClientsRents(client.id).then((rents: Rent[]) => {
                   setClientsRents(rents);
               })
            });
    }, [payload]);


    return (
        <div>
            <h1>Twoje konto</h1>
            <p>Identyfikator: {client?.id}</p>
            <p>Imię: {client?.first_name}</p>
            <p>Nazwisko: {client?.last_name}</p>
            <p>login: {client?.login}</p>
            <p>email: {client?.email}</p>
            <p>telefon: {client?.phone}</p>
            <h2>Twoje wypożyczenia:</h2>
            <ul className="border-amber-400 border-6 rounded-xl text-xl borde">
                {clientsRents.map((rent: Rent) => (
                    <li key={rent.rentId}> {rent.rentId}, {rent.facilityName}, {rent.startDate.toLocaleString()},
                        {rent.endDate?.toLocaleString()}
                        {rent.totalPrice}</li>
                ))}
            </ul>
        </div>
    )
}


export default ClientOwnDetails;