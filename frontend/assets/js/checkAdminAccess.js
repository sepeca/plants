function checkAdminAccess() {
    const cookies = document.cookie.split(';').reduce((acc, cookie) => {
        const [key, value] = cookie.trim().split('=');
        acc[key] = value;
        return acc;
    }, {});

    if (cookies.role !== 'admin') {
        window.location.href = '/pages/calendar.html'; // Redirect non-admin users
    }
}

// Automatically execute the function when the script is loaded
checkAdminAccess();
