

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
    type: string| null;
    active: boolean |null;
}

export interface CreateClientData extends CreateUserFormData {
    first_name: string;
    last_name: string;
    phone: string;
}

export interface UpdateUserFormData {
    email: string |null;
    active: boolean |null;
}

export interface UpdateClientData extends UpdateUserFormData {
    first_name: string |null;
    last_name: string |null;
    phone: string |null;
}


export interface Jwt {
        sub: string;
        roles: string[]
        exp: number;
}

export interface FacilityForm {
    name: string;
    streetNumber: string;
    street: string;
    city: string;
    postalCode: string;
    basePrice: number;
}

export interface changePasswordForm {
    login: string | undefined;
    password: string;
    newPassword: string;
}


export interface RentHateOas {
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
    links: HateOasLink[]
}

interface HateOasLink {
    rel: string;
    href: string;
}



export interface UserEtag {
    id: string;
    login: string;
    email: string;
    active: boolean;
    type: string;
    etag: string;
}



export interface ClientEtag extends UserEtag {
    first_name: string;
    last_name: string;
    phone: string;
}


export interface ClientOwnUpdateData {
    first_name: string|null;
    last_name: string|null;
    email: string|null;
    phone: string|null;
}



