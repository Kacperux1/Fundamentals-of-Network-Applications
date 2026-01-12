import React, {useEffect, useState} from 'react';
import {Alert, Pressable, Text, TextInput, View, ScrollView} from 'react-native';
import type {
    CreateClientData,
    CreateUserFormData,
    UpdateClientData,
    UpdateUserFormData,
    User
} from "../../src/utils/typedefs.ts";

import * as yup from 'yup';
import {createUser, getUserById, updateUser} from "@/src/api/user/UserService";
import {Picker} from "@react-native-picker/picker";
import {RadioButton} from "react-native-paper";

function CreateUserForm() {
    const {userId} = useParams<{ userId: string | undefined }>();

    const updateMode: boolean = Boolean(userId);
    const [typedLogin, setTypedLogin] = useState<string>('');
    const [typedEmail, setTypedEmail] = useState<string>('');
    const [chosenType, setChosenType] = useState<string>('');
    const [willBeActive, setWillBeActive] = useState<boolean | null>(null);
    const [clientFirstName, setClientFirstName] = useState<string>('');
    const [clientLastName, setClientLastName] = useState<string>('');
    const [clientPhone, setClientPhone] = useState<string>('');
    const [validationErrorMessage, setValidationErrorMessage] = useState<string>('');
    const [updatedUser, setUpdatedUser] = useState<User | null>(null);

    const createFormValidationSchema = yup.object(({
        login: yup.string().required("Login jest wymagany").max(50, "Login jest zbyt długi (max 50 znaków"),
        email: yup.string().email("Niewłaściwy format emaila").required("Email jet wymagany")
            .max(50, "Email jest zbyt długi (max 50 znaków"),
        active: yup.boolean().required("wymagane określenie stanu konta użytkownika"),
        type: yup.string().required("Wymagane podanie typu konta użytkownika"),
    }));

    const createClientValidationSchema = yup.object(({
        first_name: yup.string().required("Imię jest wymagane!")
            .matches(/^[a-zA-Z]+$/, "Imię może się skłądać tylko z liter!"),
        last_name: yup.string().required(("Nazwisko jet wymagane!"))
            .matches(/^[a-zA-Z]+$/, "Nazwisko może się skłądać tylko z liter!"),
        phone: yup.string().length(9, "Niepoprawna długość nr. telefonu").required("Telefon jest wymagany")
            .matches(/^[0-9]+$/, "Nr telefonu może się składać tylko z cyfr")
    }));

    const updateUserValidationSchema = yup.object(({
        login: yup.string().max(50, "Login jest zbyt długi (max 50 znaków"),
        email: yup.string().email("Niewłaściwy format emaila")
            .max(50, "Email jest zbyt długi (max 50 znaków"),
    }));

    const updateClientValidationSchema = yup.object(({
        first_name: yup.string()
            .matches(/^[a-zA-Z]+$/, "Imię może się skłądać tylko z liter!"),
        last_name: yup.string()
            .matches(/^[a-zA-Z]+$/, "Nazwisko może się skłądać tylko z liter!"),
        phone: yup.string().length(9, "Niepoprawna długość nr. telefonu")
            .matches(/^[0-9]+$/, "Nr telefonu może się składać tylko z cyfr")
    }));

    function sendDataToCreateUser(userData: CreateUserFormData) {
        Alert.alert('Na pewno?',`Na pewno chcesz dodać użytkownika o loginie ${userData.login}, emailu: ${userData.email}, statusie:`
            + (userData.active ? `aktywnym` : `nieaktywnym`) + 'roli: ' +
            (userData.type === 'client' ? 'klient' : userData.type === 'administrator' ? 'administrator' : 'pracownik') + ' ?', [
            {text: "Tak", onPress: () => {createUser(userData).then((user: User) => {
                    Alert.alert('Sukces!',`Użytkownik o loginie ${user.login} został dodany do systemu.`);
                });}},
            {text: 'Nie', onPress: () => {}},
        ]);
    }

    function getUpdatedUserInfo(userId: string | undefined) {
        if (userId === undefined) {
            return;
        }
        getUserById(userId).then((user: User) => {
            setUpdatedUser(user);
        })
    }

    useEffect(() => {
        getUpdatedUserInfo(userId);
    }, [userId])

    function sendDataToUpdateUser(userData: UpdateUserFormData) {
        Alert.alert('Na pewno?',`Na pewno chcesz dodać użytkownika o bieżącym loginie ${userData.login} ?`, [
            {text: "Tak", onPress: () => {if (userId) {
                    updateUser(userId, userData).then((user: User) => {
                        Alert.alert('Sukces', `zaktualizowano użytkownika o ID ${user.id}`);
                    })
                }}},
            {text: 'Nie', onPress: () => {}},
        ]);
    }

    async function handleCreationSubmit() {
        setValidationErrorMessage('');
        let userData: CreateUserFormData = {
            login: typedLogin,
            email: typedEmail,
            active: willBeActive,
            type: chosenType
        }
        if (chosenType === "client") {
            userData = {
                login: typedLogin,
                email: typedEmail,
                active: willBeActive,
                type: chosenType,
                first_name: clientFirstName,
                last_name: clientLastName,
                phone: clientPhone
            } as CreateClientData;

        }

        try {
            if (chosenType === "client") {
                await createClientValidationSchema.validate(userData, {abortEarly: true});
            } else {
                await createFormValidationSchema.validate(userData, {abortEarly: true});
            }
        } catch (error: any) {
            setValidationErrorMessage(error.message);
            return;
        }
        sendDataToCreateUser(userData);
    }

    async function handleUpdateSubmit() {
        if (!typedLogin && !typedEmail && !clientFirstName && !clientLastName && !clientPhone) {
            Alert.alert('Błont',"Wpisz jakieś dane do aktualizacji!");
            return;
        }
        setValidationErrorMessage('');
        let userData: UpdateUserFormData = {
            login: typedLogin === '' ? null : typedLogin,
            email: typedEmail === '' ? null : typedEmail,
        }
        if (updatedUser?.type === "client") {
            userData = {
                login: typedLogin === '' ? null : typedLogin,
                email: typedEmail === '' ? null : typedEmail,
                first_name: clientFirstName === '' ? null : clientFirstName,
                last_name: clientLastName === '' ? null : clientLastName,
                phone: clientPhone === '' ? null : clientPhone,
            } as UpdateClientData;
        }
        try {
            if (updatedUser?.type === "client") {
                await updateClientValidationSchema.validate(userData, {abortEarly: true});
            } else {
                await updateUserValidationSchema.validate(userData, {abortEarly: true});
            }
        } catch (error: any) {
            setValidationErrorMessage(error.message);
            return;
        }
        sendDataToUpdateUser(userData);
    }

    function resetForm() {
        setTypedLogin('');
        setTypedEmail('');
        setWillBeActive(null);
        setClientPhone('');
        setClientFirstName('');
        setClientLastName('');
        setChosenType('');
    }

    return (
        <ScrollView>
            <View className="flex flex-col items-center mx-auto w-1/2">
                {updateMode && <Text>Aktualizacja użytkownika o loginie {updatedUser?.login}</Text>
                }
                {validationErrorMessage !== '' &&
                    <Text>{validationErrorMessage}</Text>}
                <View>
                    <View>
                        <Text>Podaj nazwę(login) użytkownika:</Text>
                        <TextInput onChangeText={setTypedLogin}
                                   className="w-full"/>
                    </View>
                    <View>
                        <Text> Podaj adres email:</Text>
                        <TextInput onChangeText={setTypedEmail}
                                   className="w-full"/>
                    </View>
                    {!updateMode &&
                        <View>
                            <Text>Wybierz początkowy stan konta:</Text>
                            <RadioButton.Group onValueChange={newValue => setWillBeActive(newValue ==='active')}
                                               value={willBeActive === true ? 'active' : 'inactive'}>
                                <View style={{flexDirection: 'row', alignItems: 'center'}}>
                                    <RadioButton value="active" />
                                    <Text>Aktywny</Text>
                                </View>
                                <View style={{flexDirection: 'row', alignItems: 'center'}}>
                                    <RadioButton value="inactive" />
                                    <Text>Nieaktywny</Text>
                                </View>
                            </RadioButton.Group>
                        </View>
                    }

                    {!updateMode &&
                        <View>
                            <Text>Wybierz typ konta użytkownika:</Text>
                            <Picker onValueChange={setChosenType}>
                                <Picker.Item value=""  label="" />
                                <Picker.Item value="client"  label="Klient" />
                                <Picker.Item value="resourceMgr" label="Pracownik"/>
                                <Picker.Item value="administrator" label="Administrator"/>
                            </Picker>
                        </View>
                    }

                    {(chosenType === 'client' || updatedUser?.type === 'client') &&
                        <View className="w-full flex flex-col  m-4">
                            <View>
                                <Text>Podaj imię klienta:</Text>
                                <TextInput onChangeText={setClientFirstName} />
                            </View>
                            <View>
                                <Text>Podaj nazwisko klienta:</Text>
                                <TextInput onChangeText={setClientLastName} />
                            </View>
                            <View>
                                <Text>Podaj numer telefona klienta:</Text>
                                <TextInput onChangeText={setClientPhone} />
                            </View>
                        </View>
                    }

                    <Pressable onPress={() => {
                        if (updateMode) {
                            handleUpdateSubmit();
                        } else {
                            handleCreationSubmit();
                        }
                    }} >
                        <Text>{updateMode ? "zaktualizuj użytkownika" : "Dodaj użytkownika"}</Text>
                    </Pressable>
                    <Pressable onPress={() => resetForm()}>
                        <Text>Wyczyść formularz</Text>
                    </Pressable>
                </View>
            </View>
        </ScrollView>
    )
}

export default CreateUserForm;