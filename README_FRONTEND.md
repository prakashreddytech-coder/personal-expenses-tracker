# Frontend Documentation - Personal Expense Tracker

This document explains how the frontend is structured, how to verify the files, and how to make modifications.

## 📂 Frontend Structure
The frontend is built with standard **HTML, CSS, and JavaScript**. It is served directly by the Spring Boot backend as static resources.

**Location**: `src/main/resources/static/`

### Key Files:
1. **`index.html`**: The entry point. Handles User Registration and Login.
2. **`dashboard.html`**: The main dashboard showing financial stats, bank lists, and card lists.
3. **`manage.html`**: The management interface to add Banks, Cards, Platforms, and Expenses.
4. **`styles.css`**: Contains all the styling for the application.
5. **`app.js`**: Contains the core JavaScript logic (API calls, DOM manipulation, Auth handling).

## 🔍 How to Check Files
You can navigate to the `src/main/resources/static/` folder to view the source code.

- **HTML Files**: Define the structure of the pages.
- **CSS File**: Defines the look and feel (colors, layout, fonts).
- **JS File**: Defines the behavior (fetching data from backend, updating UI).

## 🛠️ How to Setup & Run
Since the frontend is integrated with the Spring Boot backend, you **do not** need a separate frontend server (like Node.js or React).

1. **Start the Backend**:
   Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
2. **Access the App**:
   Open your browser and go to:
   **http://localhost:8080**

   - This will load `index.html` automatically.

## ✏️ How to Modify
If you want to change the UI or logic, edit the files in `src/main/resources/static/`.

### Common Modifications:
- **Change Colors/Fonts**: Edit `styles.css`.
- **Add New Fields**:
    1. Edit the HTML file (e.g., `manage.html`) to add the input field.
    2. Edit `app.js` to capture the new value and send it in the API request.
- **Change Logic**: Edit `app.js` to modify how data is processed or displayed.

### Hot Reloading
- By default, Spring Boot caches static resources.
- To see changes immediately during development, you may need to restart the application or configure Spring Boot DevTools.
- **Quick Fix**: After editing a file, restart the server (`mvn spring-boot:run`) and refresh the browser.
