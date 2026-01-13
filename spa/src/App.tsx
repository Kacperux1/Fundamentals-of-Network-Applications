
import { NavLink, Outlet} from "react-router-dom";
import './App.css'
import axios from 'axios';



axios.defaults.baseURL = 'http://31.3.218.72:8080';



function App() {


    return (
    <>
      <h1 className = "flex justify-center">Boiskownia</h1>
        <div className= "flex m-4 justify-center">
            <NavLink to="/usersView">
                <button className =" m-4">
                    Lista użytkowników
                </button>
            </NavLink>
            <NavLink to ="/rentsView">
                <button className =" m-4">
                    Lista wypożyczeń
                </button>
            </NavLink>
            <NavLink to ="/">
                <button className =" m-4">
                    Strona główna
                </button>
            </NavLink>
        </div>
        <Outlet/>
    </>
  )
}

export default App
