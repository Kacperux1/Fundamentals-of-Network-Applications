import {useEffect, useState} from 'react'
import { NavLink, Outlet} from "react-router-dom";
import './App.css'
import axios from 'axios';



axios.defaults.baseURL = 'http://localhost:8080';



function App() {


    return (
    <>
      <h1 className = "flex justify-center">Boiskownia</h1>
        <div className= "flex m-4">
            <NavLink to="/users">
                <button className =" m-4">
                    Kliknij mnie :(
                </button>
            </NavLink>
            <NavLink to ="/rents">
                <button className =" m-4">
                    a mnie to nie klikaj :P
                </button>
            </NavLink>

        </div>
        <Outlet/>
    </>
  )
}

export default App
