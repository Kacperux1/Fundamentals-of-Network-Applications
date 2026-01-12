import "./global.css";
import { Text, View } from "react-native";
import { verifyInstallation } from "nativewind";

console.log("NativeWind config:", verifyInstallation());

export default function App() {
    return (
        <View className="flex-1 items-center justify-center bg-white">
            <Text className="text-3xl font-bold text-red-600">
                Welcome to Nativewind!
            </Text>
        </View>
    );
}