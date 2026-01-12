import * as React from 'react';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import CreateUser from "@/src/screens/user/CreateUser";
import ClientDetails from "@/src/screens/user/ClientDetails";
import UserList from "@/src/screens/user/UserList";


const UserNavigatorStack = createNativeStackNavigator();

function UserNavigator(){
    return (
        <UserNavigatorStack.Navigator>
            <UserNavigatorStack.Screen name="UserList" component={UserList} />
            <UserNavigatorStack.Screen name="CreateUser" component={CreateUser} />
            <UserNavigatorStack.Screen name="ClientDetails" component={ClientDetails}  />
        </UserNavigatorStack.Navigator>
    )
}

export default UserNavigator;