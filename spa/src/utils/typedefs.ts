

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

export interface Facility {
    id: string;
    name: string;
    streetNumber: string;
    street: string;
    city: string;
    postalCode: string;
    price: number;
}

export interface Client extends User {
    first_name: string;
    last_name: string;
    phone: string;
}

export interface RentForm {
    clientId: string;
    facilityId: string;
    startDate: string;
    endDate: string | null;
}

export interface CreateUserFormData {
    login: string;
    email: string;
    type: string;
    active: boolean;
}

export interface CreateClientData extends CreateUserFormData {
    first_name: string;
    last_name: string;
    phone: string;
}