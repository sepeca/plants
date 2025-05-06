function checkLoginStatus() {
    if (!document.cookie.includes('jwt=') || document.cookie.split('jwt=')[1].split(';')[0].trim() === '') {
        window.location.href = './login.html';
    }
}