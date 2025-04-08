const isLoggedIn = document.cookie.split('; ').some(cookie => cookie.startsWith('loginToken='));

if (isLoggedIn) {
    window.location.href = './pages/dashboard.html';
} else {
    window.location.href = './pages/login.html';
}
