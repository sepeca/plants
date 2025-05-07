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
                        <td>${plant.platName}</td> <!-- Correctly mapped to plant name -->
                        <td>${plant.categoryName}</td> <!-- Correctly mapped to plant type -->
                        <td>${plant.locationName}</td> <!-- Correctly mapped to location -->
                        <td>
                            <button onclick="viewDetails(${plant.plantId})">Details</button>
                            <button onclick="createTask('${plant.platName}', this)">Create Task</button>
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

    async function submitPlant(plantName, species, locationName, categoryName, humidity, lightRequirements, water, temperatureRange, imageUrls) {
        try {
            const response = await fetch(`${SERVER_ADDRESS}/api/create_plant`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({
                    plantName,
                    species,
                    locationName,
                    categoryName,
                    humidity,
                    lightRequirements,
                    water,
                    temperatureRange,
                    imageUrls
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
            const response = await fetch(`${SERVER_ADDRESS}/api/plant_detail/${plantId}`, {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${token}`
                }
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
            const response = await fetch(`${SERVER_ADDRESS}/api/care_history_by_plant/${plantId}`, {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${token}`
                }
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
        const species = document.querySelector('#species').value.trim();
        const locationName = document.querySelector('#location-name').value.trim();
        const categoryName = document.querySelector('#category-name').value.trim();
        const humidity = document.querySelector('#humidity').value.trim();
        const lightRequirements = document.querySelector('#light-requirements').value.trim();
        const water = document.querySelector('#water').value.trim();
        const temperatureRange = document.querySelector('#temperature-range').value.trim();
        const imageUrls = document.querySelector('#image-urls').value
            .split(',')
            .map(url => url.trim())
            .filter(url => url);

        if (!plantName || !species || !locationName || !categoryName || !humidity || !lightRequirements || !water || !temperatureRange || imageUrls.length === 0) {
            alert('All fields are required, and at least one image URL must be provided.');
            return;
        }

        await submitPlant(plantName, species, locationName, categoryName, humidity, lightRequirements, water, temperatureRange, imageUrls);
    });

    // Update the form HTML
    document.querySelector('#plant-add-form').innerHTML = `
        <label for="plant-name">Plant Name:</label>
        <input type="text" id="plant-name" name="plant-name" required>

        <label for="species">Species:</label>
        <input type="text" id="species" name="species" required>

        <label for="location-name">Location:</label>
        <input type="text" id="location-name" name="location-name" required>

        <label for="category-name">Category:</label>
        <input type="text" id="category-name" name="category-name" required>

        <label for="humidity">Humidity:</label>
        <input type="text" id="humidity" name="humidity" required>

        <label for="light-requirements">Light Requirements:</label>
        <input type="text" id="light-requirements" name="light-requirements" required>

        <label for="water">Water:</label>
        <input type="text" id="water" name="water" required>

        <label for="temperature-range">Temperature Range:</label>
        <input type="text" id="temperature-range" name="temperature-range" required>

        <label for="image-urls">Image URLs (comma-separated):</label>
        <textarea id="image-urls" name="image-urls" rows="3" required></textarea>

        <button type="submit" style="background-color: #6a994e; color: white; border: none; padding: 10px 20px; font-size: 1em; border-radius: 5px; cursor: pointer;">Add Plant</button>
    `;

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
        const modal = document.getElementById('plant-details-modal');
        const modalContent = document.getElementById('plant-details-content');

        try {
            console.log(`Fetching details for plantId: ${plantId}`); // Debugging log
            const [plantDetails, careHistory, careTypes] = await Promise.all([
                fetchPlantDetails(plantId),
                fetchCareHistory(plantId),
                fetchCareTypes()
            ]);

            console.log('Plant Details:', plantDetails); // Debugging log
            console.log('Care History:', careHistory); // Debugging log
            console.log('Care Types:', careTypes); // Debugging log

            modalContent.innerHTML = `
                <div class="left-plane">
                    <h3>Care History</h3>
                    <table class="care-history-table">
                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Type</th>
                                <th>Notes</th>
                                <th>User</th>
                                <th>Image</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${careHistory
                                .sort((a, b) => new Date(b.careDate) - new Date(a.careDate))
                                .map(history => `
                                    <tr>
                                        <td>${history.careDate.split('T')[0]}</td>
                                        <td>${history.careTypeName}</td>
                                        <td>${history.notes || 'No notes provided'}</td>
                                        <td>${history.userName} (${history.userEmail})</td>
                                        <td>${history.imageUrl ? `<a href="${history.imageUrl}" target="_blank">View</a>` : 'No image'}</td>
                                    </tr>
                                `).join('')}
                        </tbody>
                    </table>
                </div>
                <div class="right-plane">
                    <div class="plant-details-box">
                        <h3>Plant Details</h3>
                        <table class="plant-details-table">
                            <tr>
                                <td class="label">Name</td>
                                <td class="value">${plantDetails.plantName}</td>
                            </tr>
                            <tr>
                                <td class="label">Species</td>
                                <td class="value">${plantDetails.species}</td>
                            </tr>
                            <tr>
                                <td class="label">Humidity</td>
                                <td class="value">${plantDetails.humidity}</td>
                            </tr>
                            <tr>
                                <td class="label">Light Requirements</td>
                                <td class="value">${plantDetails.lightRequirements}</td>
                            </tr>
                            <tr>
                                <td class="label">Water</td>
                                <td class="value">${plantDetails.water}</td>
                            </tr>
                            <tr>
                                <td class="label">Temperature Range</td>
                                <td class="value">${plantDetails.temperatureRange}</td>
                            </tr>
                            ${plantDetails.imageUrls?.length ? `
                            <tr>
                                <td class="label">Images</td>
                                <td class="value">${plantDetails.imageUrls.map(url => `<a href="${url}" target="_blank">View</a>`).join(', ')}</td>
                            </tr>` : ''}
                        </table>
                    </div>
                    <div class="add-care-history-box">
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
                    </div>
                </div>
            `;

            modal.style.display = 'flex';

            document.querySelector(`#care-history-form-${plantId}`).addEventListener('submit', async function (event) {
                event.preventDefault();
                const careTypeId = parseInt(this.querySelector('input[name="careTypeId"]:checked').value);
                const notes = this.querySelector(`#notes-${plantId}`).value.trim();
                const image = this.querySelector(`#image-${plantId}`).value.trim();
                await submitCareHistory(plantId, careTypeId, notes, image);
                viewDetails(plantId); // Refresh details
            });

            window.onclick = (event) => {
                if (event.target === modal) {
                    modal.style.display = 'none';
                }
            };
        } catch (error) {
            console.error('Error loading details:', error.message);
        }
    };
});