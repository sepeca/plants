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

            tasks = tasks.map(task => {
                task.plantSpecies = task.plantSpecies;
                task.locationName = task.locationName;
                return task;
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
                                return data.split('T')[0];
                            }
                            return data;
                        }
                    },
                    { data: 'plantName' },
                    { data: 'plantSpecies' },
                    { data: 'locationName' },
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
                            'color': 'white'
                        });
                    }
                },
                pageLength: 15,
                scrollY: '400px',
                scrollCollapse: true,
                scroller: true,
                order: [[1, 'asc']]
            });

            $('#plant-ttasks tbody').on('click', '.details-btn', function () {
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
