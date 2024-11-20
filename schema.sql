-- Create database
CREATE DATABASE bank_management_system;

-- Use the database
USE bank_management_system;

-- Table: signup
-- Stores user registration details
CREATE TABLE signup (
    form_no VARCHAR(20) PRIMARY KEY,         -- Unique form number for each user
    name VARCHAR(100) NOT NULL,              -- User's name
    father_name VARCHAR(100) NOT NULL,       -- User's father's name
    dob DATE NOT NULL,                       -- Date of birth
    gender VARCHAR(10) NOT NULL,             -- Gender (Male/Female)
    phone VARCHAR(15) NOT NULL,              -- Phone number
    UNIQUE (phone)                           -- Ensure phone number is unique
);

-- Table: login
-- Stores login credentials for users
CREATE TABLE login (
    pin VARCHAR(60) PRIMARY KEY,             -- User's unique PIN
    form_no VARCHAR(20),                     -- References signup form_no
    card_number VARCHAR(16) UNIQUE,          -- User's card number
    hashed_pin VARCHAR(60),                  -- Encrypted PIN for security
    FOREIGN KEY (form_no) REFERENCES signup(form_no)
);

-- Table: bank
-- Stores transaction records for deposits, withdrawals, and transfers
CREATE TABLE bank (
    pin VARCHAR(60) NOT NULL,                -- User's PIN
    date DATETIME NOT NULL,                  -- Date and time of transaction
    type VARCHAR(20) NOT NULL,               -- Transaction type (Deposit, Withdrawal, Transfer)
    amount DECIMAL(10, 2) NOT NULL,          -- Transaction amount
    FOREIGN KEY (pin) REFERENCES login(pin)
);

-- Table: transfer
-- Stores details of fund transfers between accounts
CREATE TABLE transfer (
    sender_pin VARCHAR(60) NOT NULL,         -- PIN of the sender
    recipient_pin VARCHAR(60) NOT NULL,      -- PIN of the recipient
    date DATETIME NOT NULL,                  -- Date and time of transfer
    amount DECIMAL(10, 2) NOT NULL,          -- Transfer amount
    FOREIGN KEY (sender_pin) REFERENCES login(pin),
    FOREIGN KEY (recipient_pin) REFERENCES login(pin)
);
