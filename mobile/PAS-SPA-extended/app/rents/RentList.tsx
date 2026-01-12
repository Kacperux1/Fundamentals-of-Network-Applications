import {useState, useEffect} from 'react';
import type {Rent} from '../../src/utils/typedefs.ts';
import getAllRents, {deleteRent, endRent} from "@/src/api/rent/RentService";
import {View, Text, Alert, FlatList, Pressable, ScrollView} from "react-native";
import { Link } from 'expo-router'; // Zamiast NavLink

function RentList(){
    const [currentRents, setCurrentRents] = useState<Rent[]>([]);

    function updateCurrentRents(){
        getAllRents().then((rents: Rent[]) => {
            setCurrentRents(rents);
        })
    }

    function endGivenRent(rentId:string){
        const maybeRent = currentRents.find((rent:Rent) => rent.rentId === rentId);

        Alert.alert('Na pewno?', `Na pewno chcesz zakończyć wypożyczenie o ID ${rentId} ?`, [
            {text: "Anuluj", style: 'cancel'},
            {
                text: "Tak",
                onPress: () => {
                    if(maybeRent && maybeRent.startDate >= new Date(Date.now())) {
                        Alert.alert('Błąd!', `Nie można zakończyć rezerwacji zaczynających się w przyszłości, w celu anulowania przyszłych rezerwacji należy dokonać usunięcia rezerwacji`);
                        return;
                    }
                    endRent(rentId).then((rent: Rent) => {
                        Alert.alert('Sukces!', `Zakończono rezerwację o ID ${rent.rentId}, całkowity koszt wypożyczenia obiektu: ${rent.totalPrice} zł`);
                        updateCurrentRents();
                    });
                }
            },
        ]);
    }

    function deleteGivenRent(id:string){
        Alert.alert('Na pewno?', `Na pewno chcesz usunąć wypożyczenie o ID ${id} ?`, [
            {text: "Anuluj", style: 'cancel'},
            {
                text: "Tak",
                onPress: () => {
                    deleteRent(id).then((rent: Rent) => {
                        Alert.alert('Sukces!', `Usunięto planowaną rezerwację o ID:${rent.rentId}`);
                        updateCurrentRents();
                    })
                }
            },
        ]);
    }

    useEffect(() => {
        updateCurrentRents();
    }, [])

    return (
        <ScrollView>
            <Text className="text-xl">Lista rezerwacji obiektów sportowych:</Text>
            <FlatList
                data={currentRents}
                keyExtractor={(item) => item.rentId}
                renderItem={({item: rent}) => (
                    <View key={rent.rentId} className="m-2 rounded-xl border-2 border-yellow-600 text-lg h-35 p-4">
                        <Text>
                            Klient: {rent.firstName} {rent.lastName}, {rent.email}
                        </Text>
                        <Text>
                            obiekt sportowy: {rent.facilityName}, {rent.street} {rent.streetNumber}, {rent.city}
                        </Text>
                        <Text>
                            Początek: {rent.startDate.toLocaleString()} Koniec:
                            {rent.endDate === null ? "nieokreślony" : rent.endDate.toLocaleString()}
                        </Text>
                        <Text>
                            koszt rezerwacji: {rent.endDate === null ? "rezerwacja jeszcze niezakończona" : rent.totalPrice}
                        </Text>

                        {rent.endDate === null && (
                            <Pressable onPress={() => { endGivenRent(rent.rentId); }}>
                                <Text>Zakończ rezerwację</Text>
                            </Pressable>
                        )}

                        {rent.endDate === null && (
                            <Pressable onPress={() => { deleteGivenRent(rent.rentId) }}>
                                <Text>Usuń rezerwację</Text>
                            </Pressable>
                        )}
                    </View>
                )}
            />

            <Link href="/rents/CreateRent">
                <View className="m-4">
                    <Text>Stwórz nową rezerwację</Text>
                </View>
            </Link>
        </ScrollView>
    )
}

export default RentList;