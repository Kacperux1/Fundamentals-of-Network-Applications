

export interface User {
    id: string;
    login: string;
    email: string;
    active: boolean;
    type: string;
}


export interface Rent {
    rentId: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    facilityName: string;
    streetNumber: string;
    street: string;
    city: string;
    startDate: Date;
    endDate: Date;
    totalPrice: number;
}