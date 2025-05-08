import { SERVER_ADDRESS } from './config.js';

document.getElementById('register-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const organizationName = document.getElementById('organization').value;
    const name = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (password !== confirmPassword) {
        alert('Passwords do not match.');
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
            alert(`Registration failed: ${data.message || 'Unknown error'}`);
        }
    } catch (error) {
        console.error('Error during registration:', error);
        alert('An error occurred. Please try again later.');
    }
});
