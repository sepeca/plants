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
            const tasks = await response.json();
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
                    { data: 'taskDate' },
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
                pageLength: 15,
                scrollY: '400px',
                scrollCollapse: true,
                scroller: true
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

        if (detailContainer.is(':visible')) {
            detailContainer.hide();
            return;
        }

        detailContent.html(`
            <p><strong>Description:</strong> ${data.description || 'No description available.'}</p>
            <p><strong>Assigned Users:</strong></p>
            <ul>
                ${data.assignedUsers?.map(user => `<li>${user}</li>`).join('') || '<li>No users assigned.</li>'}
            </ul>
        `);

        detailContainer.show();
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
