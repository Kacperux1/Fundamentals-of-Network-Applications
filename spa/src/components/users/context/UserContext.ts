import React, {createContext} from "react";
import type {Jwt} from "../../../utils/typedefs.ts";




export const UserContext = createContext<{
    payload: Jwt | null;
    setPayload: React.Dispatch<React.SetStateAction<Jwt | null>>;

} | null>(null);