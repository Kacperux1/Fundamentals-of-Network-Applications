import * as react from "react";
import {NavigationContainer} from '@react-navigation/native';

import HomeScreen from "@/app-example/(tabs)";
import RentList from "@/src/screens/rent/RentList";
import {createNativeBottomTabNavigator} from "@react-navigation/bottom-tabs/unstable";
import UserNavigator from "@/app/navigation/UserNavigator";


const Tab = createNativeBottomTabNavigator();


function AppNavigator() {
    return (
        <NavigationContainer>
            <Tab.Navigator initialRouteName="Home">
                <Tab.Screen name="Home"
                component={HomeScreen}/>
                <Tab.Screen name="UsersTab"
                              component={UserNavigator}/>
                <Tab.Screen name="Rents"
                              component={RentList}/>
            </Tab.Navigator>
        </NavigationContainer>
    )
}

export default AppNavigator;