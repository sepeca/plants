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
                            <button onclick="deletePlant(${plant.plantId}, this)">Delete</button> <!-- New Delete button -->
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

    async function submitTask(plantId, taskDetails, taskTime, taskEmails, includeMe) {
        try {
            const body = {
                plantId, // Include plantId in the request body
                description: taskDetails,
                time: taskTime,
                me: includeMe,
            };
            if (taskEmails.length > 0) {
                body.emails = taskEmails; // Only include emails if not empty
            }

            const response = await fetch(`${SERVER_ADDRESS}/api/create_task`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify(body)
            });
            if (!response.ok) throw new Error('Failed to create task');
            alert('Task created successfully!');
        } catch (error) {
            console.error('Error creating task:', error.message);
        }
    }

    async function submitPlant(plantName, species, locationName, categoryName, humidity, lightRequirements, water, temperatureRange, images) {
        try {
            if (images.length > 3) {
                showNotification('You can upload a maximum of 3 images.', false);
                return;
            }

            const formData = new FormData();
            const plantData = {
                plantName,
                species,
                locationName,
                categoryName,
                humidity,
                lightRequirements,
                water,
                temperatureRange
            };

            const jsonBlob = new Blob(
                [JSON.stringify(plantData)],
                { type: 'application/json' }
            );

            formData.append('plantData', jsonBlob); // Append JSON blob for plant data

            images.forEach((image) => {
                formData.append(`plantImages`, image); // Append each image file
            });

            console.log('Submitting plant data:', {
                plantData,
                plantImages: images.map(image => image.name) // Log image names for debugging
            });

            const response = await fetch(`${SERVER_ADDRESS}/api/add_plant`, {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${token}`
                },
                body: formData
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
            if (image && image.length > 1) {
                alert('You can upload a maximum of 1 image for care history.');
                return;
            }

            const careData = {
                plantId,
                careTypeId,
                notes
            };

            const formData = new FormData();
            const jsonBlob = new Blob(
                [JSON.stringify(careData)],
                { type: 'application/json' }
            );

            formData.append('careData', jsonBlob); // Append JSON blob for care data

            if (image) {
                formData.append('careImage', image); // Append the image file
            }

            console.log('Submitting care history data:', {
                careData: JSON.stringify(careData), // Log careData as JSON
                careImage: image ? image.name : null // Log image file name if present
            });

            const response = await fetch(`${SERVER_ADDRESS}/api/create_care_history`, {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${token}`
                },
                body: formData
            });
            if (!response.ok) throw new Error('Failed to create care history');
            alert('Care history entry created successfully!');
        } catch (error) {
            console.error('Error creating care history:', error.message);
        }
    }

    window.deletePlant = async function(plantId, button) {
        if (!confirm('Do you want to proceed with action: Removing this plant?')) return;
        if (!confirm('Last chance. Are you sure?')) return;

        try {
            console.log('Data being sent for deletion:', { plantId }); // Log the data being sent
            const response = await fetch(`${SERVER_ADDRESS}/api/delete_plant`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({ plantId: parseInt(plantId, 10) }) // Ensure plantId is sent as an integer
            });

            if (response.ok) {
                alert('Plant deleted successfully!');
                button.closest('tr').remove(); // Remove the plant row from the table
            } else {
                const errorData = await response.json();
                alert(`Failed to delete plant: ${errorData.message || 'Unknown error'}`);
            }
        } catch (error) {
            console.error('Error deleting plant:', error.message);
            alert('An error occurred. Please try again later.');
        }
    };

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
        const images = Array.from(document.querySelector('#images').files); // Get image files

        if (!plantName || !species || !locationName || !categoryName || !humidity || !lightRequirements || !water || !temperatureRange) {
            alert('All fields except Images are required.');
            return;
        }

        await submitPlant(plantName, species, locationName, categoryName, humidity, lightRequirements, water, temperatureRange, images);
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

        <label for="images">Images:</label>
        <input type="file" id="images" name="images" multiple>

        <button type="submit" style="background-color: #6a994e; color: white; border: none; padding: 10px 20px; font-size: 1em; border-radius: 5px; cursor: pointer;">Add Plant</button>
    `;

    document.querySelector('#plant-add-form').style.gap = '5px'; // Set form gap to 5px
    document.querySelector('.main-container').style.marginTop = '100px'; // Set main-container margin-top to 100px
    document.querySelector('.table-container').style.width = '650px'; // Set table-container width to 1000px

    document.querySelectorAll('#plants-table button').forEach(button => {
        button.style.padding = '5px 10px'; // Make action buttons smaller
        button.style.fontSize = '0.9em'; // Reduce font size for buttons
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
                    <form style="display: flex; flex-direction: column; gap: 10px; margin-top: 10px;" onsubmit="handleTaskSubmit(event, '${parentRow.dataset.plantId}')">
                        <label for="task-details-${parentRow.dataset.plantId}">Description:</label>
                        <textarea id="task-details-${parentRow.dataset.plantId}" name="task-details" rows="3" required></textarea>
                        
                        <label for="task-time-${parentRow.dataset.plantId}">Time (in days):</label>
                        <input type="number" id="task-time-${parentRow.dataset.plantId}" name="task-time" min="1" required>
                        
                        <label for="task-emails-${parentRow.dataset.plantId}">Emails (comma separated):</label>
                        <input type="text" id="task-emails-${parentRow.dataset.plantId}" name="task-emails">
                        
                        <label>
                            <input type="checkbox" id="task-include-me-${parentRow.dataset.plantId}" name="task-include-me">
                            Include Me
                        </label>
                        
                        <button type="submit" style="background-color: #6a994e; color: white; border: none; padding: 10px 20px; font-size: 1em; border-radius: 5px; cursor: pointer;">Create Task</button>
                    </form>
                </td>
            `;
            parentRow.insertAdjacentElement('afterend', taskRow);
        }
    };

    window.handleTaskSubmit = async function(event, plantId) {
        event.preventDefault();
        const taskDetails = event.target.querySelector(`[name="task-details"]`).value.trim();
        const taskTime = event.target.querySelector(`[name="task-time"]`).value.trim();
        const taskEmails = event.target.querySelector(`[name="task-emails"]`).value
            .split(',')
            .map(email => email.trim())
            .filter(email => email); // Filter out empty emails
        const includeMe = event.target.querySelector(`[name="task-include-me"]`).checked;

        if (!taskDetails || !taskTime) {
            alert('Task details and time are required.');
            return;
        }

        await submitTask(plantId, taskDetails, taskTime, taskEmails, includeMe);
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
                                <th>Action</th> <!-- New column for actions -->
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
                                        <td>
                                            ${history.imageUrl ? `<button onclick="showCareHistoryImage('${history.imageUrl}', this)">Show Image</button>` : 'No image'}
                                        </td>
                                        <td>
                                            <button onclick="deleteCareHistory(${history.careHistoryId}, this)">DEL</button> <!-- DEL button -->
                                        </td>
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
                                <td class="value">
                                    ${plantDetails.imageUrls.map(url => `<button onclick="showPlantImage('${url}', this)">Show Image</button>`).join(' ')}
                                </td>
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
                            <label for="image-${plantId}">Image:</label>
                            <input type="file" id="image-${plantId}" name="image" accept="image/*">
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
                const image = this.querySelector(`#image-${plantId}`).files[0]; // Get the selected image file
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

    window.showPlantImage = async function (url, button) {
        const parentRow = button.closest('tr');
        let imageRow = parentRow.nextElementSibling;

        if (imageRow && imageRow.classList.contains('image-row')) {
            imageRow.remove();
        } else {
            imageRow = document.createElement('tr');
            imageRow.classList.add('image-row');
            imageRow.innerHTML = `
                <td colspan="2">
                    <img src="${url}" style="max-width: 350px; height: auto;" alt="Plant Image">
                </td>
            `;
            parentRow.insertAdjacentElement('afterend', imageRow);
        }
    };

    window.showCareHistoryImage = async function (url, button) {
        const parentRow = button.closest('tr');
        let imageRow = parentRow.nextElementSibling;

        if (imageRow && imageRow.classList.contains('image-row')) {
            imageRow.remove();
        } else {
            imageRow = document.createElement('tr');
            imageRow.classList.add('image-row');
            imageRow.innerHTML = `
                <td colspan="6">
                    <img src="${url}" style="max-width: 350px; height: auto;" alt="Care History Image">
                </td>
            `;
            parentRow.insertAdjacentElement('afterend', imageRow);
        }
    };

    window.deleteCareHistory = async function(careHistoryId, button) {
        if (!confirm('Are you sure you want to delete this care history entry?')) return;

        try {
            const response = await fetch(`${SERVER_ADDRESS}/api/care_history_delete`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({ careHistoryId })
            });

            if (response.ok) {
                alert('Care history entry deleted successfully!');
                button.closest('tr').remove(); // Remove the care history row from the table
            } else {
                const errorData = await response.json();
                alert(`Failed to delete care history: ${errorData.message || 'Unknown error'}`);
            }
        } catch (error) {
            console.error('Error deleting care history:', error.message);
            alert('An error occurred. Please try again later.');
        }
    };
});
