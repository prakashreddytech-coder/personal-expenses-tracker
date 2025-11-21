const API_BASE = 'http://localhost:8080/api';
let currentUser = JSON.parse(localStorage.getItem('user'));

// --- Auth Functions ---

async function login(email, password) {
    try {
        const response = await fetch(`${API_BASE}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        if (response.ok) {
            const user = await response.json();
            localStorage.setItem('user', JSON.stringify(user));
            if (user.role === 'ADMIN') {
                window.location.href = 'admin.html';
            } else {
                window.location.href = 'dashboard.html';
            }
        } else {
            await handleError(response);
        }
    } catch (error) {
        console.error('Login error:', error);
        showAlert('danger', 'An unexpected error occurred.');
    }
}

async function register(username, email, password) {
    try {
        const response = await fetch(`${API_BASE}/users/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });
        if (response.ok) {
            showAlert('success', 'Registration successful! Please login.');
            setTimeout(() => window.location.href = 'index.html', 2000);
        } else {
            await handleError(response);
        }
    } catch (error) {
        console.error('Registration error:', error);
        showAlert('danger', 'An unexpected error occurred.');
    }
}

function logout() {
    localStorage.removeItem('user');
    window.location.href = 'index.html';
}

// --- Dashboard Functions ---

// --- Dashboard Functions ---

async function loadDashboard() {
    if (!currentUser) return;
    try {
        const response = await fetch(`${API_BASE}/dashboard/${currentUser.id}`);
        const stats = await response.json();

        document.getElementById('totalBankBalance').innerText = formatCurrency(stats.totalBankBalance);
        document.getElementById('totalCardAvailable').innerText = formatCurrency(stats.totalCardAvailable);
        document.getElementById('monthlyTotalSpend').innerText = formatCurrency(stats.monthlyTotalSpend);

        const bankList = document.getElementById('bankList');
        if (bankList) {
            bankList.innerHTML = stats.banks.map(bank => `
                <div class="card">
                    <div class="card-header">
                        <span class="card-title">${bank.name}</span>
                    </div>
                    <p class="text-muted">Balance</p>
                    <h3>${formatCurrency(bank.currentBalance)}</h3>
                </div>
            `).join('');
        }

        const cardList = document.getElementById('cardList');
        if (cardList) {
            cardList.innerHTML = stats.cards.map(card => `
                <div class="card">
                    <div class="card-header">
                        <span class="card-title">${card.name}</span>
                    </div>
                    <div class="flex justify-between mb-2">
                        <span class="text-muted">Available</span>
                        <span class="font-bold">${formatCurrency(card.totalLimit - card.usedLimit)}</span>
                    </div>
                    <div class="flex justify-between">
                        <span class="text-muted">Used</span>
                        <span class="text-danger">${formatCurrency(card.usedLimit)}</span>
                    </div>
                </div>
            `).join('');
        }

        // Load Platforms in Dashboard
        const platformList = document.getElementById('platformList');
        if (platformList) {
            // We need to fetch platforms separately if not in stats, but let's assume we fetch them
            // Or we can fetch them here if dashboard stats doesn't include them
            const platformsRes = await fetch(`${API_BASE}/platforms/user/${currentUser.id}`);
            if (platformsRes.ok) {
                const platforms = await platformsRes.json();
                platformList.innerHTML = platforms.map(p => `
                    <div class="card">
                        <div class="card-header">
                            <span class="card-title">${p.name}</span>
                        </div>
                    </div>
                `).join('');
            }
        }

    } catch (error) {
        console.error('Dashboard error:', error);
    }
}

// --- Management Functions ---

async function addBank(event) {
    event.preventDefault();
    const form = event.target;
    const data = {
        name: form.name.value,
        openingBalance: form.openingBalance.value,
        currentBalance: form.openingBalance.value
    };
    await submitForm(`${API_BASE}/banks?userId=${currentUser.id}`, data, form, 'Bank added successfully!');
    loadManageData(); // Refresh list
}

async function addCard(event) {
    event.preventDefault();
    const form = event.target;
    const data = {
        name: form.name.value,
        totalLimit: form.totalLimit.value,
        usedLimit: 0
    };
    await submitForm(`${API_BASE}/credit-cards?userId=${currentUser.id}`, data, form, 'Credit Card added successfully!');
    loadManageData(); // Refresh list
}

