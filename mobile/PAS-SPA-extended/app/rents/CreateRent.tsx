import {useState, useEffect} from 'react';
import type {Client, Facility, RentForm, Rent} from '../../src/utils/typedefs.ts';
import * as yup from 'yup';
import getAllFacilities from "@/src/api/facility/FacilityService";
import {createRent} from "@/src/api/rent/RentService";
import {Alert, ScrollView, View, Text, Pressable, Platform} from "react-native";
import {getAllClients} from "@/src/api/user/UserService";
import { Picker } from "@react-native-picker/picker";
import DateTimePicker from '@react-native-community/datetimepicker';

function CreateRentForm() {
    const [currentClients, setCurrentClients] = useState<Client[]>([]);
    const [currentFacilities, setCurrentFacilities] = useState<Facility[]>([]);
    const [selectedClientId, setSelectedClientId] = useState<string>('');
    const [selectedFacilityId, setSelectedFacilityId] = useState<string>('');
    const [chosenStartDate, setChosenStartDate] = useState<Date>(new Date());
    const [chosenEndDate, setChosenEndDate] = useState<Date | null>(null);
    const [validationError, setValidationError] = useState<string>('');
    const [showStartDatePicker, setShowStartDatePicker] = useState(false);
    const [showEndDatePicker, setShowEndDatePicker] = useState(false);

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

    const onStartDateChange = (event: any, selectedDate?: Date) => {
        setShowStartDatePicker(Platform.OS === 'ios');
        if (selectedDate) {
            setChosenStartDate(selectedDate);
        }
    };

    const onEndDateChange = (event: any, selectedDate?: Date) => {
        setShowEndDatePicker(Platform.OS === 'ios');
        if (selectedDate) {
            setChosenEndDate(selectedDate);
        } else {
            setChosenEndDate(null);
        }
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

    return (
        <ScrollView className="p-4">
            {validationError && <Text className="text-red-700 mb-4">{validationError}</Text>}
            <View className="flex flex-col items-center">
                <Text className="m-4">Podaj datę początkową:</Text>
                <Pressable
                    onPress={() => setShowStartDatePicker(true)}
                    className="w-full border p-3 m-4"
                >
                    <Text>{formatDate(chosenStartDate)}</Text>
                </Pressable>

                {showStartDatePicker && (
                    <DateTimePicker
                        value={chosenStartDate}
                        mode="datetime"
                        onChange={onStartDateChange}
                        minimumDate={new Date()}
                        locale="pl-PL"
                    />
                )}

                <Text className="m-4">Podaj datę końcową (opcjonalnie):</Text>
                <Pressable
                    onPress={() => setShowEndDatePicker(true)}
                    className="w-full border p-3 m-4"
                >
                    <Text>{formatDate(chosenEndDate)}</Text>
                </Pressable>

                {showEndDatePicker && (
                    <DateTimePicker
                        value={chosenEndDate || new Date()}
                        mode="datetime"
                        onChange={onEndDateChange}
                        minimumDate={chosenStartDate}
                        locale="pl-PL"
                    />
                )}

                <Text className="m-4">Wybierz rezerwującego klienta:</Text>
                <Picker
                    selectedValue={selectedClientId}
                    onValueChange={setSelectedClientId}
                    className="w-full m-4"
                >
                    <Picker.Item value="" label="" />
                    {currentClients.map((client) => (
                        <Picker.Item
                            key={client.id}
                            value={client.id}
                            label={client.login}
                        />
                    ))}
                </Picker>

                <Text className="m-4">Wybierz rezerwowany obiekt:</Text>
                <Picker
                    selectedValue={selectedFacilityId}
                    onValueChange={setSelectedFacilityId}
                    className="w-full m-4"
                >
                    <Picker.Item value="" label="Wybierz typ..." />
                    {currentFacilities.map((facility) => (
                        <Picker.Item
                            key={facility.id}
                            value={facility.id}
                            label={`${facility.name}, ${facility.street}
                            ${facility.streetNumber} ${facility.postalCode} ${facility.city}, ${facility.price} zł/godz.`}
                        />
                    ))}
                </Picker>

                <Pressable
                    onPress={handleCreateRentSubmit}
                    className="bg-green-500 w-1/2 p-3 m-4"
                >
                    <Text className="text-center">Stwórz rezerwację</Text>
                </Pressable>

                <Pressable
                    onPress={resetForm}
                    className="bg-gray-500 w-1/2 p-3 m-4"
                >
                    <Text className="text-center text-white">Wyczyść formularz</Text>
                </Pressable>
            </View>
        </ScrollView>
    )
}

export default CreateRentForm;