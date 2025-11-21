# Personal Expense Tracker - Walkthrough

## Prerequisites
- Java 17+
- Maven
- PostgreSQL

## Setup Instructions

### 1. Database Setup
Create a PostgreSQL database named `expense_tracker`:
```sql
CREATE DATABASE expense_tracker;
```
Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. Email Setup
Update `src/main/resources/application.properties` with your SMTP details for email reports:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### 3. Build and Run
Navigate to the project root and run:
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080`.

## Verification Steps

### 1. User Registration & Login
1. Go to `http://localhost:8080`.
2. Click "Register" and create an account.
3. Login with your credentials.

### 2. Manage Accounts
1. Go to "Manage" page.
2. Add a **Bank** (e.g., "HDFC", Opening Balance: 50000).
3. Add a **Credit Card** (e.g., "Visa", Limit: 100000).
4. Add a **Platform** (e.g., "GPay").

### 3. Add Expenses
1. On "Manage" page, add an expense:
   - **Debit Bank**: 5000 for "Rent" -> Check Dashboard, Bank Balance should be 45000.
   - **Debit Card**: 2000 for "Shopping" -> Check Dashboard, Card Used should be 2000.

### 4. Dashboard
- Verify "Total Bank Balance" matches sum of all banks.
- Verify "Total Card Available" matches (Total Limits - Used Limits).
- Verify "This Month Spend" aggregates correctly.

### 5. History & Filters
- Go to "History" page.
- Filter by "Debit" or "Credit".
- Filter by Date Range.
- Verify the table updates correctly.

### 6. Profile Management
- Go to "Profile" page.
- Update your username or email.
- Logout and Login again to verify changes.
- **Danger Zone**: Try deleting the account (creates a permanent deletion).

### 7. Monthly Report
- The report is scheduled for the last day of the month.
- To test manually, you can modify `ReportScheduler.java` to run every minute:
  ```java
  @Scheduled(cron = "0 * * * * ?")
  ```
- Check your email for the PDF attachments.
