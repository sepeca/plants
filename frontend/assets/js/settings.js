import { SERVER_ADDRESS } from './config.js';
import { checkAuthAndRedirect } from './auth.js';

$(document).ready(async function () {
    const token = checkAuthAndRedirect();
    if (!token) return;

    document.getElementById('change-username-form').addEventListener('submit', async function (event) {
        event.preventDefault();
        const newUsername = document.getElementById('new-username').value;

        if (!newUsername) {
            showNotification('Username cannot be empty.', false);
            return;
        }

        try {
            const response = await fetch(`${SERVER_ADDRESS}/profile`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ username: newUsername })
            });

            if (response.ok) {
                showNotification('Username changed successfully.', true);
                location.reload();
            } else {
                const errorData = await response.json();
                showNotification(`Failed to change username: ${errorData.message || 'Unknown error'}`, false);
            }
        } catch (error) {
            showNotification('An error occurred. Please try again later.', false);
        }
    });

    document.getElementById('change-password-form').addEventListener('submit', async function (event) {
        event.preventDefault();
        const newPassword = document.getElementById('new-password').value;
        const confirmNewPassword = document.getElementById('confirm-new-password').value;

        if (newPassword !== confirmNewPassword) {
            showNotification('New passwords do not match.', false);
            return;
        }

        try {
            const response = await fetch(`${SERVER_ADDRESS}/profile`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ newPassword })
            });

            if (response.ok) {
                showNotification('Password changed successfully. You will be logged out.', true);
                localStorage.removeItem('jwt');
                window.location.href = './login.html';
            } else {
                const errorData = await response.json();
                showNotification(`Failed to change password: ${errorData.message || 'Unknown error'}`, false);
            }
        } catch (error) {
            showNotification('An error occurred. Please try again later.', false);
        }
    });
});
