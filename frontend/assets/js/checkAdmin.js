async function checkAndRedirect() {
    try {
        const adminCheckResponse = await fetch('/api/check_admin');
        const response = await adminCheckResponse.json();

        if (response.status === "success") {
            window.location.href = '/pages/login.html';
        } else if (response.status === "failure") {
            window.location.href = '/pages/register.html';
        } else {
            console.error('Unexpected response:', response);
        }
    } catch (error) {
        console.error('Error during redirection checks:', error); // Handle fetch errors
    }
}

checkAndRedirect();
