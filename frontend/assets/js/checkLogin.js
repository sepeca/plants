// Check if user is logged in
function checkLoginStatus() {
    if (!document.cookie.includes('jwt=')) {
        window.location.href = './login.html'; // Redirect to login if not logged in
    }
}
