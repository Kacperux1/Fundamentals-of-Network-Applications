
import {useState, useEffect} from 'react';
import type {Rent} from '../../src/utils/typedefs.ts';
import getAllRents, {deleteRent, endRent} from "@/src/api/rent/RentService";
import {View, Text, Alert} from "react-native";

function RentList(){

    const [currentRents, setCurrentRents] = useState<Rent[]>([]);

    const [confirmedAction, setConfirmedAction] = useState<boolean|null>(null);

    function updateCurrentRents(){
        getAllRents().then((rents: Rent[]) => {
            setCurrentRents(rents);
        })
    }

    function showConfirmationAlert(message: string) {
        Alert.alert('Na pewno?', message, [
            {text: "Tak", onPress: () => {setConfirmedAction(true)}},
            {text: 'Nie', onPress: () => {setConfirmedAction(false)}},
        ]);
    }

    function endGivenRent(rentId:string){
        const maybeRent = currentRents.find((rent:Rent) => rent.rentId === rentId);
        showConfirmationAlert(`Na pewno chcesz zakończyć wypożyczenie o ID ${rentId} ?`);
        if(!confirmedAction) {
            setConfirmedAction(null);
            return;
        }
        if(maybeRent &&maybeRent.startDate >= new Date(Date.now())) {
            Alert.alert('Błont!',`Nie można zakończyć rezerwacji zaczynających się w przyszłości,
             w celu anulowania przyszłych rezerwacji należy dokonać usunięcia rezerwacji
            `);
            setConfirmedAction(null);
            return;
        }
        endRent(rentId).then((rent: Rent) => {
            Alert.alert('Sukces!',`Zakończono rezerwację o ID ${rent.rentId}, całkowity koszt wypożyczenia obiektu: ${rent.totalPrice} zł`)
            updateCurrentRents();
            setConfirmedAction(null);
        });
    }

    function deleteGivenRent(id:string){
        showConfirmationAlert(`Na pewno chcesz usunąć wypożyczenie o ID ${id} ?`)
        if(!confirmedAction) {
            setConfirmedAction(null);
            return;
        }
        deleteRent(id).then((rent: Rent) => {
            Alert.alert('Sukces!', `Usunięto planowaną rezerwację o ID:${rent.rentId}`);
            updateCurrentRents();
            setConfirmedAction(null);
        })
    }

    useEffect(() => {
        updateCurrentRents();
    }, [currentRents])

    return (
        <>
            <Text className ="text-xl">Lista rezerwacji obiektów sportowych:</Text>
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
            <View className="flex justify-center">
                <Outlet/>
            </View>

        </>
    )
}

export default RentList;
