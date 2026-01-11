import App from "../App.tsx";
import Users from "../components/users/Users.tsx";
import Rents from "../components/rents/Rents.tsx";
import ClientDetails from "../components/users/ClientDetails.tsx";
import { createBrowserRouter } from "react-router-dom";
import CreateRentForm from "../components/rents/CreateRentForm.tsx";
import CreateUserForm from "../components/users/CreateUserForm.tsx";

export const  router = createBrowserRouter([
    {
        path: '/',
        element: <App/>,
        children: [
            {
                path: '/usersView',
                element: <Users/>,
                children: [
                    {
                        path: '/usersView/createUser',
                        element: <CreateUserForm/>
                    }
                ]
            },
            {
                path: '/rentsView',
                element: <Rents/>,
                children: [
                    {
                        path: '/rentsView/createRent',
                        element: <CreateRentForm/>
                    }
                ]
            },
            {
                //jak dałem /clients/clientId to router dostał głupawki
                path: '/:clientId',
                element: <ClientDetails/>
            }
        ]
    }
]);