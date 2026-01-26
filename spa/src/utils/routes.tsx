    import App from "../App.tsx";
    import Users from "../components/users/Users.tsx";
    import Rents from "../components/rents/Rents.tsx";
    import ClientDetails from "../components/users/ClientDetails.tsx";
    import { createBrowserRouter } from "react-router-dom";
    import CreateRentForm from "../components/rents/CreateRentForm.tsx";
    import CreateUserForm from "../components/users/CreateUserForm.tsx";
    import LoginForm from "../components/auth/LoginForm.tsx";
    import {UserContextProvider} from "../components/users/context/UserContextProvider.tsx";
    import Facilities from "../components/facilities/Facilities.tsx";
    import CreateFacilityForm from "../components/facilities/CreateFacilityForm.tsx";
    import ClientOwnDetails from "../components/users/ClientOwnDetails.tsx";

    export const  router = createBrowserRouter([
        {
            path: '/',
            element: <UserContextProvider><App/></UserContextProvider>,
            children: [
                {
                    path: 'usersView',
                    element: <Users/>,
                    children: [
                        {
                            path: 'createUser',
                            element: <CreateUserForm/>
                        },
                        {
                            path: 'updateUser/:userId?',
                            element: <CreateUserForm/>
                        }
                    ]
                },
                {
                    path: 'rentsView',
                    element: <Rents/>,
                    children: [
                        {
                            path: 'createRent',
                            element: <CreateRentForm/>
                        }
                    ]
                },
                {
                    //jak dałem /clients/clientId to router dostał głupawki
                    path: ':clientId',
                    element: <ClientDetails/>
                },
                {
                    path: 'login',
                    element: <LoginForm/>
                },
                {
                    path: 'facilitiesView',
                    element: <Facilities/>,
                    children: [
                        {
                            path: 'createFacility',
                            element: <CreateFacilityForm/>
                        }
                    ]
                },
                {
                    path: 'myAccount',
                    element: <ClientOwnDetails/>
                }
            ]
        }
    ]);