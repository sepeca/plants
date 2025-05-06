import { SERVER_ADDRESS } from './config.js';

export async function logout() {
    const token = localStorage.getItem('jwt');
    if (!token) {
        window.location.href = './login.html';
        return;
    }

    try {
        const response = await fetch(`${SERVER_ADDRESS}/logout`, {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (response.ok) {
            localStorage.removeItem('jwt');
            window.location.href = './login.html';
        } else {
            showNotification('Logout failed.', false);
        }
    } catch (error) {
        console.error('Error during logout:', error);
        showNotification('An error occurred during logout.', false);
    }
}