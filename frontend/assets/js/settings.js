// Import checkLogin.js functionality by including it in the HTML
checkLoginStatus();

function logout() {
    document.cookie = 'authToken=; max-age=0; path=/'; // Clear auth token
    document.cookie = 'username=; max-age=0; path=/'; // Clear username
    document.cookie = 'role=; max-age=0; path=/'; // Clear role
    window.location.href = '/pages/login.html';
}

document.getElementById('change-password-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const currentPassword = document.getElementById('current-password').value;
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
        logout();
        return;
    }

    try {
        const response = await fetch('/api/change_password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}` // Include auth token
            },
            body: JSON.stringify({ currentPassword, newPassword })
        });

        if (response.ok) {
            alert('Password changed successfully. You will be logged out.');
            logout();
        } else {
            const errorData = await response.json();
            alert(`Failed to change password: ${errorData.message || 'Unknown error'}`);
        }
    } catch (error) {
        console.error('Error changing password:', error);
        alert('An error occurred. Please try again later.');
    }
});
