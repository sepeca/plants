// Check if user is logged in
function checkLoginStatus() {
    const cookies = document.cookie.split(';').reduce((acc, cookie) => {
        const [key, value] = cookie.trim().split('=');
        acc[key] = value;
        return acc;
    }, {});

    if (!cookies.authToken) {
        window.location.href = '/index.html'; // Redirect to index if not logged in
    }
}
