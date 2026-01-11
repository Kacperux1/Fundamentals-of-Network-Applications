
import {createContext} from "react";

export const ClientContext = createContext<ClientContextType | null>(null)
//to do zastanowienia

interface ClientContextType {
    clientId: string | null;
    setClientId: React.Dispatch<React.SetStateAction<string | null>>;
}