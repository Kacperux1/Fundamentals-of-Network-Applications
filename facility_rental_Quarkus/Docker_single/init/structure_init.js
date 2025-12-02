const db = db.getMongo().getDB("facility_rental");

    db.users.drop();
    db.facilities.drop();
    db.rents.drop();

db.createCollection("users", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["login", "email", "active"],
            properties: {
                login: {
                    bsonType: "string",
                    description: "must be unique, string type, required"
                },
                email: {
                    bsonType: "string",
                    pattern: "^[\\w\\.]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$",
                    description: "must be unique, string type, required, matches pattern *@*"
                },
                active: {
                    bsonType: "bool",
                    description: "must be boolean"
                }
            }
        }
    }
});

db.users.createIndex({ login: 1}, { unique: true });
db.users.createIndex({ email: 1}, { unique: true });



db.createCollection("facilities", {
    validator: {
        $jsonSchema: {
            bsonType: "object",
            required: ["name", "street", "street_number", "city", "postal_code", "base_price"],
            properties: {
                name: { bsonType: "string", description: "string type, required" },
                street: { bsonType: "string", description: "string type, required" },
                street_number: { bsonType: "string", description: "string type, required" },
                city: { bsonType: "string", description: "string type, required" },
                postal_code: {
                    bsonType: "string",
                    pattern: "^[0-9]{2}-[0-9]{3}$",
                    description: "string type, required, format XX-XXX"
                },
                base_price: { bsonType: "decimal", description: "fixed-point number, required" }
            }
        }
    }
});
