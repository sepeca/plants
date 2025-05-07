import { SERVER_ADDRESS } from './config.js';
import { checkAuthAndRedirect } from './auth.js';

$(document).ready(async function () {
    const token = checkAuthAndRedirect();
    if (!token) return;

    const plantsTableBody = document.querySelector('#plants-table tbody');

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
                    <tr data-plant-id="${plant.plantId}">
                        <td>${plant.platName}</td> <!-- Updated to use platName -->
                        <td>${plant.locationName}</td>
                        <td>${plant.categoryName}</td>
                        <td>
                            <button onclick="viewDetails(${plant.plantId})">Details</button>
                            <button onclick="createTask('${plant.platName}', this)">Create Task</button> <!-- Updated to use platName -->
                        </td>
                    </tr>
                `;
                plantsTableBody.insertAdjacentHTML('beforeend', row);
            });

            // Initialize DataTable
            $('#plants-table').DataTable();
        } catch (error) {
            console.error('Error fetching plants:', error.message);
        }
    }

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
            console.error('Error creating task:', error.message);
        }
    }

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
            console.error('Error creating plant:', error.message);
        }
    }

    async function fetchPlantDetails(plantId) {
        try {
            console.log('Fetching plant details for plantId:', plantId); // Debugging log
            const response = await fetch(`${SERVER_ADDRESS}/api/plant_detail`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({ plantId })
            });

            if (!response.ok) {
                console.error('Server responded with status:', response.status); // Log server response status
                throw new Error('Failed to fetch plant details');
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching plant details:', error.message);
            throw error;
        }
    }

    async function fetchCareHistory(plantId) {
        try {
            console.log('Fetching care history for plantId:', plantId); // Debugging log
            const response = await fetch(`${SERVER_ADDRESS}/api/careHistory`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({ plantId })
            });

            if (!response.ok) {
                console.error('Server responded with status:', response.status); // Log server response status
                throw new Error('Failed to fetch care history');
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching care history:', error.message);
            throw error;
        }
    }

    async function fetchCareTypes() {
        try {
            const response = await fetch(`${SERVER_ADDRESS}/api/care_types`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            if (!response.ok) throw new Error('Failed to fetch care types');
            return await response.json();
        } catch (error) {
            console.error('Error fetching care types:', error.message);
            throw error;
        }
    }

    async function submitCareHistory(plantId, careTypeId, notes, image) {
        try {
            const response = await fetch(`${SERVER_ADDRESS}/api/care_history`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({ plantId, careTypeId, notes, image })
            });
            if (!response.ok) throw new Error('Failed to create care history');
            alert('Care history entry created successfully!');
        } catch (error) {
            console.error('Error creating care history:', error.message);
        }
    }

    document.querySelector('#plant-add-form').addEventListener('submit', async (event) => {
        event.preventDefault();
        const plantName = document.querySelector('#plant-name').value.trim();
        const plantType = document.querySelector('#plant-type').value.trim();
        const plantLocation = document.querySelector('#plant-location').value.trim();
        const plantDescription = document.querySelector('#plant-description').value.trim();

        if (!plantName || !plantType || !plantLocation || !plantDescription) {
            alert('All fields must be filled in to create a new plant.');
            return;
        }

        await submitPlant(plantName, plantType, plantLocation, plantDescription);
    });

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
        const taskDetails = event.target.querySelector(`[name="task-details"]`).value.trim();
        const taskTime = event.target.querySelector(`[name="task-time"]`).value.trim();
        const taskEmails = event.target.querySelector(`[name="task-emails"]`).value.trim();
        const includeMe = event.target.querySelector(`[name="task-include-me"]`).checked;

        if (!taskDetails || !taskTime || !taskEmails) {
            alert('All fields must be filled in to create a new task.');
            return;
        }

        await submitTask(plantName, taskDetails, taskTime, taskEmails, includeMe);
        event.target.closest('tr').remove(); // Remove the task row after submission
    };

    fetchPlants();

    window.viewDetails = async function (plantId) {
        const parentRow = [...plantsTableBody.rows].find(row => row.dataset.plantId == plantId);
        let detailsRow = parentRow.nextElementSibling;

        if (detailsRow && detailsRow.classList.contains('details-row')) {
            detailsRow.remove();
        } else {
            try {
                const [plantDetails, careHistory, careTypes] = await Promise.all([
                    fetchPlantDetails(plantId),
                    fetchCareHistory(plantId),
                    fetchCareTypes()
                ]);

                detailsRow = document.createElement('tr');
                detailsRow.classList.add('details-row');
                detailsRow.innerHTML = `
                    <td colspan="4" style="background-color: #f9f9f9; padding: 15px;">
                        <h3>Plant Details</h3>
                        <p><strong>Name:</strong> ${plantDetails.plantName}</p>
                        <p><strong>Species:</strong> ${plantDetails.species}</p>
                        <p><strong>Humidity:</strong> ${plantDetails.humidity}</p>
                        <p><strong>Light Requirements:</strong> ${plantDetails.lightRequirements}</p>
                        <p><strong>Water:</strong> ${plantDetails.water}</p>
                        <p><strong>Temperature Range:</strong> ${plantDetails.temperatureRange}</p>
                        ${plantDetails.imageUrls?.length ? `<p><strong>Images:</strong> ${plantDetails.imageUrls.map(url => `<a href="${url}" target="_blank">View</a>`).join(', ')}</p>` : ''}
                        
                        <h3>Care History</h3>
                        <ul>
                            ${careHistory
                                .sort((a, b) => new Date(b.careDate) - new Date(a.careDate))
                                .map(history => `
                                    <li>
                                        <p><strong>Date:</strong> ${history.careDate.split('T')[0]}</p>
                                        <p><strong>Type:</strong> ${careTypes.find(type => type.careTypeId === history.careTypeId)?.name || 'Unknown'}</p>
                                        <p><strong>Notes:</strong> ${history.notes || 'No notes provided'}</p>
                                        <p><strong>User:</strong> ${history.userName} (${history.userEmail})</p>
                                        ${history.imageUrl ? `<p><strong>Image:</strong> <a href="${history.imageUrl}" target="_blank">View</a></p>` : ''}
                                    </li>
                                `).join('')}
                        </ul>

                        <h3>Add Care History</h3>
                        <form id="care-history-form-${plantId}" style="display: flex; flex-direction: column; gap: 10px;">
                            <label>Care Type:</label>
                            ${careTypes.map(type => `
                                <label>
                                    <input type="radio" name="careTypeId" value="${type.careTypeId}" required>
                                    ${type.name}
                                </label>
                            `).join('')}
                            <label for="notes-${plantId}">Notes:</label>
                            <textarea id="notes-${plantId}" name="notes" rows="3"></textarea>
                            <label for="image-${plantId}">Image URL:</label>
                            <input type="text" id="image-${plantId}" name="image">
                            <button type="submit" style="background-color: #6a994e; color: white; border: none; padding: 10px 20px; font-size: 1em; border-radius: 5px; cursor: pointer;">Submit</button>
                        </form>
                    </td>
                `;
                parentRow.insertAdjacentElement('afterend', detailsRow);

                document.querySelector(`#care-history-form-${plantId}`).addEventListener('submit', async function (event) {
                    event.preventDefault();
                    const careTypeId = parseInt(this.querySelector('input[name="careTypeId"]:checked').value);
                    const notes = this.querySelector(`#notes-${plantId}`).value.trim();
                    const image = this.querySelector(`#image-${plantId}`).value.trim();
                    await submitCareHistory(plantId, careTypeId, notes, image);
                    viewDetails(plantId); // Refresh details
                });
            } catch (error) {
                console.error('Error loading details:', error.message);
            }
        }
    };
});