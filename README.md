# 💳 Bank Management System

## 📋 Description

The **Bank Management System** is a robust desktop application built using **Java (Swing)** for the user interface and **MySQL** as the backend database. The system offers essential banking functionalities to streamline user interactions with their bank accounts. It incorporates secure login, account management, and transaction capabilities, providing users with a seamless banking experience.

### ✨ Key Features:
- **User Registration**: Register new users with detailed account information.
- **Deposit Money**: Enable users to securely deposit funds into their accounts.
- **Withdraw Money**: Perform withdrawals with sufficient balance checks.
- **Transfer Funds**: Safely transfer funds between accounts.
- **View Mini Statement**: Generate and display a list of recent transactions.

---

## 🛠️ Technologies Used

- **Java (Swing)**: For creating the user interface.
- **MySQL**: As the database for managing user and transaction data.
- **JDBC**: For establishing connectivity between Java and MySQL.
- **External Libraries**:
  - `mysql-connector-java`: For database connectivity.
  - `jcalendar`: For date selection in forms.

---

## 🚀 Setup Instructions

Follow these steps to set up the project locally:

### 1. Clone the Repository:
```bash
git clone https://github.com/username/bank-management-system.git
cd bank-management-system
``` 
2. Import the Project:

    Open IntelliJ IDEA.
    Import the project folder into your IDE.

3. Set Up the Database:

    Create a new MySQL database.
    Execute the SQL schema file (schema.sql) provided in the project to set up the database tables.

4. Add External Libraries:

    Navigate to libs/external-libraries.zip.
    Extract the ZIP file and add the JAR files to the project's classpath.

5. Run the Application:

    Open Login.java or Signup.java in your IDE.
    Execute the main() method to launch the application.

🗂️ Database Schema

Below are the database schemas used in the project:
bank Table:
```
CREATE TABLE bank (
    pin VARCHAR(10) NOT NULL,
    date DATETIME NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (pin, date)
);
```
login Table:
```
CREATE TABLE login (
    form_no VARCHAR(30) NOT NULL,
    card_number VARCHAR(50) NOT NULL UNIQUE,
    pin VARCHAR(60) NOT NULL,
    PRIMARY KEY (form_no)
);
```
signup Table:
```
CREATE TABLE signup (
    form_no VARCHAR(30) NOT NULL,
    name VARCHAR(100) NOT NULL,
    father_name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    email VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    pin_code VARCHAR(10) NOT NULL,
    PRIMARY KEY (form_no)
);
```
signupthree Table:
```
CREATE TABLE signupthree (
    form_no VARCHAR(30) NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    card_number VARCHAR(50) NOT NULL UNIQUE,
    pin VARCHAR(60) NOT NULL,
    facility VARCHAR(255) NOT NULL,
    PRIMARY KEY (form_no),
    FOREIGN KEY (form_no) REFERENCES signup(form_no)
);
```
📑 Features

    User Registration: Create a new bank account with detailed user information.
    Deposit Money: Deposit funds into the user's account.
    Withdraw Money: Withdraw cash while ensuring sufficient balance.
    Transfer Funds: Transfer money between accounts securely.
    View Mini Statement: View a record of recent transactions.

🤝 Contributions are welcome! 

Enjoy building your Bank Management System! 🚀
