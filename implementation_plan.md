# Implementation Plan - Personal Expense Tracker

## Goal Description
Build a Personal Expense Tracker web application to help users manage financial accounts, track daily expenses, and analyze monthly usage. The app will include automated monthly email reports with PDF attachments.

## Coverage of User Requirements (10 Steps)
This plan is designed to cover all 10 steps specified in the requirements:
1. **Purpose**: Centralized tracking of banks, cards, and expenses.
2. **Target Users**: Individuals and small business owners.
3. **Core Modules**: All 8 modules (Auth, Bank, Card, Platform, Expense, History, Dashboard, Reports) are included.
4. **Functionality**: Detailed breakdown below.
5. **Dashboard**: Specific widgets for Banks, Cards, and Monthly Analytics.
6. **Reports**: Monthly PDF generation (Banks & Cards) via Email.
7. **Architecture**: Spring Boot (Controller, Service, Repository, Entity).
8. **Database**: Users, Banks, CreditCards, Platforms, Expenses.
9. **Frontend**: Responsive HTML/CSS/JS with AJAX.
10. **Success Metrics**: Focus on accuracy, speed, and security.

## Proposed Changes

### 1. Project Structure & Dependencies (Tech Stack)
- **Backend**: Spring Boot (Java 17+)
- **Database**: PostgreSQL
- **Dependencies**:
    - `spring-boot-starter-web` (REST APIs)
    - `spring-boot-starter-data-jpa` (Database)
    - `spring-boot-starter-mail` (Email)
    - `spring-boot-starter-validation` (Data validation)
    - `itextpdf` or `pdfbox` (PDF Generation)
    - `lombok` (Boilerplate reduction)

### 2. Database Schema & Entities (Step 8)
- **User**: `id`, `name`, `email`, `password`, `status` (Active/Deactivated for temp delete).
- **Bank**: `id`, `user_id`, `name`, `opening_balance`, `current_balance`.
- **CreditCard**: `id`, `user_id`, `name`, `total_limit`, `used_limit`.
- **Platform**: `id`, `user_id`, `name` (e.g., GPay, PhonePe).
- **Expense**: `id`, `user_id`, `title`, `description`, `amount`, `type` (Debit/Credit), `mode` (Bank/Card), `bank_id`, `card_id`, `platform_id`, `timestamp`.

### 3. Core Modules Implementation (Step 3 & 4)

#### User Module
- **Register/Login**: Standard Auth.
- **Edit Profile**: Update details.
- **Delete Account**:
    - *Temporary*: Toggle `status` to Deactivated.
    - *Permanent*: Cascade delete all user data.

#### Bank Module
- **CRUD**: Add/Edit/Delete Banks.
- **Logic**: Deleting a bank updates linked expenses/dashboard.

#### Credit Card Module
- **CRUD**: Add/Edit/Delete Cards.
- **Logic**: Track `total_limit` vs `used_limit`.

#### Platform Module
- **CRUD**: Manage payment platforms.

#### Expense Module
- **Add/Edit/Delete**:
    - **Debit Bank**: Decrease `current_balance`.
    - **Credit Bank**: Increase `current_balance`.
    - **Debit Card**: Increase `used_limit`.
    - **Credit Card**: Decrease `used_limit`.
- **History**: Filtering by Date, Category, Mode, Platform.

### 4. Dashboard (Step 5)
- **Widgets**:
    - Total Bank Balance (Sum of all banks).
    - Total Card Availability (Total Limit - Used Limit).
    - Individual Bank Balances & Card Limits.
    - **Monthly Analytics**: Total spend (Bank vs Card vs Total).

### 5. Reporting & Scheduling (Step 6)
- **Scheduler**: `@Scheduled` cron job for last day of the month.
- **PDF Generation**:
    - **Bank Report**: Opening/Closing balance, transaction history.
    - **Card Report**: Limits, usage, transaction history.
- **Email**: Send both PDFs as attachments.

### 6. Frontend (Step 9)
- **Tech**: HTML, CSS (Vanilla/Custom), JavaScript (Vanilla).
- **Features**: Responsive design, AJAX for smooth updates without reload.

## Verification Plan
### Automated Tests
- Unit tests for `ExpenseService` to verify balance calculations (Success Metric: 100% accuracy).
### Manual Verification
- **Flows**:
    - Register -> Login.
    - Add Bank -> Add Card -> Add Platform.
    - Add Expense (Debit Bank) -> Verify Bank Balance decreases.
    - Add Expense (Debit Card) -> Verify Card Used Limit increases.
    - Check Dashboard totals.
    - Trigger Monthly Report manually -> Check Email & PDF content.
