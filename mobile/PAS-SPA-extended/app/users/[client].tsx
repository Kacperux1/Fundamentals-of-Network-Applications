import {useState, useEffect} from 'react';
import type {Client, Rent} from '../../src/utils/typedefs.ts';
import {getClientsRents} from "@/src/api/rent/RentService";
import {getUserById} from "@/src/api/user/UserService";
import {Text, View, FlatList, ActivityIndicator} from 'react-native';
import {useLocalSearchParams} from "expo-router";

function ClientDetails() {
    const [client, setClient] = useState<Client | null>(null);
    const [clientsRents, setClientsRents] = useState<Rent[]>([]);
    const [loading, setLoading] = useState(true);


    const { client: clientId } = useLocalSearchParams<{ client?: string }>();

    console.log("render ClientDetails, clientId:", clientId);

    // Funkcje pobierania danych
    const getClientInfo = async (id: string) => {
        try {
            const clientData = await getUserById(id);
            setClient(clientData);
        } catch (error) {
            console.error("Błąd pobierania klienta:", error);
        }
    };

    const updateClientsRents = async (id: string) => {
        try {
            console.log("Pobieranie wypożyczeń dla ID:", id);
            const rents = await getClientsRents(id);
            console.log("Wypożyczenia z API:", rents);
            setClientsRents(rents);
        } catch (error) {
            console.error("Błąd pobierania wypożyczeń:", error);
        }
    };

    useEffect(() => {
        // clientId może być string lub string[] - konwertujemy na string
        const actualClientId = typeof clientId === 'string'
            ? clientId
            : Array.isArray(clientId)
                ? clientId[0]
                : undefined;

        if (!actualClientId) {
            console.log("Brak clientId");
            return;
        }

        console.log("Używam clientId:", actualClientId);


        setLoading(true);
        setClient(null);
        setClientsRents([]);


        const fetchData = async () => {
            try {
                await Promise.all([
                    getClientInfo(actualClientId),
                    updateClientsRents(actualClientId)
                ]);
            } catch (error) {
                console.error("Błąd ładowania danych:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [clientId]);


    if (loading) {
        return (
            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                <ActivityIndicator size="large" />
                <Text>Ładowanie danych...</Text>
            </View>
        );
    }


    if (!clientId) {
        return (
            <View>
                <Text>Brak identyfikatora klienta</Text>
            </View>
        );
    }

    // Jeśli brak danych klienta
    if (!client) {
        return (
            <View>
                <Text>Nie znaleziono danych klienta</Text>
            </View>
        );
    }

    return (
        <View style={{ padding: 16 }}>
            <Text style={{ fontSize: 24, fontWeight: 'bold', marginBottom: 16 }}>
                Szczegóły klienta:
            </Text>

            <View style={{ marginBottom: 20 }}>
                <Text>Identyfikator: {client.id}</Text>
                <Text>Imię: {client.first_name || 'Nie podano'}</Text>
                <Text>Nazwisko: {client.last_name || 'Nie podano'}</Text>
                <Text>Login: {client.login}</Text>
                <Text>Email: {client.email}</Text>
                <Text>Telefon: {client.phone || 'Nie podano'}</Text>
                <Text>Stan: {client.active ? "aktywny" : "nieaktywny"}</Text>
            </View>

            <Text style={{ fontSize: 20, fontWeight: 'bold', marginBottom: 12 }}>
                Wypożyczenia klienta:
            </Text>

            {clientsRents.length === 0 ? (
                <Text>Brak wypożyczeń</Text>
            ) : (
                <FlatList
                    data={clientsRents}
                    keyExtractor={(item) => item.rentId}
                    renderItem={({ item }) => (
                        <View style={{
                            marginVertical: 8,
                            padding: 12,
                            borderWidth: 1,
                            borderRadius: 8,
                            borderColor: '#ddd'
                        }}>
                            <Text>ID: {item.rentId}</Text>
                            <Text>Obiekt: {item.facilityName}</Text>
                            <Text>Data rozpoczęcia: {new Date(item.startDate).toLocaleString()}</Text>
                            <Text>
                                Data zakończenia: {item.endDate
                                ? new Date(item.endDate).toLocaleString()
                                : 'Nie zakończono'
                            }
                            </Text>
                            <Text>Cena: {item.totalPrice || '0'} zł</Text>
                        </View>
                    )}
                />
            )}
        </View>
    );
}

export default ClientDetails;