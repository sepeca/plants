function checkLoginStatus() {
    if (!localStorage.getItem('jwt') || localStorage.getItem('jwt').trim() === '') {
        showNotification('Please log in and try again.', false);
        window.location.href = './login.html';
    }
}