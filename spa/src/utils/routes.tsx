import App from "../App.tsx";
import Users from "../components/users/Users.tsx";
import Rents from "../components/rents/Rents.tsx";
import UserDetails from "../components/users/UserDetails.tsx";
import { createBrowserRouter } from "react-router-dom";
import CreateRentForm from "../components/rents/CreateRentForm.tsx";



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
                        path: '/usersView/:userId',
                        element: <UserDetails/>,
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

        ]
    }
]);