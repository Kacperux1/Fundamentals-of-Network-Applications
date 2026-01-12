import "./global.css";
import {Pressable, Text, View, SafeAreaView} from "react-native";
import axios from "axios";
import {useNavigation, useRouter} from "expo-router";

axios.defaults.baseURL = 'http://31.3.218.72:8080';

export default function App() {
    const navigation = useRouter();

    return (
        <SafeAreaView className="flex-1 bg-gray-50">
            <View className="flex-1 justify-center items-center p-6">
                <Text className="text-3xl font-bold text-blue-800 mb-2 text-center">
                    System Rezerwacji
                </Text>
                <Text className="text-lg text-gray-600 mb-10 text-center">
                    Wybierz moduł do zarządzania
                </Text>

                <View className="w-full max-w-md space-y-6">
                    <Pressable
                        onPress={() => navigation.push("/users/UserList")}
                        className="bg-blue-600 p-6 rounded-xl shadow-lg active:bg-blue-700"
                    >
                        <Text className="text-white text-xl font-bold text-center">
                            Użytkownicy
                        </Text>
                        <Text className="text-blue-200 text-center mt-2">
                            Zarządzaj użytkownikami systemu
                        </Text>
                    </Pressable>

                    <Pressable
                        onPress={() => navigation.push('/rents/RentList')}
                        className="bg-green-600 p-6 rounded-xl shadow-lg active:bg-green-700"
                    >
                        <Text className="text-white text-xl font-bold text-center">
                            Wypożyczenia
                        </Text>
                        <Text className="text-green-200 text-center mt-2">
                            Zarządzaj rezerwacjami obiektów
                        </Text>
                    </Pressable>
                </View>

                <Text className="text-gray-400 text-sm mt-16 text-center">
                    System zarządzania rezerwacjami obiektów sportowych
                </Text>
            </View>
        </SafeAreaView>
    );
}