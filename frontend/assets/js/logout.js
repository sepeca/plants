function logout() {
    document.cookie = 'jwt=; path=/; max-age=0';
    window.location.href = './login.html';
}