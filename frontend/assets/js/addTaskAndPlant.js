import { SERVER_ADDRESS } from './config.js';

const token = localStorage.getItem('jwt');

if (!token) {
    console.error('Authentication token is missing. Please log in again.');
    window.location.href = './login.html';
}

const plantsTableBody = document.querySelector('#plants-table tbody');

// Fetch plants from the API and populate the table
async function fetchPlants() {
    try {
        const response = await fetch(`${SERVER_ADDRESS}/api/get_plants`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        if (!response.ok) throw new Error('Failed to fetch plants');
        
        const plants = await response.json();
        console.log('API Response:', plants); // Log the API response to the console

        plants.forEach(plant => {
            const row = `
                <tr>
                    <td>${plant.name}</td>
                    <td>${plant.type}</td>
                    <td>${plant.location}</td>
                    <td>
                        <button onclick="viewDetails('${plant.name}')">Details</button>
                        <button onclick="createTask('${plant.name}', this)">Create Task</button>
                    </td>
                </tr>
            `;
            plantsTableBody.insertAdjacentHTML('beforeend', row);
        });

        // Initialize DataTable
        $('#plants-table').DataTable();
    } catch (error) {
        console.error(error.message);
    }
}

// Submit a new task to the API
async function submitTask(plantName, taskDetails, taskTime, taskEmails, includeMe) {
    try {
        const response = await fetch(`${SERVER_ADDRESS}/api/create_task`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({
                plantName,
                details: taskDetails,
                time: taskTime,
                emails: taskEmails,
                includeMe
            })
        });
        if (!response.ok) throw new Error('Failed to create task');
        alert('Task created successfully!');
    } catch (error) {
        console.error(error.message);
    }
}

// Submit a new plant to the API
async function submitPlant(plantName, plantType, plantLocation, plantDescription) {
    try {
        const response = await fetch(`${SERVER_ADDRESS}/api/create_plant`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify({
                name: plantName,
                type: plantType,
                location: plantLocation,
                description: plantDescription
            })
        });
        if (!response.ok) throw new Error('Failed to create plant');
        alert('Plant created successfully!');
        location.reload(); // Reload the page to update the table
    } catch (error) {
        console.error(error.message);
    }
}

// Handle plant creation form submission
document.querySelector('#plant-add-form').addEventListener('submit', async (event) => {
    event.preventDefault();
    const plantName = document.querySelector('#plant-name').value;
    const plantType = document.querySelector('#plant-type').value;
    const plantLocation = document.querySelector('#plant-location').value;
    const plantDescription = document.querySelector('#plant-description').value;

    await submitPlant(plantName, plantType, plantLocation, plantDescription);
});

// Handle task creation form submission
window.createTask = function(plantName, button) {
    const parentRow = button.closest('tr');
    let taskRow = parentRow.nextElementSibling;

    if (taskRow && taskRow.classList.contains('task-row')) {
        taskRow.remove();
    } else {
        taskRow = document.createElement('tr');
        taskRow.classList.add('task-row');
        taskRow.innerHTML = `
            <td colspan="4">
                <form style="display: flex; flex-direction: column; gap: 10px; margin-top: 10px;" onsubmit="handleTaskSubmit(event, '${plantName}')">
                    <label for="task-details-${plantName}">Details:</label>
                    <textarea id="task-details-${plantName}" name="task-details" rows="3" required></textarea>
                    
                    <label for="task-time-${plantName}">Time (in days):</label>
                    <input type="number" id="task-time-${plantName}" name="task-time" min="1" required>
                    
                    <label for="task-emails-${plantName}">Emails (comma separated):</label>
                    <input type="text" id="task-emails-${plantName}" name="task-emails" required>
                    
                    <label>
                        <input type="checkbox" id="task-include-me-${plantName}" name="task-include-me">
                        Include Me
                    </label>
                    
                    <button type="submit" style="background-color: #6a994e; color: white; border: none; padding: 10px 20px; font-size: 1em; border-radius: 5px; cursor: pointer;">Create Task</button>
                </form>
            </td>
        `;
        parentRow.insertAdjacentElement('afterend', taskRow);
    }
};

window.handleTaskSubmit = async function(event, plantName) {
    event.preventDefault();
    const taskDetails = event.target.querySelector(`[name="task-details"]`).value;
    const taskTime = event.target.querySelector(`[name="task-time"]`).value;
    const taskEmails = event.target.querySelector(`[name="task-emails"]`).value;
    const includeMe = event.target.querySelector(`[name="task-include-me"]`).checked;

    await submitTask(plantName, taskDetails, taskTime, taskEmails, includeMe);
    event.target.closest('tr').remove(); // Remove the task row after submission
};

// Fetch plants on page load
fetchPlants();

window.viewDetails = function(plantName) {
    const parentRow = [...plantsTableBody.rows].find(row => row.cells[0].textContent === plantName);
    let detailsRow = parentRow.nextElementSibling;

    // Check if the next row is already a details row
    if (detailsRow && detailsRow.classList.contains('details-row')) {
        detailsRow.remove(); // Remove the details row
    } else {
        // Create a new details row
        detailsRow = document.createElement('tr');
        detailsRow.classList.add('details-row');
        detailsRow.innerHTML = `
            <td colspan="4" style="background-color: #f9f9f9; padding: 15px;">
                <p><strong>Description:</strong> This is a placeholder description for ${plantName}.</p>
                <p><strong>Assigned Users:</strong> user1@example.com, user2@example.com</p>
            </td>
        `;
        parentRow.insertAdjacentElement('afterend', detailsRow);
    }
};
