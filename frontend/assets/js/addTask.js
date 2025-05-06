document.addEventListener('DOMContentLoaded', () => {
    const taskModal = document.getElementById('task-modal');
    const taskForm = document.getElementById('task-form');
    const closeTaskModal = document.getElementById('close-task-modal');

    // Open the modal when the "Create Task" button is clicked
    document.querySelectorAll('.create-task-btn').forEach(button => {
        button.addEventListener('click', () => {
            const plantId = button.dataset.plantId; // Get plant ID from button data attribute
            document.getElementById('task-plant-id').value = plantId; // Pre-fill plant ID
            taskModal.style.display = 'block';
        });
    });

    // Close the modal
    closeTaskModal.addEventListener('click', () => {
        taskModal.style.display = 'none';
    });

    // Handle form submission
    taskForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const plantId = document.getElementById('task-plant-id').value;
        const description = document.getElementById('task-description').value;
        const emails = document.getElementById('task-emails').value;
        const finishDaysInput = document.getElementById('task-finish-days').value;
        const finishDate = parseInt(finishDaysInput, 10);
        const me = document.getElementById('task-include-me').checked;

        if (!plantId || !description || !emails || isNaN(finishDate) || finishDate <= 0) {
            alert('All fields are required and must be valid.');
            return;
        }

        const token = document.cookie.split(';').find(cookie => cookie.trim().startsWith('authToken='))
            ?.split('=')[1];

        if (!token) {
            alert('Authentication token is missing. Please log in again.');
            window.location.href = '/pages/login.html';
            return;
        }

        try {
            const response = await fetch('/api/create_task', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ plantId, description, emails, finishDate, me })
            });

            if (response.ok) {
                alert('Task created successfully.');
                taskModal.style.display = 'none'; // Close the modal
                location.reload(); // Reload the page to reflect changes
            } else {
                const errorData = await response.json();
                alert(`Failed to create task: ${errorData.message || 'Unknown error'}`);
            }
        } catch (error) {
            console.error('Error creating task:', error);
            alert('An error occurred. Please try again later.');
        }
    });
});
