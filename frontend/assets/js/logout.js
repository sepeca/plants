function logout() {
    document.cookie = 'jwt=; path=/; max-age=0'; // Clear the jwt cookie
    window.location.href = './login.html'; // Redirect to login
}