async function addPlatform(event) {
    event.preventDefault();
    const form = event.target;
    const data = { name: form.name.value };
    await submitForm(`${API_BASE}/platforms?userId=${currentUser.id}`, data, form, 'Platform added successfully!');
    loadManageData(); // Refresh list
}

async function addExpense(event) {
    event.preventDefault();
    const form = event.target;
    const data = {
        title: form.title.value,
        description: form.description.value,
        amount: form.amount.value,
        type: form.type.value,
        mode: form.mode.value,
        dateTime: form.dateTime.value,
        bank: form.bankId.value ? { id: form.bankId.value } : null,
        card: form.cardId.value ? { id: form.cardId.value } : null,
        platform: form.platformId.value ? { id: form.platformId.value } : null
    };
    await submitForm(`${API_BASE}/expenses?userId=${currentUser.id}`, data, form, 'Expense added successfully!');
}

async function submitForm(url, data, form, successMessage) {
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (response.ok) {
            showAlert('success', successMessage);
            form.reset();
        } else {
            await handleError(response);
        }
    } catch (error) {
        console.error('Submission error:', error);
        showAlert('danger', 'An unexpected error occurred.');
    }
}

// Delete Functions
async function deleteBank(id) {
    if (!confirm('Delete this bank?')) return;
    await deleteItem(`${API_BASE}/banks/${id}`);
    loadManageData();
}

async function deleteCard(id) {
    if (!confirm('Delete this card?')) return;
    await deleteItem(`${API_BASE}/credit-cards/${id}`);
    loadManageData();
}

async function deletePlatform(id) {
    if (!confirm('Delete this platform?')) return;
    await deleteItem(`${API_BASE}/platforms/${id}`);
    loadManageData();
}

async function deleteItem(url) {
    try {
        const res = await fetch(url, { method: 'DELETE' });
        if (res.ok) showAlert('success', 'Deleted successfully');
        else await handleError(res);
    } catch (e) {
        console.error(e);
        showAlert('danger', 'Delete failed');
    }
}

async function loadManageData() {
    if (!currentUser || !document.getElementById('manageBankList')) return;

    try {
        const [banks, cards, platforms] = await Promise.all([
            fetch(`${API_BASE}/banks/user/${currentUser.id}`).then(r => r.json()),
            fetch(`${API_BASE}/credit-cards/user/${currentUser.id}`).then(r => r.json()),
            fetch(`${API_BASE}/platforms/user/${currentUser.id}`).then(r => r.json())
        ]);

        const renderList = (items, id, deleteFn, labelFn) => {
            const el = document.getElementById(id);
            if (el) {
                el.innerHTML = items.map(item => `
                    <div class="flex justify-between items-center p-2 border-b">
                        <span>${labelFn(item)}</span>
                        <button onclick="${deleteFn}(${item.id})" class="btn btn-danger" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;">Delete</button>
                    </div>
                `).join('');
            }
        };

        renderList(banks, 'manageBankList', 'deleteBank', b => `${b.name} (${formatCurrency(b.currentBalance)})`);
        renderList(cards, 'manageCardList', 'deleteCard', c => `${c.name} (Limit: ${formatCurrency(c.totalLimit)})`);
        renderList(platforms, 'managePlatformList', 'deletePlatform', p => p.name);

    } catch (e) {
        console.error('Error loading manage data', e);
    }
}

// --- Dropdown Functions ---

async function loadDropdowns() {
    if (!currentUser) return;

    const fetchSafe = async (url) => {
        try {
            const res = await fetch(url);
            if (!res.ok) throw new Error(`HTTP ${res.status}`);
            return await res.json();
        } catch (e) {
            console.error(`Failed to fetch ${url}`, e);
            return [];
        }
    };

    try {
        const [banks, cards, platforms] = await Promise.all([
            fetchSafe(`${API_BASE}/banks/user/${currentUser.id}`),
            fetchSafe(`${API_BASE}/credit-cards/user/${currentUser.id}`),
            fetchSafe(`${API_BASE}/platforms/user/${currentUser.id}`)
        ]);

        const bankSelect = document.getElementById('bankId');
        if (bankSelect) {
            bankSelect.innerHTML = '<option value="">Select Bank</option>' +
                (banks.length ? banks.map(b => `<option value="${b.id}">${b.name}</option>`).join('') : '<option value="" disabled>No Banks Found</option>');
        }

        const cardSelect = document.getElementById('cardId');
        if (cardSelect) {
            cardSelect.innerHTML = '<option value="">Select Card</option>' +
                (cards.length ? cards.map(c => `<option value="${c.id}">${c.name}</option>`).join('') : '<option value="" disabled>No Cards Found</option>');
        }

        const platformSelect = document.getElementById('platformId');
        if (platformSelect) {
            platformSelect.innerHTML = '<option value="">Select Platform</option>' +
                (platforms.length ? platforms.map(p => `<option value="${p.id}">${p.name}</option>`).join('') : '<option value="" disabled>No Platforms Found</option>');
        }

        // Also populate filter dropdowns if they exist (History page)
        const filterPlatform = document.getElementById('filterPlatform');
        if (filterPlatform) {
            filterPlatform.innerHTML = '<option value="">All Platforms</option>' +
                (platforms.length ? platforms.map(p => `<option value="${p.name}">${p.name}</option>`).join('') : '');
        }

    } catch (error) {
        console.error('Error loading dropdowns:', error);
        showAlert('danger', 'Failed to load account options. Please refresh.');
    }
}

