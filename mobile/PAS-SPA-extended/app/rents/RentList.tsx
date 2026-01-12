import {useState, useEffect} from 'react';
import type {Rent} from '../../src/utils/typedefs.ts';
import getAllRents, {deleteRent, endRent} from "@/src/api/rent/RentService";
import {View, Text, Alert, FlatList, Pressable, SafeAreaView} from "react-native";
import { useRouter } from 'expo-router';

function RentList(){
    const [currentRents, setCurrentRents] = useState<Rent[]>([]);
    const router = useRouter();

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
        <SafeAreaView className="flex-1">
            <View className="p-4">
                <Pressable
                    onPress={() => router.push('/rents/CreateRent')}
                    className="bg-blue-600 p-4 rounded-lg mb-4"
                >
                    <Text className="text-white text-center font-bold text-lg">
                        Stwórz nową rezerwację
                    </Text>
                </Pressable>
            </View>

            <View className="flex-1 px-4">
                <Text className="text-xl font-bold mb-4">Lista rezerwacji obiektów sportowych:</Text>

                <FlatList
                    data={currentRents}
                    keyExtractor={(item) => item.rentId}
                    renderItem={({item: rent}) => (
                        <View key={rent.rentId} className="mb-4 rounded-xl border-2 border-yellow-600 p-4">
                            <Text className="mb-1">
                                <Text className="font-semibold">Klient:</Text> {rent.firstName} {rent.lastName}, {rent.email}
                            </Text>
                            <Text className="mb-1">
                                <Text className="font-semibold">Obiekt sportowy:</Text> {rent.facilityName}, {rent.street} {rent.streetNumber}, {rent.city}
                            </Text>
                            <Text className="mb-1">
                                <Text className="font-semibold">Początek:</Text> {rent.startDate.toLocaleString()}
                            </Text>
                            <Text className="mb-1">
                                <Text className="font-semibold">Koniec:</Text> {rent.endDate === null ? "nieokreślony" : rent.endDate.toLocaleString()}
                            </Text>
                            <Text className="mb-3">
                                <Text className="font-semibold">Koszt rezerwacji:</Text> {rent.endDate === null ? "rezerwacja jeszcze niezakończona" : rent.totalPrice}
                            </Text>

                            <View className="flex-row space-x-3">
                                {rent.endDate === null && (
                                    <Pressable
                                        onPress={() => { endGivenRent(rent.rentId); }}
                                        className="bg-green-500 px-4 py-2 rounded flex-1"
                                    >
                                        <Text className="text-white text-center font-semibold">Zakończ rezerwację</Text>
                                    </Pressable>
                                )}

                                {rent.endDate === null && (
                                    <Pressable
                                        onPress={() => { deleteGivenRent(rent.rentId) }}
                                        className="bg-red-500 px-4 py-2 rounded flex-1"
                                    >
                                        <Text className="text-white text-center font-semibold">Usuń rezerwację</Text>
                                    </Pressable>
                                )}
                            </View>
                        </View>
                    )}
                    ListEmptyComponent={
                        <Text className="text-center mt-8 text-gray-500">
                            Brak rezerwacji do wyświetlenia
                        </Text>
                    }
                    contentContainerStyle={{ paddingBottom: 20 }}
                />
            </View>
        </SafeAreaView>
    )
}

export default RentList;