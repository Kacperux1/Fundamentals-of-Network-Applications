import {useState, useEffect} from 'react';
import {getAllUsers, activateUser, deactivateUser, getUserById} from '../../src/api/user/UserService';
import {Alert, FlatList, Pressable, Text, TextInput, View, ScrollView} from 'react-native';
import {User} from "@/src/utils/typedefs";
import {Link, useRouter} from "expo-router";

function UserList() {
    const [currentUsers, setCurrentUsers] = useState<User[]>([]);
    const [searchLogin, setSearchLogin] = useState('');
    const router = useRouter();

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

    const renderListItem = ({item}: {item: User}) => (
        <View className="m-2 rounded-xl border-2 border-gray-300 p-4">
            <Text className="text-lg">
                {item.login}, {item.email}, {item.type === "client" ? "klient" : item.type === "resourceMgr" ?
                "pracownik" : "administrator"},
                {item.active ? " aktywny" : " nieaktywny"}
            </Text>

            <View className="flex flex-row gap-3 justify-center mt-2">
                {item.active ?
                    <Pressable
                        className="bg-red-500 p-2 rounded"
                        onPress={() => deactivateChosenUser(item.id)}
                    >
                        <Text className="text-white">Deaktywuj</Text>
                    </Pressable>
                    :
                    <Pressable
                        className="bg-green-500 p-2 rounded"
                        onPress={() => activateChosenUser(item.id)}
                    >
                        <Text className="text-white">Aktywuj</Text>
                    </Pressable>
                }

                {item.type === "client" && (
                    <Pressable
                        className="bg-blue-500 p-2 rounded"
                        onPress={() => router.push(`/users/${item.id}`)}
                    >
                        <Text className="text-white">Szczegóły</Text>
                    </Pressable>
                )}

                <Pressable
                    className="bg-yellow-500 p-2 rounded"
                    onPress={() => router.push(`/users/CreateUser?userId=${item.id}`)}
                >
                    <Text className="text-white">Modyfikuj</Text>
                </Pressable>
            </View>
        </View>
    );

    return (
        <ScrollView className="p-4">
            <Text className="text-2xl font-bold mb-4">Lista użytkowników</Text>

            <TextInput
                placeholder="Szukaj po loginie..."
                value={searchLogin}
                onChangeText={setSearchLogin}
                className="border border-gray-300 p-2 rounded mb-4"
            />

            <FlatList
                data={filteredUsers}
                keyExtractor={(item) => item.id}
                renderItem={renderListItem}
                scrollEnabled={false}
                ListEmptyComponent={
                    <Text className="text-center mt-4">Brak użytkowników do wyświetlenia</Text>
                }
            />


            <Pressable
                className="bg-blue-500 p-3 rounded mt-4"
                onPress={() => router.push('/users/CreateUser')}
            >
                <Text className="text-white text-center">Dodaj nowego użytkownika</Text>
            </Pressable>
        </ScrollView>
    )
}

export default UserList;