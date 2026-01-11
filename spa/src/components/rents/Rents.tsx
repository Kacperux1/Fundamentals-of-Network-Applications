import {useState, useEffect} from 'react';
import getAllRents from "./services/RentService.ts";
import {endRent, deleteRent} from "./services/RentService.ts";
import type {Rent} from '../../utils/typedefs.ts';
import {NavLink, Outlet} from "react-router-dom";

function Rents(){

    const [currentRents, setCurrentRents] = useState<Rent[]>([]);

    function updateCurrentRents(){
        getAllRents().then((rents: Rent[]) => {
            setCurrentRents(rents);
        })
    }

    function endGivenRent(rentId:string){
        const maybeRent = currentRents.find((rent:Rent) => rent.rentId === rentId);
        if(!window.confirm(`Na pewno chcesz zakończyć wypożyczenie o ID ${rentId} ?`)) {
            return;
        }
        if(maybeRent &&maybeRent.startDate >= new Date(Date.now())) {
            alert(`Nie można zakończyć rezerwacji zaczynających się w przyszłości,
             w celu anulowania przyszłych rezerwacji należy dokonać usunięcia rezerwacji
            `);
            return;
        }
        endRent(rentId).then((rent: Rent) => {
            alert(`Zakończono rezerwację o ID ${rent.rentId}, całkowity koszt wypożyczenia obiektu: ${rent.totalPrice} zł`)
            updateCurrentRents();
        });
    }

    function deleteGivenRent(id:string){
        if(!window.confirm(`Na pewno chcesz usunąć wypożyczenie o ID ${id} ?`)) {
            return;
        }
        deleteRent(id).then((rent: Rent) => {
            alert(`Usunięto planowaną rezerwację o ID:${rent.rentId}`);
            updateCurrentRents();
        })
    }

    useEffect(() => {
        updateCurrentRents();
    }, [currentRents])

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

                        {rent.endDate ===null && <button onClick={() => {
                            endGivenRent(rent.rentId);
                        }}>Zakończ rezerwację</button>
                        }

                        {rent.endDate ===null && <button onClick={() => {deleteGivenRent(rent.rentId)}}>
                            Usuń rezerwację
                        </button>
                        }

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