// --- History Functions ---

async function loadHistory() {
    if (!currentUser) return;

    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    const type = document.getElementById('filterType').value;
    const mode = document.getElementById('filterMode').value;
    // Add platform filter if you want to filter by platform name, 
    // but backend currently only supports type/mode/dates in searchExpenses.
    // If user wants platform filter, we need to update backend too.
    // For now, let's just show it in table.

    let url = `${API_BASE}/expenses/search?userId=${currentUser.id}`;
    if (startDate) url += `&startDate=${startDate}`;
    if (endDate) url += `&endDate=${endDate}`;
    if (type) url += `&type=${type}`;
    if (mode) url += `&mode=${mode}`;

    try {
        const response = await fetch(url);
        const expenses = await response.json();
        renderHistoryTable(expenses);
    } catch (error) {
        console.error('History load error:', error);
    }
}

function renderHistoryTable(expenses) {
    const tbody = document.getElementById('historyTableBody');
    if (!tbody) return;

    tbody.innerHTML = expenses.map(exp => `
        <tr>
            <td>${new Date(exp.dateTime).toLocaleDateString()}</td>
            <td>${exp.title}</td>
            <td>${exp.type}</td>
            <td>${exp.mode}</td>
            <td class="${exp.type === 'CREDIT' ? 'text-success' : 'text-danger'}">
                ${formatCurrency(exp.amount)}
            </td>
            <td>${exp.bank ? exp.bank.name : (exp.card ? exp.card.name : '-')}</td>
            <td>${exp.platform ? exp.platform.name : '-'}</td>
        </tr>
    `).join('');
}

// --- Profile Functions ---

async function loadProfile() {
    if (!currentUser) return;
    document.getElementById('username').value = currentUser.username;
    document.getElementById('email').value = currentUser.email;
}

async function updateProfile(event) {
    event.preventDefault();
    const form = event.target;
    const data = {
        username: form.username.value,
        email: form.email.value,
        password: form.password.value
    };

    try {
        const response = await fetch(`${API_BASE}/users/${currentUser.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const updatedUser = await response.json();
            localStorage.setItem('user', JSON.stringify(updatedUser));
            currentUser = updatedUser;
            showAlert('success', 'Profile updated successfully!');
        } else {
            await handleError(response);
        }
    } catch (error) {
        console.error('Update error:', error);
        showAlert('danger', 'An unexpected error occurred.');
    }
}

async function deleteAccount() {
    if (!confirm('Are you sure you want to delete your account? This action cannot be undone.')) return;

    try {
        const response = await fetch(`${API_BASE}/users/${currentUser.id}?permanent=true`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('Account deleted successfully.');
            logout();
        } else {
            await handleError(response);
        }
    } catch (error) {
        console.error('Delete error:', error);
    }
}

// --- Navigation & Layout Functions ---

function updateNavigation() {
    if (currentUser) {
        const navUsername = document.getElementById('navUsername');
        if (navUsername) {
            navUsername.innerText = currentUser.username;
        }
        const userAvatar = document.getElementById('userAvatar');
        if (userAvatar) {
            userAvatar.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(currentUser.username)}&background=random`;
        }

        // Show Admin Link if user is admin
        const adminLink = document.getElementById('adminLink');
        if (adminLink && currentUser.role === 'ADMIN') {
            adminLink.style.display = 'flex';
        }
    }
}

