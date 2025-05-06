// Check if user is already logged in
function checkLoginStatus() {
    const cookies = document.cookie.split(';').reduce((acc, cookie) => {
        const [key, value] = cookie.trim().split('=');
        acc[key] = value;
        return acc;
    }, {});

    if (cookies.authToken) {
        window.location.href = '/pages/calendar.html'; // Redirect to calendar if logged in
    }
}

checkLoginStatus();

// Handle login form submission
document.getElementById('login-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password }) // Send email and password
        });

        if (response.ok) {
            const data = await response.json();
            document.cookie = `authToken=${data.token}; max-age=7200; path=/`; // Auth token valid for 2 hours
            showNotification('Login successful!', true);
            window.location.href = '/pages/calendar.html'; // Redirect to calendar
        } else {
            showNotification('Invalid email or password.', false); // Show error notification
        }
    } catch (error) {
        console.error('Error during login:', error);
        showNotification('An error occurred. Please try again later.', false); // Show error notification
    }
});
