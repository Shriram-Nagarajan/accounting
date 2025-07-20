# Accounting Application

Track where you spend your money. Categorize your expenses and track them by category.  Full-stack web application built using React for the front end and Spring Boot for the back end. Scalable, maintainable, and easy to use.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)

## Features

- User registration, authentication and reset password
- Upload income and expense transaction data
- Income and expense tracker
- Categorize expenses and track expenses by category

## Technologies

- **Responsive UI**: React, Redux, Axios, Material UI
- **RESTful API / Micro-service**: Spring Boot, Spring Security, Spring Data JPA, Hibernate, Memcached
- **Database**: MySQL
- **Build tools**: Maven, npm

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 17
- Node.js and npm
- MySQL
- Maven
- Memcached
- Python 3.7+ (for AI-based expense categorization)

## Installation

### Clone the Repository

```bash
git clone https://github.com/Shriram-Nagarajan/accounting.git
cd accounting
```

### Back End Setup

1. Maven build the backend applications:

    ```bash
    cd common
    mvn clean install
    cd ../user_management
    mvn clean install
    cd ../api
    mvn clean install
    ```

2. Update the `application-local.properties` file in Accounting service with your MySQL configuration:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/accounts
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    ```

3. Update the `application-local.properties` file in UserManagement service with your MySQL configuration:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/userdb
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    ```

 4. Run the Spring Boot applications:

    ```bash
    cd ../user_management
    mvn spring-boot:run
    cd ../api
    mvn spring-boot:run
    ```

### Front End Setup

1. Navigate to the front end directory:

    ```bash
    cd ../ui/my-app
    ```

2. Install the dependencies:

    ```bash
    npm install
    ```

3. Start the React application:

    ```bash
    npm start
    ```

### Python AI Categorization Setup

For automated expense categorization using machine learning:

1. Install required Python packages:

    ```bash
    pip install pandas numpy scikit-learn matplotlib seaborn openpyxl
    ```

2. Navigate to the Python directory:

    ```bash
    cd python
    ```

3. Train the model and categorize expenses:

    ```bash
    python predict_expense_categories.py path/to/your/expenses.csv
    ```

## Usage

After completing the installation steps, you can access the application by opening your browser and navigating to `http://localhost:3000` for the front end and `http://localhost:8080` for the User Management APIs and `http://localhost:8081` for the Accounting APIs.
