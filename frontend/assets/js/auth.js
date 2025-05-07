export function checkAuthAndRedirect() {
    const token = localStorage.getItem('jwt');
    if (!token) {
        showNotification('Authentication token is missing. Please log in again.', false);
        window.location.href = './login.html';
        return false;
    }
    return token;
}
