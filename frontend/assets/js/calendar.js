import { SERVER_ADDRESS } from './config.js';

$(document).ready(async function () {
    const token = localStorage.getItem('jwt');

    if (!token) {
        console.error('Authentication token is missing. Please log in again.');
        window.location.href = './login.html';
        return;
    }

    try {
        const response = await fetch(`${SERVER_ADDRESS}/api/get_tasks`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (response.ok) {
            let tasks = await response.json();
            console.log('Tasks response:', tasks); // Log the response for debugging

            // Ensure all tasks have 'type' and 'location' properties
            tasks = tasks.map(task => {
                if (!task.type) {
                    console.warn(`Task with ID ${task.id} is missing the 'type' property.`);
                }
                if (!task.location) {
                    console.warn(`Task with ID ${task.id} is missing the 'location' property.`);
                }
                return {
                    ...task,
                    type: task.type || 'Unknown', // Default value for 'type'
                    location: task.location || 'Unknown' // Default value for 'location'
                };
            });

            const table = $('#plant-tasks').DataTable({
                data: tasks,
                columns: [
                    {
                        data: null,
                        render: function (data, type, row) {
                            return `<input type="checkbox" class="task-select" data-id="${row.id}">`;
                        },
                        orderable: false
                    },
                    {
                        data: 'taskDate',
                        render: function (data, type, row) {
                            if (type === 'display' || type === 'filter') {
                                return data.split('T')[0]; // Extract only the date part (YYYY-MM-DD)
                            }
                            return data; // Return original data for other types
                        }
                    },
                    { data: 'plantName' },
                    { data: 'type' },
                    { data: 'location' },
                    {
                        data: null,
                        render: function (data, type, row) {
                            return `<button class="details-btn" data-id="${row.id}">Details</button>`;
                        }
                    }
                ],
                createdRow: function (row, data) {
                    const taskDate = new Date(data.taskDate);
                    const today = new Date();
                    const isPast = taskDate < today && taskDate.toDateString() !== today.toDateString();
                    if (isPast) {
                        $(row).css({
                            'background-color': 'red',
                            'color': 'white' // Set text color to white for better readability
                        });
                    }
                },
                pageLength: 15,
                scrollY: '400px',
                scrollCollapse: true,
                scroller: true,
                order: [[1, 'asc']] // Sort by the "Task Date" column (index 1) in ascending order
            });

            $('#plant-tasks tbody').on('click', '.details-btn', function () {
                const rowData = table.row($(this).parents('tr')).data();
                showTaskDetails(rowData);
            });
        } else {
            console.error('Failed to fetch tasks. Please try again.');
        }
    } catch (error) {
        console.error('Error fetching tasks:', error);
        console.error('An error occurred. Please try again later.');
    }

    function showTaskDetails(data) {
        const detailContainer = $('.task-detail-container');
        const detailContent = $('#task-detail-content');

        // Check if the clicked task is already displayed
        if (detailContainer.is(':visible') && detailContent.data('taskId') === data.id) {
            detailContainer.hide(); // Hide the container if the same task is clicked again
            detailContent.removeData('taskId'); // Clear the stored task ID
            return;
        }

        // Populate and show the container for the new task
        detailContent.html(`
            <p><strong>Description:</strong> ${data.description || 'No description available.'}</p>
            <p><strong>Assigned Users:</strong></p>
            <ul>
                ${data.assignedUsers?.map(user => `<li>${user}</li>`).join('') || '<li>No users assigned.</li>'}
            </ul>
        `).data('taskId', data.id); // Store the task ID in the content for comparison

        detailContainer.show();

        // Add event listener to close when clicking outside
        $(document).off('click', closeOnOutsideClick); // Remove any existing event listener
        $(document).on('click', closeOnOutsideClick);

        $('html, body').animate({ scrollTop: detailContainer.offset().top }, 'slow');
    }

    $('#finish-tasks-btn').on('click', async function () {
        const selectedTasks = $('.task-select:checked').map(function () {
            return $(this).data('id');
        }).get();

        if (selectedTasks.length === 0) {
            console.error('No tasks selected.');
            return;
        }

        try {
            const response = await fetch(`${SERVER_ADDRESS}/api/finish_tasks`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify({ taskIds: selectedTasks })
            });

            if (response.ok) {
                console.log('Tasks marked as finished.');
                $('#plant-tasks').DataTable().clear().rows.add([]).draw(); // Optionally reload data
            } else {
                console.error('Failed to finish tasks. Please try again.');
            }
        } catch (error) {
            console.error('Error finishing tasks:', error);
        }
    });


});
