import axios, {AxiosError} from "axios";

export default async function login(username: string, password: string) {
    try {
        const response = await axios.post("http://localhost:8080/auth/login",
            { login: username, rawPassword: password });
        console.log("Login response:", response.data);
        return response.data;
    } catch (err: unknown) {
            const error = err as AxiosError;
            alert(error.response?.data);
    }

}

export function decodePayload(token: string) {
    const payload = token.split('.')[1];
    const decoded =  JSON.parse(atob(
        payload.replace(/-/g, '+').replace(/_/g, '/')));

        decoded.roles = decoded.roles.map((role: {authority:string}) => role.authority.split("_")[1]);
    console.log(decoded)
    return decoded;
}

