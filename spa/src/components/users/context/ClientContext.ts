
import {createContext} from "react";


export type ClientManagementMode = 'create' | 'update'

interface ClientContextType {
    clientMgmntMode: ClientManagementMode,
    setClientMgmntMode: React.Dispatch<React.SetStateAction<ClientManagementMode>>;
}


export const ClientContext = createContext<ClientContextType>({
    clientMgmntMode: "create",
    setClientMgmntMode: () => {
        throw new Error("setMode used outside ClientContext.Provider");
    },
})
//to do zastanowienia

