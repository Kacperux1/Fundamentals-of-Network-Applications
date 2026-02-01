import React, {type ReactNode} from "react";
import { UserContext } from "./UserContext.ts";
import type {Jwt} from "../../../utils/typedefs.ts";
import {decodePayload} from "../../auth/services/AuthService.ts";


export const UserContextProvider = ({children}: {children: ReactNode}) => {

    const [payload, setPayload] = React.useState<Jwt | null>
    (sessionStorage.getItem("jwt") !== null ? decodePayload(sessionStorage.getItem("jwt")!):
    null);

    return (
        <UserContext.Provider value={{payload, setPayload }}>
            {children}
        </UserContext.Provider>
    )
}