function toggleProfileMenu() {
    const menu = document.getElementById('profileDropdown');
    if (menu) {
        menu.classList.toggle('show');
    }
}

function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    sidebar.classList.toggle('active');
}

// Close dropdown when clicking outside
document.addEventListener('click', function (event) {
    const profileMenu = document.querySelector('.profile-menu');
    const menu = document.getElementById('profileDropdown');
    if (profileMenu && !profileMenu.contains(event.target) && menu && menu.classList.contains('show')) {
        menu.classList.remove('show');
    }
});

// --- Helper Functions ---

function setDefaultDates() {
    const dateTimeInput = document.querySelector('input[name="dateTime"]');
    if (dateTimeInput) {
        const now = new Date();
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        dateTimeInput.value = now.toISOString().slice(0, 16);
    }

    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    if (startDateInput && endDateInput) {
        const today = new Date().toISOString().split('T')[0];
        startDateInput.value = today;
        endDateInput.value = today;
    }
}

async function handleError(response) {
    try {
        const errorData = await response.json();
        let message = errorData.message || 'An error occurred';

        if (typeof errorData === 'object' && !errorData.message) {
            const messages = Object.values(errorData);
            if (messages.length > 0) {
                message = messages.join('\n');
            } else {
                message = JSON.stringify(errorData);
            }
        }

        showAlert('danger', message);
    } catch (e) {
        console.error('Error parsing error response:', e);
        showAlert('danger', `Request failed: ${response.status} ${response.statusText}`);
    }
}

function showAlert(type, message) {
    const alertBox = document.getElementById('alertBox');
    if (alertBox) {
        alertBox.className = `alert alert-${type}`;
        alertBox.innerText = message;
        alertBox.style.display = 'block';
        setTimeout(() => alertBox.style.display = 'none', 5000);
    } else {
        alert(message);
    }
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(amount);
}

function checkAuth() {
    if (!currentUser && !window.location.href.includes('index.html')) {
        window.location.href = 'index.html';
    }
    return currentUser;
}

// Initialize
checkAuth();
setDefaultDates();
updateNavigation();

if (document.getElementById('historyTableBody')) {
    loadHistory();
    loadDropdowns(); // For filter
}
if (document.getElementById('totalBankBalance')) {
    loadDashboard();
}
if (document.getElementById('bankId')) {
    loadDropdowns();
}
if (document.getElementById('manageBankList')) {
    loadManageData();
}

// --- Admin Dashboard Logic ---

async function loadAdminDashboard() {
    const user = checkAuth();
    if (!user || user.role !== 'ADMIN') {
        window.location.href = 'dashboard.html';
        return;
    }
    updateNavigation();

    try {
        const statsResponse = await fetch(`${API_BASE}/users/stats`);
        if (statsResponse.ok) {
            const stats = await statsResponse.json();
            document.getElementById('totalUsers').textContent = stats.totalUsers;
            document.getElementById('activeUsers').textContent = stats.activeUsers;
        }

        const usersResponse = await fetch(`${API_BASE}/users`);
        if (usersResponse.ok) {
            const users = await usersResponse.json();
            const tbody = document.getElementById('userTableBody');
            tbody.innerHTML = users.map(u => `
                <tr>
                    <td>${u.id}</td>
                    <td>${u.username}</td>
                    <td>${u.email}</td>
                    <td><span class="badge ${u.role === 'ADMIN' ? 'badge-credit' : 'badge-bank'}">${u.role}</span></td>
                    <td>${u.status}</td>
                    <td>
                        ${u.role !== 'ADMIN' ? `<button onclick="deleteUser(${u.id})" class="btn btn-danger" style="padding: 0.25rem 0.5rem; font-size: 0.8rem;">Delete</button>` : ''}
                    </td>
                </tr>
            `).join('');
        }
    } catch (error) {
        console.error('Error loading admin dashboard:', error);
    }
}

async function deleteUser(userId) {
    if (!confirm('Are you sure you want to delete this user? This action cannot be undone.')) return;

    try {
        const response = await fetch(`${API_BASE}/users/${userId}`, {
            method: 'DELETE'
        });
        if (response.ok) {
            showAlert('success', 'User deleted successfully');
            loadAdminDashboard();
        } else {
            await handleError(response);
        }
    } catch (error) {
        console.error('Error deleting user:', error);
        showAlert('danger', 'Failed to delete user');
    }
}
