// Check if user is already logged in
function checkLoginStatus() {
    const cookies = document.cookie.split(';').reduce((acc, cookie) => {
        const [key, value] = cookie.trim().split('=');
        acc[key] = value;
        return acc;
    }, {});

    if (cookies.authToken && cookies.username && cookies.role) {
        window.location.href = '/pages/calendar.html'; // Redirect to calendar if logged in
    }
}

checkLoginStatus();

// Automatically fill the organization field
async function loadOrganization() {
    try {
        const response = await fetch('/api/get-organization');
        const data = await response.json();
        if (data.organizationName) {
            document.getElementById('organization').value = data.organizationName; // Fill organization field
        } else {
            console.error('Organization name not found in response:', data);
        }
    } catch (error) {
        console.error('Error loading organization name:', error);
    }
}

loadOrganization();

// Handle login form submission
document.getElementById('login-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }) // Send username and password
        });

        if (response.ok) {
            const data = await response.json();
            document.cookie = `authToken=${data.token}; max-age=7200; path=/`; // Auth token valid for 2 hours
            document.cookie = `username=${data.username}; max-age=7200; path=/`; // Username cookie
            document.cookie = `role=${data.role}; max-age=7200; path=/`; // Role cookie
            showNotification('Login successful!', true);
            window.location.href = '/pages/calendar.html'; // Redirect to calendar
        } else {
            showNotification('Invalid username or password.', false); // Show error notification
        }
    } catch (error) {
        console.error('Error during login:', error);
        showNotification('An error occurred. Please try again later.', false); // Show error notification
    }
});
