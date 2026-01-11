import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import {RouterProvider} from "react-router-dom";
import {router} from "./utils/routes.tsx";
import {ClientContextProvider} from "./components/users/ClientContextProvider.tsx";

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <ClientContextProvider>
          <RouterProvider router={router}/>
      </ClientContextProvider>
  </StrictMode>,
)
