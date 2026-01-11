import {useState, useEffect} from 'react';
import getAllRents from "./services/RentService.ts";
import type {Rent} from '../../utils/typedefs.ts';
import {NavLink, Outlet} from "react-router-dom";

function Rents(){

    const [currentRents, setCurrentRents] = useState<Rent[]>([]);

    function updateCurrentRents(){
        getAllRents().then((rents: Rent[]) => {
            setCurrentRents(rents);
        })
    }

    useEffect(() => {
        updateCurrentRents();
    }, [])

    return (
        <>
            <h2 className ="text-xl">Lista rezerwacji obiektów sportowych:</h2>
            <ul>
                {currentRents.map((rent: Rent) => (
                    <li key = {rent.rentId} className=" m-2 rounded-xl border-2 border-yellow-600 text-lg h-35 p-4">
                        Klient: {rent.firstName} {rent.lastName}, {rent.email} <br/>
                        obiekt sportowy: {rent.facilityName}, {rent.street} {rent.streetNumber}, {rent.city} <br/>
                        Początek: {rent.startDate.toLocaleString()} Koniec:
                        {rent.endDate ===null? "nieokreślony" : rent.endDate.toLocaleString()} <br/>
                        koszt rezerwacji: {rent.endDate===null? "rezerwacja jeszcze niezakończona": rent.totalPrice}
                    </li>
                ))}
            </ul>
            <NavLink to = "/rentsView/createRent">
                <button className ="m-4">
                    Stwórz nową rezerwację
                </button>
            </NavLink>
            <div className="flex justify-center">
                <Outlet/>
            </div>

        </>
    )
}

export default Rents;