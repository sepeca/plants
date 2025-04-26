async function checkAndRedirect() {
    try {
        const adminCheckResponse = await fetch('/api/check-admin');
        const response = await adminCheckResponse.json();

        // Check the status and redirect accordingly
        if (response.status === "success") {
            window.location.href = '/pages/login.html'; // Redirect to login if admin exists
        } else if (response.status === "failure") {
            window.location.href = '/pages/register.html'; // Redirect to register if no admin exists
        }
    } catch (error) {
        console.error('Error during redirection checks:', error); // Handle fetch errors
    }
}

checkAndRedirect();
