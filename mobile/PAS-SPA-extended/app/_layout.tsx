import { Stack } from "expo-router";
import "./global.css";

export default function RootLayout() {
    return (
        <Stack>
            <Stack.Screen
                name="index"
            />
            <Stack.Screen
                name="users/UsersList"
            />
            <Stack.Screen
                name="users/ClientDetails"
            />
            <Stack.Screen
                name="users/CreateUser"
            />
            <Stack.Screen
                name="rents/RentList"

            />
        </Stack>
    );
}