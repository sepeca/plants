import { SERVER_ADDRESS } from './config.js';

function checkLoginStatus() {
    const token = localStorage.getItem('jwt');
    if (token) {
        window.location.href = './calendar.html';
    }
}

checkLoginStatus();

document.getElementById('login-form').addEventListener('submit', async function (event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch(`${SERVER_ADDRESS}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('jwt', data.token);
            showNotification('Login successful!', true);
            window.location.href = './calendar.html';
        } else {
            showNotification('Invalid email or password.', false);
        }
    } catch (error) {
        showNotification('An error occurred. Please try again later.', false);
    }
});