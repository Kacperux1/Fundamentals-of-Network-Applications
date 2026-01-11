import App from "../App.tsx";
import Users from "../components/users/Users.tsx";
import Rents from "../components/rents/Rents.tsx";
import ClientDetails from "../components/users/ClientDetails.tsx";
import { createBrowserRouter } from "react-router-dom";
import CreateRentForm from "../components/rents/CreateRentForm.tsx";

export const  router = createBrowserRouter([
    {
        path: '/',
        element: <App/>,
        children: [
            {
                path: '/usersView',
                element: <Users/>
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
                path: '/:clientId',
                element: <ClientDetails/>
            }
        ]
    }
]);