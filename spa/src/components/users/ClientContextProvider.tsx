import { useState, type ReactNode } from "react";
import { ClientContext } from "./ClientContext";



export function ClientContextProvider({children}: {children: ReactNode}) {
    const [clientId, setClientId] = useState<string|null>(null);
    return(
        <ClientContext.Provider value = {{clientId, setClientId}}>
            {children}
        </ClientContext.Provider>
    )
}