import {useState, useEffect} from 'react';
import {getAllUsers, activateUser, deactivateUser, getUserById} from '../../api/user/UserService';
import {Alert, Animated, FlatList, Pressable, Text, TextInput, View} from 'react-native';

import {User} from "@/src/utils/typedefs";

import {useNavigation} from "expo-router";


function UserList() {

    const [currentUsers, setCurrentUsers] = useState<User[]>([]);

    const [searchLogin, setSearchLogin] = useState('');

    //const {clientMgmntMode, setClientMgmntMode} = useContext(ClientContext);

    const navigation = useNavigation();

    const filteredUsers = currentUsers.filter(user =>
        user.login.toLowerCase().includes(searchLogin.toLowerCase())
    );


    function updateUserList() {
        getAllUsers().then((users: User[]) => {
            setCurrentUsers(users);
        })
    }

    useEffect(() => {
        updateUserList();
    }, [])

    function activateChosenUser(id: string) {
        const chosenUser = currentUsers.find(user => user.id === id);
        Alert.alert('Na pewno?', `Na pewno chcesz aktywować użytkownika ${chosenUser?.login} ?`, [
            {text: "Tak", onPress: () => {activateUser(id).then(() => {
                    updateUserList();
                    getUserById(id).then((user:User) => {
                        Alert.alert('Sukces',`Aktywowano użytkownika: ${user.login}`);

                    })
                });}},
            {text: 'Nie', },
        ]);
    }
    //do zmiany lub nie wiem zeby nei pobierac z api
    function deactivateChosenUser(id: string) {
        const chosenUser = currentUsers.find(user => user.id === id);
        Alert.alert('Na pewno?', `Na pewno chcesz deaktywować użytkownika ${chosenUser?.login} ?`, [
            {text: "Tak", onPress: () => {deactivateUser(id).then(() => {
                    updateUserList();
                    getUserById(id).then((user:User) => {
                        Alert.alert('Sukces', `Deaktywowano użytkownika: ${user.login}`);
                    })
                })}},
            {text: 'Nie', },
        ]);
    }

    const renderListItem = (user: {item: User}) => (
        <View  className=" m-2 rounded-xl border-2 border-yellow-600 text-lg h-25 p-4">
            <Text className="text-lg">
                {user.item.login}, {user.item.email}, {user.item.type === "client" ? "klient" : user.item.type === "resourceMgr" ?
                "pracownik" : "administrator"},
                {user.item.active ? " aktywny" : " nieaktywny"}
            </Text>

            <View className="flex gap-3 justify-center">
                {user.item.active ? <Pressable className="bg-red" onPress={() => {
                        deactivateChosenUser(user.item.id);}}>
                        <Text>Deaktywuj</Text>
                </Pressable>
                    : <Pressable className="bg-green"
                              onPress={() => activateChosenUser(user.item.id)}>
                        <Text>Aktywuj</Text>
                    </Pressable>}
                {user.item.type == "client"  && (
                        <Pressable onPress ={ () => navigation.navigate('ClientDetails', {userId: user.item.id})}>
                           <Text>Szczegóły</Text>
                        </Pressable>
                    )}
                    <Pressable onPress ={ () => navigation.navigate('CreateUser', {userId: user.item.id})}>
                        Modyfikuj
                    </Pressable>
            </View>
        </View>
    );


    return (
        <View>
            <Text>Lista użytkowników</Text>

            <TextInput
                placeholder="Szukaj po loginie..."
                value={searchLogin}
                onChangeText={setSearchLogin}
                className="border p-2 rounded w-full mb-4"
            />

            <FlatList
                data={filteredUsers}
                keyExtractor={(user) => user.id}
                renderItem={renderListItem}
            </FlatList>

            {//filteredUsers.length === 0 && (
               // <Text className="text-gray-500">Brak użytkowników spełniających kryteria</Text>)
                 }
                <Pressable onPress = {() => {navigation.navigate('CreateUser')}}>
                    <Text>Dodaj nowego użytkownika</Text></Pressable>
        </View>
    )
}

export default UserList;