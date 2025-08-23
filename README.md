# 🛒 E-Commerce Monolith Backend API

<img width="900" alt="entities relationship diagram" src="https://github.com/user-attachments/assets/592521b7-dbf4-41d1-9fb0-d7fc5bd12d4a" />

## Environment Variables
To run this project, you will need to add the following environment variables to your `.env` file. You can use some of the example values provided in `.env.example`.

| Variable                     | Description                                               | Example                               |
|------------------------------|-----------------------------------------------------------|---------------------------------------|
| `SECRET_KEY`                 | Secret key for signing and verifying JWT tokens.          | V1U5UUM1WGNDaEJ...                     |
| `TOKEN_EXPIRATION`           | Expiration time for access tokens in milliseconds.        | 3600000                               |
| `TOKEN_REFRESH_EXPIRATION`   | Expiration time for refresh tokens in milliseconds.       | 259200000                             |
| `RESET_TOKEN_EXPIRATION`     | Expiration time for password reset tokens in seconds.     | 300                                   |
| `SPRING_DATASOURCE_USERNAME` | MySQL database username.                                  | your_username                         |
| `SPRING_DATASOURCE_PASSWORD` | MySQL database password.                                  | your_password                         |
| `SPRING_DATASOURCE_URL`      | Database connection URL.                                  | jdbc:mysql://db:3306/your_database_name |
| `SUPPORT_EMAIL`              | Support email address for notifications.                  | jhon@doe.com                          |
| `SUPPORT_EMAIL_PASSWORD`     | Support email password.                                   | xxxx xxxx xxxx xxxx                   |
| `PAYPAL_CLIENT_ID`           | Client ID for the PayPal API.                             | your_paypal_client_id                 |
| `PAYPAL_CLIENT_SECRET`       | Client secret for the PayPal API.                         | your_paypal_client_secret             |
| `PAYPAL_MODE`                | PayPal environment mode (sandbox or live).                | sandbox                               |
| `APPLICATION_URL`            | Base URL of the backend application.                      | http://localhost:8080                 |
| `FRONTEND_URL`               | Base URL of the frontend application.                     | http://localhost:5173                 |
