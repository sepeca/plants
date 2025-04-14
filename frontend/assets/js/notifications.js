function showNotification(message, isPositive = true) {
    // Create notification container if it doesn't exist
    let notificationContainer = document.getElementById('notification-container');
    if (!notificationContainer) {
        notificationContainer = document.createElement('div');
        notificationContainer.id = 'notification-container';
        notificationContainer.style.position = 'fixed';
        notificationContainer.style.top = '20px';
        notificationContainer.style.left = '50%';
        notificationContainer.style.transform = 'translateX(-50%)';
        notificationContainer.style.zIndex = '1000';
        notificationContainer.style.maxWidth = '80%';
        notificationContainer.style.textAlign = 'center';
        document.body.appendChild(notificationContainer);
    }

    // Create notification element
    const notification = document.createElement('div');
    notification.textContent = message;
    notification.style.backgroundColor = isPositive ? '#4CAF50' : '#F44336'; // Green for positive, red for negative
    notification.style.color = 'white';
    notification.style.padding = '10px 20px';
    notification.style.margin = '5px 0';
    notification.style.borderRadius = '5px';
    notification.style.boxShadow = '0 4px 6px rgba(0, 0, 0, 0.2)';
    notification.style.fontSize = '1em';
    notification.style.fontWeight = 'bold';
    notification.style.opacity = '1';
    notification.style.transition = 'opacity 0.5s ease';

    // Append notification to container
    notificationContainer.appendChild(notification);

    // Remove notification after 5 seconds
    setTimeout(() => {
        notification.style.opacity = '0';
        setTimeout(() => notification.remove(), 500); // Wait for fade-out transition
    }, 5000);
}
