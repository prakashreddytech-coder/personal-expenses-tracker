# Personal Expense Tracker

A comprehensive web application to track personal expenses, manage bank accounts and credit cards, and receive monthly email reports.

## 🚀 Tech Stack
- **Backend**: Java, Spring Boot
- **Database**: PostgreSQL
- **Frontend**: HTML, CSS, JavaScript (Served by Spring Boot)
- **PDF/Email**: iText, JavaMailSender

## 🛠️ Prerequisites
Before running the application, ensure you have the following installed:
1. **Java 17** or higher
2. **Maven** (for building the project)
3. **PostgreSQL** (running on port 5432)

## ⚙️ Setup Instructions

### 1. Database Setup
1. Open your PostgreSQL tool (pgAdmin or psql).
2. Create a new database named `expense_tracker`:
   ```sql
   CREATE DATABASE expense_tracker;
   ```
3. Open `src/main/resources/application.properties` and update your database username and password:
   ```properties
   spring.datasource.username=YOUR_POSTGRES_USERNAME
   spring.datasource.password=YOUR_POSTGRES_PASSWORD
   ```

### 2. Email Configuration (Optional)
To receive monthly PDF reports, configure your SMTP settings in `src/main/resources/application.properties`:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```
*Note: For Gmail, you need to generate an App Password.*

### 3. Build and Run
Open a terminal in the project root folder and run:

```bash
mvn spring-boot:run
```

The application will start at: **http://localhost:8080**

## 🖥️ How to Use
1. **Register**: Create a new account on the login page.
2. **Login**: Use your credentials to access the dashboard.
3. **Manage**: Go to the "Manage" page to:
   - Add Banks (e.g., HDFC, SBI)
   - Add Credit Cards
   - Add Payment Platforms (GPay, PhonePe)
   - Add Expenses (Debit from Bank or Card)
4. **Dashboard**: View your total balance, card availability, and monthly spending.
5. **Reports**: Automated emails are sent on the last day of the month.

## 📂 Project Structure
- `src/main/java`: Backend source code
- `src/main/resources/static`: Frontend HTML/CSS/JS files
