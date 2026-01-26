import {useState, useEffect, useContext} from 'react';
import getAllRents, {getClientForRentHateoas, getFacilityForRentHateoa} from "./services/RentService.ts";
import {endRent, deleteRent} from "./services/RentService.ts";
import type {Client, Facility, Rent, RentHateOas} from '../../utils/typedefs.ts';
import {NavLink} from "react-router-dom";
import {UserContext} from "../users/context/UserContext.ts";


function Rents(){

    const context = useContext(UserContext);
    const {payload}  = context!;
    const [currentRents, setCurrentRents] = useState<RentHateOas[]>([]);

    function updateCurrentRents(){
        getAllRents().then((rents: RentHateOas[]) => {
            setCurrentRents(rents);
        })
    }

    function endGivenRent(rentId:string){
        const maybeRent = currentRents.find((rent:RentHateOas) => rent.rentId === rentId);
        if(!window.confirm(`Na pewno chcesz zakończyć wypożyczenie o ID ${rentId} ?`)) {
            return;
        }
        // eslint-disable-next-line react-hooks/purity
        if(maybeRent &&maybeRent.startDate >= new Date(Date.now())) {
            alert(`Nie można zakończyć rezerwacji zaczynających się w przyszłości,
             w celu anulowania przyszłych rezerwacji należy dokonać usunięcia rezerwacji
            `);
            return;
        }
        const closeLink = maybeRent?.links.find(link => link.rel === "close");
        if(!closeLink){
            alert("Ni ma linka dla zakunczenia!");
            return;
        }
        endRent(closeLink?.href).then((rent: RentHateOas) => {
            alert(`Zakończono rezerwację o ID ${rent.rentId}, całkowity koszt wypożyczenia obiektu: ${rent.totalPrice} zł`)
            updateCurrentRents();
        });
    }

    function deleteGivenRent(id:string){
        const maybeRent = currentRents.find((rent:RentHateOas) => rent.rentId === id);
        if(!window.confirm(`Na pewno chcesz usunąć wypożyczenie o ID ${id} ?`)) {
            return;
        }
        const deleteLink = maybeRent?.links.find(link => link.rel === "delete");
        if(!deleteLink || !deleteLink.href){
            alert("Ni ma linka dla ósónięća!");
            return;
        }
        deleteRent(deleteLink.href).then((rent: RentHateOas) => {
            alert(`Usunięto planowaną rezerwację o ID:${rent.rentId}`);
            updateCurrentRents();
        })
    }

    function getClientForRent(rentId:string){
        const maybeRent = currentRents.find((rent:RentHateOas) => rent.rentId === rentId);

        const clientLink = maybeRent?.links.find(link => link.rel === "client");
        if(!clientLink || !clientLink.href){
            alert("Ni ma linka dla klięta");
        }
        getClientForRentHateoas(clientLink.href).then((client: Client) => {
            alert(`login:  ${client.login}, email: ${client.email}, status:`
                + (client.active ? `aktywny` : `nieaktywny` + `imię: ${client.first_name}, nazisko: ${client.last_name},
                 telefon : ${client.phone}`), );
        })
    }

    function getFacilityForRent(rentId:string){
        const maybeRent = currentRents.find((rent:RentHateOas) => rent.rentId === rentId);
        const facilityLink = maybeRent?.links.find(link => link.rel === "facility");
        if(!facilityLink || !facilityLink.href){
            alert("Ni ma linka dla obiektu");
        }
        getFacilityForRentHateoa(facilityLink.href).then((facility: Facility) => {
            alert(`obiekt o nazwie ${facility.name},
            na ulicy ${facility.street} ${facility.streetNumber}, o kodzie pocztowym ${facility.postalCode} ${facility.city},
            o stawce za godzinę ${facility.price} zł?`)
        });
    }

    useEffect(() => {
        updateCurrentRents();
    }, [payload])

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
                        <button onClick={() => {
                            getFacilityForRent(rent.rentId);
                        }}>Pokaż ypożyczony obiekt</button>
                        <button onClick={() => {
                            getClientForRent(rent.rentId);
                        }}>Pokaż ypożyczającego klienta</button>

                    </li>
                ))}
            </ul>
            <NavLink to = "/createRent">
                <button className ="m-4">
                    Stwórz nową rezerwację
                </button>
            </NavLink>

        </>
    )
}

export default Rents;