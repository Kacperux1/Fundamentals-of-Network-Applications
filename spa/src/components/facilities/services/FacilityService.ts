import axios, {AxiosError} from 'axios';
import type {FacilityForm} from "../../../utils/typedefs.ts";

async function getAllFacilities() {

    const res = await axios.get('/facilities');
    return res.data;
}

export async function createFacility(facilityForm: FacilityForm) {
    try {
        const res = await axios.post('/facilities', facilityForm);
        return res.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            const status = error.response?.status;

            if (status === 403) {
                alert("Brak uprawnień do wykonania tej operacji");
            } else {
                alert(error.response?.data?.message ?? "Błąd serwera");
            }
        }

    }

}

export default  getAllFacilities;