import {useState, useEffect} from 'react';
import type {Client, Facility, RentForm, Rent} from '../../src/utils/typedefs.ts';
import * as yup from 'yup';
import getAllFacilities from "@/src/api/facility/FacilityService";
import {createRent} from "@/src/api/rent/RentService";
import {Alert, ScrollView, View, Text, Pressable, Modal, FlatList} from "react-native";
import {getAllClients} from "@/src/api/user/UserService";
import DateTimePickerModal from "react-native-modal-datetime-picker";

function CreateRentForm() {
    const [currentClients, setCurrentClients] = useState<Client[]>([]);
    const [currentFacilities, setCurrentFacilities] = useState<Facility[]>([]);
    const [selectedClientId, setSelectedClientId] = useState<string>('');
    const [selectedFacilityId, setSelectedFacilityId] = useState<string>('');
    const [chosenStartDate, setChosenStartDate] = useState<Date>(new Date());
    const [chosenEndDate, setChosenEndDate] = useState<Date | null>(null);
    const [validationError, setValidationError] = useState<string>('');
    const [isStartDatePickerVisible, setStartDatePickerVisible] = useState(false);
    const [isEndDatePickerVisible, setEndDatePickerVisible] = useState(false);
    const [showClientModal, setShowClientModal] = useState(false);
    const [showFacilityModal, setShowFacilityModal] = useState(false);

    const createRentValidationSchema = yup.object({
        facilityId: yup.string().required("Podanie rezerwowanego obiektu jest wymagane!"),
        clientId: yup.string().required("Podanie zamawiającego klienta jest wymagane"),
        startDate: yup.date().required("Data rozpoczęcia jest wymagana do rezerwacji")
    })

    function updateFacilityList() {
        getAllFacilities().then((response) => {
            setCurrentFacilities(response);
        })
    }

    function updateClientList() {
        getAllClients().then((response) => {
            setCurrentClients(response);
        })
    }

    useEffect(() => {
        updateClientList();
        updateFacilityList();
    }, [])

    function sendCreateRentData(data: RentForm) {
        const chosenClient = currentClients.find((client:Client) => client.id === data.clientId);
        const chosenFacility = currentFacilities.find((facility:Facility) => facility.id === data.facilityId);

        const startDateStr = new Date(data.startDate).toLocaleString('pl-PL');
        const endDateStr = data.endDate ? new Date(data.endDate).toLocaleString('pl-PL') : 'nieokreślony';

        Alert.alert('Na pewno?' ,`Na pewno chcesz dodać wypożyczenie:\n\nKlient: ${chosenClient?.first_name} ${chosenClient?.last_name}\nObiekt: ${chosenFacility?.name}\nOd: ${startDateStr}\nDo: ${endDateStr}`, [
            {text: "Anuluj", style: 'cancel'},
            {
                text: "Tak",
                onPress: () => {
                    createRent(data).then((rent: Rent) => {
                        Alert.alert('Sukces', `Wypożyczenie o ID ${rent.rentId} zostało utworzone`);
                        resetForm();
                    }).catch((err) => {
                        Alert.alert('Błąd', err.body?.message || 'Wystąpił błąd podczas tworzenia rezerwacji');
                    });
                }
            },
        ]);
    }

    async function handleCreateRentSubmit() {
        setValidationError('');
        const data: RentForm = {
            clientId: selectedClientId,
            facilityId: selectedFacilityId,
            startDate: chosenStartDate.toISOString(),
            endDate: chosenEndDate ? chosenEndDate.toISOString() : '',
        }
        try {
            await createRentValidationSchema.validate(data);
            if (chosenEndDate && chosenEndDate <= chosenStartDate) {
                setValidationError("Data zakończenia musi być późniejsza niż data rozpoczęcia");
                return;
            }
            sendCreateRentData(data);
        } catch (error: any) {
            setValidationError(error.message);
            return;
        }
    }

    function resetForm() {
        setSelectedClientId('');
        setSelectedFacilityId('');
        setChosenStartDate(new Date());
        setChosenEndDate(null);
    }

    const handleStartDateConfirm = (date: Date) => {
        setChosenStartDate(date);
        setStartDatePickerVisible(false);
    };

    const handleEndDateConfirm = (date: Date) => {
        setChosenEndDate(date);
        setEndDatePickerVisible(false);
    };

    const formatDate = (date: Date | null): string => {
        if (!date) return 'Nie wybrano';
        return date.toLocaleString('pl-PL', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const getSelectedClientName = () => {
        if (!selectedClientId) return 'Wybierz klienta...';
        const client = currentClients.find(c => c.id === selectedClientId);
        return client ? client.login : 'Wybierz klienta...';
    };

    const getSelectedFacilityName = () => {
        if (!selectedFacilityId) return 'Wybierz obiekt...';
        const facility = currentFacilities.find(f => f.id === selectedFacilityId);
        return facility ? facility.name : 'Wybierz obiekt...';
    };

    return (
        <View className="flex-1">
            <ScrollView className="p-4">
                <Text className="text-2xl font-bold mb-6 text-center">Nowa rezerwacja</Text>

                {validationError && (
                    <View className="bg-red-100 p-3 rounded mb-4">
                        <Text className="text-red-700 font-bold">Błąd:</Text>
                        <Text className="text-red-700">{validationError}</Text>
                    </View>
                )}

                <View className="flex-row space-x-3 mb-6">
                    <Pressable
                        onPress={handleCreateRentSubmit}
                        className="flex-1 bg-green-600 p-4 rounded-lg"
                    >
                        <Text className="text-white text-center font-bold text-lg">Stwórz rezerwację</Text>
                    </Pressable>

                    <Pressable
                        onPress={resetForm}
                        className="flex-1 bg-gray-500 p-4 rounded-lg"
                    >
                        <Text className="text-white text-center font-bold text-lg">Wyczyść</Text>
                    </Pressable>
                </View>

                <View className="space-y-6">
                    <View>
                        <Text className="text-lg font-semibold mb-2">Podaj datę początkową:</Text>
                        <Pressable
                            onPress={() => setStartDatePickerVisible(true)}
                            className="w-full border border-gray-300 p-3 rounded bg-white"
                        >
                            <Text className="text-lg">{formatDate(chosenStartDate)}</Text>
                        </Pressable>
                    </View>

                    <View>
                        <Text className="text-lg font-semibold mb-2">Podaj datę końcową (opcjonalnie):</Text>
                        <Pressable
                            onPress={() => setEndDatePickerVisible(true)}
                            className="w-full border border-gray-300 p-3 rounded bg-white"
                        >
                            <Text className="text-lg">{formatDate(chosenEndDate)}</Text>
                        </Pressable>
                    </View>

                    <View>
                        <Text className="text-lg font-semibold mb-2">Wybierz rezerwującego klienta:</Text>
                        <Pressable
                            onPress={() => setShowClientModal(true)}
                            className="w-full border border-gray-300 p-3 rounded bg-white"
                        >
                            <Text className="text-lg">{getSelectedClientName()}</Text>
                        </Pressable>
                    </View>

                    <View>
                        <Text className="text-lg font-semibold mb-2">Wybierz rezerwowany obiekt:</Text>
                        <Pressable
                            onPress={() => setShowFacilityModal(true)}
                            className="w-full border border-gray-300 p-3 rounded bg-white"
                        >
                            <Text className="text-lg">{getSelectedFacilityName()}</Text>
                        </Pressable>
                    </View>
                </View>
            </ScrollView>

            <DateTimePickerModal
                isVisible={isStartDatePickerVisible}
                mode="datetime"
                onConfirm={handleStartDateConfirm}
                onCancel={() => setStartDatePickerVisible(false)}
                minimumDate={new Date()}
                locale="pl_PL"
            />

            <DateTimePickerModal
                isVisible={isEndDatePickerVisible}
                mode="datetime"
                onConfirm={handleEndDateConfirm}
                onCancel={() => setEndDatePickerVisible(false)}
                minimumDate={chosenStartDate}
                locale="pl_PL"
            />

            <Modal visible={showClientModal} animationType="slide">
                <View className="flex-1 p-4 bg-white">
                    <Text className="text-2xl font-bold mb-6 text-center">Wybierz klienta</Text>
                    <FlatList
                        data={currentClients}
                        keyExtractor={(item) => item.id}
                        renderItem={({item}) => (
                            <Pressable
                                onPress={() => {
                                    setSelectedClientId(item.id);
                                    setShowClientModal(false);
                                }}
                                className="p-4 border-b border-gray-300 active:bg-gray-100"
                            >
                                <Text className="text-lg">{item.login}</Text>
                            </Pressable>
                        )}
                        ListEmptyComponent={
                            <Text className="text-center mt-8 text-gray-500">Brak klientów</Text>
                        }
                    />
                    <Pressable
                        onPress={() => setShowClientModal(false)}
                        className="bg-red-500 p-4 mt-4 rounded-lg active:bg-red-600"
                    >
                        <Text className="text-white text-center font-bold">Anuluj</Text>
                    </Pressable>
                </View>
            </Modal>

            <Modal visible={showFacilityModal} animationType="slide">
                <View className="flex-1 p-4 bg-white">
                    <Text className="text-2xl font-bold mb-6 text-center">Wybierz obiekt</Text>
                    <FlatList
                        data={currentFacilities}
                        keyExtractor={(item) => item.id}
                        renderItem={({item}) => (
                            <Pressable
                                onPress={() => {
                                    setSelectedFacilityId(item.id);
                                    setShowFacilityModal(false);
                                }}
                                className="p-4 border-b border-gray-300 active:bg-gray-100"
                            >
                                <Text className="text-lg font-semibold">{item.name}</Text>
                                <Text className="text-gray-600">{item.street} {item.streetNumber}, {item.city}</Text>
                                <Text className="text-gray-600">{item.price} zł/godz.</Text>
                            </Pressable>
                        )}
                        ListEmptyComponent={
                            <Text className="text-center mt-8 text-gray-500">Brak obiektów</Text>
                        }
                    />
                    <Pressable
                        onPress={() => setShowFacilityModal(false)}
                        className="bg-red-500 p-4 mt-4 rounded-lg active:bg-red-600"
                    >
                        <Text className="text-white text-center font-bold">Anuluj</Text>
                    </Pressable>
                </View>
            </Modal>
        </View>
    )
}

export default CreateRentForm;