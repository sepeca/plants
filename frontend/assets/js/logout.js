function logout() {
    document.cookie = 'authToken=; max-age=0; path=/'; // Clear auth token
    window.location.href = './login.html'; // Redirect to login
}
