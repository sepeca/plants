import { SERVER_ADDRESS } from './config.js';
import { checkAuthAndRedirect } from './auth.js';

$(document).ready(async function () {
    const token = checkAuthAndRedirect();
    if (!token) return;

    try {
        const response = await fetch(`${SERVER_ADDRESS}/api/get_tasks`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        });

        if (response.ok) {
            let tasks = await response.json();

            // Filter new tasks (notified === false)
            const newTasks = tasks.filter(task => task.notified === false);

            if (newTasks.length > 0) {
                showNewTasksModal(newTasks, token);
            }

            tasks = tasks.map(task => {
                console.log('Task before mapping:', task); // Debug log to inspect task data
                task.plantSpecies = task.plantSpecies;
                task.locationName = task.locationName;
                task.id = task.id || task.taskId; // Map taskId to id if id is undefined
                console.log('Task after mapping:', task); // Debug log to verify task.id is set
                return task;
            });

            tasks = tasks
                .filter(task => !task.completed) // Filter uncompleted tasks first
                .concat(tasks.filter(task => task.completed)) // Append completed tasks
                .sort((a, b) => new Date(a.taskDate) - new Date(b.taskDate)); // Sort by date

            const table = $('#plant-tasks').DataTable({
                data: tasks,
                columns: [
                    {
                        data: null,
                        render: function (data, type, row) {
                            console.log('Row data:', row); // Debug log to inspect row data
                            console.log('Row ID:', row.id); // Debug log to ensure row.id is correct
                            return `<input type="checkbox" class="task-select" data-id="${row.id}">`; // Ensure data-id uses the correct id
                        },
                        orderable: false
                    },
                    {
                        data: 'taskDate',
                        render: function (data, type, row) {
                            if (type === 'display' || type === 'filter') {
                                return data.split('T')[0];
                            }
                            return data;
                        }
                    },
                    { data: 'plantName' },
                    { data: 'plantSpecies' },
                    { data: 'locationName' },
                    {
                        data: 'completed',
                        render: function (data, type, row) {
                            return data ? 'Completed' : 'Pending'; // Display "Completed" or "Pending"
                        }
                    },
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
                    const yesterday = new Date();
                    yesterday.setDate(today.getDate() - 1);

                    const isTodayOrYesterday =
                        taskDate.toDateString() === today.toDateString() ||
                        taskDate.toDateString() === yesterday.toDateString();

                    if (data.completed) {
                        $(row).css({
                            'background-color': 'green',
                            'color': 'white'
                        });
                    } else if (isTodayOrYesterday) {
                        $(row).css({
                            'background-color': 'yellow',
                            'color': 'black'
                        });
                    } else if (taskDate < today && taskDate.toDateString() !== today.toDateString()) {
                        $(row).css({
                            'background-color': 'red',
                            'color': 'white'
                        });
                    }
                },
                pageLength: 15,
                scrollY: '400px',
                scrollCollapse: true,
                scroller: true,
                order: [[1, 'asc']],
                initComplete: function () {
                    const table = this.api();

                    // Add a dropdown filter for the "Task Date" column
                    const dateFilter = $('<select><option value="">All Dates</option><option value="Today">Today</option><option value="Yesterday">Yesterday</option><option value="Past">Past</option></select>')
                        .appendTo($('#plant-tasks_filter'))
                        .on('change', function () {
                            const val = $(this).val();
                            const today = new Date();
                            const yesterday = new Date();
                            yesterday.setDate(today.getDate() - 1);

                            table.column(1).search('', false, false).draw(); // Clear previous search
                            if (val === 'Today') {
                                table.column(1).search(today.toISOString().split('T')[0], true, false).draw();
                            } else if (val === 'Yesterday') {
                                table.column(1).search(yesterday.toISOString().split('T')[0], true, false).draw();
                            } else if (val === 'Past') {
                                table.rows().every(function () {
                                    const data = this.data();
                                    const taskDate = new Date(data.taskDate);
                                    if (taskDate < today && taskDate.toDateString() !== today.toDateString()) {
                                        $(this.node()).show();
                                    } else {
                                        $(this.node()).hide();
                                    }
                                });
                            }
                        });

                    // Add a dropdown filter for the "Status" column
                    const statusFilter = $('<select><option value="">All Statuses</option><option value="Completed">Completed</option><option value="Pending" selected>Pending</option></select>')
                        .appendTo($('#plant-tasks_filter'))
                        .on('change', function () {
                            const val = $(this).val();
                            table.column(5).search(val).draw(); // Filter the "Status" column (index 5)
                        });

                    // Trigger the default filter for "Pending"
                    table.column(5).search('Pending').draw();
                }
            });

            $('#plant-tasks tbody').on('click', '.details-btn', function () { // Fixed selector typo
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

    // Function to display the modal with new tasks
    function showNewTasksModal(newTasks, token) {
        const modalHtml = `
            <div id="new-tasks-modal" style="position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.8); color: white; display: flex; flex-direction: column; justify-content: center; align-items: center; z-index: 1000;">
                <h1 style="color: red;">There are ${newTasks.length} new tasks:</h1>
                <ul style="list-style: none; padding: 0; text-align: left;">
                    ${newTasks.map(task => `
                        <li>
                            <strong>${task.plantName}</strong> - ${task.taskDate.split('T')[0]}<br>
                            ${task.taskDescription || 'No description available.'}
                        </li>
                    `).join('')}
                </ul>
                <button id="notify-tasks-btn" style="margin-top: 20px; padding: 10px 20px; background: green; color: white; border: none; cursor: pointer;">Acknowledge</button>
            </div>
        `;

        $('body').append(modalHtml);

        $('#notify-tasks-btn').on('click', async function () {
            const taskIds = newTasks.map(task => task.id);

            try {
                const response = await fetch(`${SERVER_ADDRESS}/api/notify_task`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + token
                    },
                    body: JSON.stringify({ task_ids: taskIds })
                });

                if (response.ok) {
                    $('#new-tasks-modal').remove(); // Hide the modal on success
                } else {
                    console.error('Failed to notify tasks. Please try again.');
                }
            } catch (error) {
                console.error('Error notifying tasks:', error);
            }
        });
    }

    function showTaskDetails(data) {
        const detailContainer = $('.task-detail-container');
        const detailContent = $('#task-detail-content');

        // Ensure the container exists and is properly initialized
        if (detailContainer.length === 0 || detailContent.length === 0) {
            console.error('Task detail container or content element is missing.');
            return;
        }

        // Check if the clicked task is already displayed
        if (detailContainer.is(':visible') && detailContent.data('taskId') === data.id) {
            detailContainer.hide(); // Hide the container if the same task is clicked again
            detailContent.removeData('taskId'); // Clear the stored task ID
            return;
        }

        // Populate and show the container for the new task
        detailContent.html(`
            <p><strong>Description:</strong> ${data.taskDescription || 'No description available.'}</p> <!-- Updated to use taskDescription -->
            <p><strong>Assigned Users:</strong></p>
            <ul>
                ${data.assignedUsers?.map(user => `<li>${user}</li>`).join('') || '<li>No users assigned.</li>'}
            </ul>
        `).data('taskId', data.id); // Store the task ID in the content for comparison

        detailContainer.show();

        $('html, body').animate({ scrollTop: detailContainer.offset().top }, 'slow');
    }

    $('#plant-tasks').on('change', '.task-select', function () {
        const selectedTasks = $('.task-select:checked').map(function () {
            const taskId = $(this).data('id');
            console.log('Checkbox data-id:', taskId); // Debug log for each checkbox
            return taskId;
        }).get();

        console.log('Currently selected task IDs:', selectedTasks); // Log the selected task IDs
    });

    $('#finish-tasks-btn').on('click', async function () {
        const selectedTasks = $('.task-select:checked').map(function () {
            const taskId = $(this).data('id');
            if (!taskId) {
                console.error('Task ID is undefined for a selected task.');
            }
            return taskId;
        }).get();

        if (selectedTasks.length === 0 || selectedTasks.includes(undefined)) {
            console.error('No valid tasks selected.');
            return;
        }

        console.log('Selected task IDs to finish:', selectedTasks); // Log the task IDs

        try {
            const response = await fetch(`${SERVER_ADDRESS}/api/finish_tasks`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify({ task_ids: selectedTasks }) // Send task_ids as an array
            });

            if (response.ok) {
                console.log('Tasks marked as finished.');
                location.reload(); // Refresh the page after finishing tasks
            } else {
                console.error('Failed to finish tasks. Please try again.');
            }
        } catch (error) {
            console.error('Error finishing tasks:', error);
        }
    });

});
