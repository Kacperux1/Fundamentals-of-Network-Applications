import axios from 'axios';
import {useEffect, useState} from 'react'


function RentList() {
    const [rentList, setRentList] = useState<Rent[]>([])

    interface Rent {
        rentId: string;
        totalPrice: number;
        firstName: string;
        lastName: string;
        email: string;
        phoneNumber: string;
        facilityName: string;
        streetNumber: string;
        street: string;
        city: string;
        postalCode: string;
        startDate: Date;
        endDate: Date;
    }

    async function getRents() {
        try {
            const response = await axios.get("/rents");
            return response.data;
        } catch(err) {
            alert(err.response.data.message)
        }
    }

    useEffect(() => {getRents().then(r => setRentList(r))}, []);

    return (
        <>
            <ul id = "rentList">
                {rentList.map((rent) => (
                    <li key={rent.rentId}>
                        {rent.rentId}, {rent.firstName}, {rent.lastName}, {rent.facilityName}, {new Date(rent.startDate).toLocaleString()} ;
                         {new Date(rent.endDate).toLocaleString()}, total price: {rent.totalPrice}
                    </li>
                ))}
            </ul>
        </>
    )
}


export default RentList;



