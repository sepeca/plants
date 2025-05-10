import { SERVER_ADDRESS } from './config.js';

document.getElementById('register-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const organizationName = document.getElementById('organization').value;
    const name = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (password !== confirmPassword) {
        showNotification('Passwords do not match.',false);
        return;
    }

    try {
        const response = await fetch(`${SERVER_ADDRESS}/register_self`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ organizationName, name, email, password })
        });

        const data = await response.json();

        if (data.status === "success") {
            window.location.href = './login.html'; // Redirect to login
        } else {
            showNotification(`Registration failed: ${data.message || 'Unknown error'}`, false);
        }
    } catch (error) {
        console.error('Error during registration:', error);
        showNotification('An error occurred. Please try again later.',false);
    }
});
