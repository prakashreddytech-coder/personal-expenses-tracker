# Filter Verification Test Guide

## Test Scenarios

### 1. Type Filter (DEBIT/CREDIT)
**Test Case 1: Filter by DEBIT**
- Go to History page
- Select "Debit" from Type dropdown
- Click "Apply Filters"
- **Expected**: Only DEBIT transactions shown
- **URL**: `http://localhost:8080/api/expenses/search?userId=1&type=DEBIT`

**Test Case 2: Filter by CREDIT**
- Select "Credit" from Type dropdown
- Click "Apply Filters"
- **Expected**: Only CREDIT transactions shown
- **URL**: `http://localhost:8080/api/expenses/search?userId=1&type=CREDIT`

### 2. Mode Filter (BANK/CARD)
**Test Case 3: Filter by BANK**
- Select "Bank" from Mode dropdown
- Click "Apply Filters"
- **Expected**: Only BANK mode transactions shown
- **URL**: `http://localhost:8080/api/expenses/search?userId=1&mode=BANK`

**Test Case 4: Filter by CARD**
- Select "Card" from Mode dropdown
- Click "Apply Filters"
- **Expected**: Only CARD mode transactions shown
- **URL**: `http://localhost:8080/api/expenses/search?userId=1&mode=CARD`

### 3. Date Filters
**Test Case 5: Filter by Start Date**
- Set Start Date to "2025-11-21"
- Leave End Date empty
- Click "Apply Filters"
- **Expected**: All transactions from 2025-11-21 onwards
- **URL**: `http://localhost:8080/api/expenses/search?userId=1&startDate=2025-11-21`

**Test Case 6: Filter by End Date**
- Leave Start Date empty
- Set End Date to "2025-11-21"
- Click "Apply Filters"
- **Expected**: All transactions up to 2025-11-21
- **URL**: `http://localhost:8080/api/expenses/search?userId=1&endDate=2025-11-21`

**Test Case 7: Filter by Date Range**
- Set Start Date to "2025-11-20"
- Set End Date to "2025-11-22"
- Click "Apply Filters"
- **Expected**: Only transactions between these dates
- **URL**: `http://localhost:8080/api/expenses/search?userId=1&startDate=2025-11-20&endDate=2025-11-22`

### 4. Combined Filters
**Test Case 8: Type + Mode**
- Select "Debit" from Type
- Select "Bank" from Mode
- Click "Apply Filters"
- **Expected**: Only DEBIT transactions via BANK
- **URL**: `http://localhost:8080/api/expenses/search?userId=1&type=DEBIT&mode=BANK`

**Test Case 9: All Filters**
- Set Start Date to "2025-11-21"
- Set End Date to "2025-11-21"
- Select "Debit" from Type
- Select "Bank" from Mode
- Click "Apply Filters"
- **Expected**: Only DEBIT BANK transactions on 2025-11-21
- **URL**: `http://localhost:8080/api/expenses/search?userId=1&startDate=2025-11-21&endDate=2025-11-21&type=DEBIT&mode=BANK`

### 5. Clear Filters
**Test Case 10: Show All**
- Select "All" from Type dropdown
- Select "All" from Mode dropdown
- Clear both date fields
- Click "Apply Filters"
- **Expected**: All transactions shown
- **URL**: `http://localhost:8080/api/expenses/search?userId=1`

## Verification Checklist
- [ ] Type filter works for DEBIT
- [ ] Type filter works for CREDIT
- [ ] Mode filter works for BANK
- [ ] Mode filter works for CARD
- [ ] Start date filter works
- [ ] End date filter works
- [ ] Date range filter works
- [ ] Combined filters work
- [ ] "All" option shows all transactions
- [ ] Table updates correctly after filtering

## Backend Query Logic
The backend uses this JPQL query:
```sql
SELECT e FROM Expense e WHERE e.user.id = :userId
AND (:startDate IS NULL OR e.dateTime >= :startDate)
AND (:endDate IS NULL OR e.dateTime <= :endDate)
AND (:type IS NULL OR :type = '' OR e.type = :type)
AND (:mode IS NULL OR :mode = '' OR e.mode = :mode)
```

This ensures:
- NULL or empty parameters are ignored
- Non-empty parameters filter results
- All conditions are combined with AND
