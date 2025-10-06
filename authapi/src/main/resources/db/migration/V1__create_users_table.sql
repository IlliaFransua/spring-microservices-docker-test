CREATE TABLE users (
    id UUID PRIMARY KEY,
    
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(60) NOT NULL
);
