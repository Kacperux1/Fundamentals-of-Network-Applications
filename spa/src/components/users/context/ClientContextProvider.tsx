import { useState, type ReactNode } from "react";
import { ClientContext } from "./ClientContext.ts";
import type {ClientManagementMode} from "./ClientContext.ts";


export function ClientContextProvider({children}: {children: ReactNode}) {
    const [clientMgmntMode,setClientMgmntMode] = useState<ClientManagementMode>('create');
    return(
        <ClientContext.Provider value = {{clientMgmntMode, setClientMgmntMode}}>
            {children}
        </ClientContext.Provider>
    )
}