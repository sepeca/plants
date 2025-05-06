import { SERVER_ADDRESS } from './config.js'; // Import server address

function checkLoginStatus() {
    if (document.cookie.includes('jwt=')) {
        window.location.href = './calendar.html'; // Redirect to calendar if logged in
    }
}

checkLoginStatus();

document.getElementById('login-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch(`${SERVER_ADDRESS}/login`, { // Use centralized server address
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password }), // Send email and password
            credentials: 'include', // Include cookies in the request
        });

        if (response.ok) {
            console.log('Set-Cookie header received:', response.headers.get('Set-Cookie')); // Debug log
            showNotification('Login successful!', true);
            //window.location.href = './calendar.html'; // Redirect to calendar
        } else {
            showNotification('Invalid email or password.', false); // Show error notification
        }
    } catch (error) {
        console.error('Error during login:', error);
        showNotification('An error occurred. Please try again later.', false); // Show error notification
    }
});
