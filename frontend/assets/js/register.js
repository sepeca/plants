async function checkAdminExists() {
    try {
        const response = await fetch('/api/check-admin');
        const adminExists = await response.json();

        if (adminExists) {
            window.location.href = '/pages/login.html';
        }
    } catch (error) {
        console.error('Error checking admin existence:', error);
    }
}

checkAdminExists();

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
        const response = await fetch('/api/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ organization, username, email, password })
        });

        if (response.ok) {
            window.location.href = '/pages/login.html';
        } else {
            alert('Registration failed. Please try again.');
        }
    } catch (error) {
        console.error('Error during registration:', error);
        alert('An error occurred. Please try again later.');
    }
});
