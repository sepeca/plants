// Check if user is logged in
function checkLoginStatus() {
    if (!document.cookie.includes('authToken=')) {
        window.location.href = './pages/login.html'; // Redirect to login if not logged in
    }
}
