// Import checkLogin.js functionality by including it in the HTML
checkLoginStatus();

function logout() {
    document.cookie = 'authToken=; max-age=0; path=/'; // Clear auth token
    window.location.href = './login.html'; // Redirect to login
}

document.getElementById('change-username-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const newUsername = document.getElementById('new-username').value;

    if (!newUsername) {
        alert('Username cannot be empty.');
        return;
    }

    const authToken = document.cookie.split(';').find(cookie => cookie.trim().startsWith('authToken='))
        ?.split('=')[1];

    if (!authToken) {
        alert('You are not authenticated. Please log in again.');
        window.location.href = '/pages/login.html';
        return;
    }

    try {
        const response = await fetch('/profile', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify({ username: newUsername })
        });

        if (response.ok) {
            alert('Username changed successfully.');
            location.reload(); // Reload the page to reflect changes
        } else {
            const errorData = await response.json();
            alert(`Failed to change username: ${errorData.message || 'Unknown error'}`);
        }
    } catch (error) {
        console.error('Error changing username:', error);
        alert('An error occurred. Please try again later.');
    }
});

document.getElementById('change-password-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const newPassword = document.getElementById('new-password').value;
    const confirmNewPassword = document.getElementById('confirm-new-password').value;

    if (newPassword !== confirmNewPassword) {
        alert('New passwords do not match.');
        return;
    }

    const authToken = document.cookie.split(';').find(cookie => cookie.trim().startsWith('authToken='))
        ?.split('=')[1];

    if (!authToken) {
        alert('You are not authenticated. Please log in again.');
        window.location.href = '/pages/login.html';
        return;
    }

    try {
        const response = await fetch('/profile', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify({ newPassword }) // Send newPassword field
        });

        if (response.ok) {
            alert('Password changed successfully. You will be logged out.');
            document.cookie = 'authToken=; max-age=0; path=/'; // Clear auth token
            window.location.href = '/pages/login.html';
        } else {
            const errorData = await response.json();
            alert(`Failed to change password: ${errorData.message || 'Unknown error'}`);
        }
    } catch (error) {
        console.error('Error changing password:', error);
        alert('An error occurred. Please try again later.');
    }
});
