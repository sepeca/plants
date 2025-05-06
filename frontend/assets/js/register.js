document.getElementById('register-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const organization = document.getElementById('organization').value;
    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;

    if (password !== confirmPassword) {
        alert('Passwords do not match.');
        return;
    }

    try {
        const response = await fetch('/api/register_self', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ organization, username, email, password })
        });

        const data = await response.json();

        if (data.status === "success") {
            document.cookie = `authToken=${data.token}; max-age=7200; path=/`; // Store token as a cookie
            window.location.href = '/pages/calendar.html'; // Redirect to calendar
        } else {
            alert(`Registration failed: ${data.message || 'Unknown error'}`);
        }
    } catch (error) {
        console.error('Error during registration:', error);
        alert('An error occurred. Please try again later.');
    }
});
