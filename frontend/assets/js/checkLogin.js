function checkLoginStatus() {
    if (!document.cookie.includes('jwt=') || document.cookie.split('jwt=')[1].split(';')[0].trim() === '') {
        showNotification('Please log in and try again.', false);
        //window.location.href = './login.html';
    }
}