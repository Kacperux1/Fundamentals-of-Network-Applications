    import "./global.css";
    import {Pressable, Text, View} from "react-native";
    import axios from "axios";
    import {useNavigation, useRouter} from "expo-router";


    axios.defaults.baseURL = 'http://localhost:8080';



    export default function App() {

        const navigation = useRouter();
        return (
            <View>
                <Pressable onPress={() => navigation.push("/users/UserList")}>
                <Text>Użytkownicy</Text>
            </Pressable><Pressable onPress={() => navigation.push('/rents/RentList')}>
                <Text>Wypożyczenia</Text>
            </Pressable>
           </View>
        );
    }