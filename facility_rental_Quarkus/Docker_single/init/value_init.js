

db.users.insertMany([
    {
        login: "admin",
        email: "admin@example.com",
        active: true,
        _class: "administrator"
    },
    {
        login: "manager",
        email: "manager@example.pl",
        active: true,
        _class: "resourceMgr"
    },
    {
        login: "stachudzons",
        email: "stasiu@piwo.pl",
        active: false,
        _class: "client",
        first_name: "Stanisław",
        last_name: "Dżons",
        phone: "123456789"
    },
    {
        login: "j_bwicz",
        email: "janek@2ws.pl",
        active: true,
        _class: "client",
        first_name: "Janusz",
        last_name: "Brzęszczęszczykiewicz",
        phone: "123456789"
    }
    ]
);

db.facilities.insertMany([
    {
        name: "hala w Zatoce Sportu",
        street_number: "10",
        street: "Aleje Politechniki",
        city: "Łódź",
        postal_code: "93-590",
        base_price: NumberDecimal("200.00")
    },
    {
        name: "kort squashowy w Zatoce Sportu",
        street_number: "10",
        street: "Aleje Politechniki",
        city: "Łódź",
        postal_code: "93-590",
        base_price: NumberDecimal("100.00")
    },
    {
        name: "Boisko przy Centrum Sportu",
        street_number: "11",
        street: "Aleje Politechniki",
        city: "Łódź",
        postal_code: "93-590",
        base_price: NumberDecimal("50.00")
    }
]);

db.rents.insertOne(
    {
        client: {
            login: "stachudzons",
            email: "stasiu@piwo.pl",
            active: false,
            _class: "client",
            first_name: "Stanisław",
            last_name: "Dżons",
            phone: "123456789"
        },
        facility: {
            name: "Boisko przy Centrum Sportu",
            street_number: "11",
            street: "Aleje Politechniki",
            city: "Łódź",
            postal_code: "93-590",
            base_price: NumberDecimal("50.00")
        },
        start_date:ISODate("2025-11-16T15:30:00Z"),
        end_date:ISODate("2025-11-16T17:30:00Z"),
        total_price: NumberDecimal("100.00")
    }
);