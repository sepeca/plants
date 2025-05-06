// Import checkLogin.js functionality by including it in the HTML
checkLoginStatus();

$(document).ready(async function () {
    const jwt = document.cookie
        .split('; ')
        .find(cookie => cookie.startsWith('jwt='))
        ?.split('=')[1];

    if (!jwt) {
        alert('Authentication token is missing. Please log in again.');
        //window.location.href = './login.html';
        return;
    }

    console.log('Token:', jwt); // Log the token to the console

    try {
        const response = await fetch('http://192.168.192.1:8081/api/get_tasks', {
            method: 'GET',
            credentials: 'include', // Automatically include cookies
headers: {
                'Authorization': `Bearer ${jwt}`
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

            // Handle row click for additional details
            $('#plant-tasks tbody').on('click', '.details-btn', function () {
                const rowId = $(this).data('id');
                const rowData = table.row($(this).parents('tr')).data(); // Get row data

                showTaskDetails(rowData); // Pass row data to the details view
            });
        } else {
            alert('Failed to fetch tasks. Please try again.');
        }
    } catch (error) {
        console.error('Error fetching tasks:', error);
        alert('An error occurred. Please try again later.');
    }

    // Show task details in the detail container
    function showTaskDetails(data) {
        const detailContainer = $('.task-detail-container');
        const detailContent = $('#task-detail-content');

        if (detailContainer.is(':visible')) {
            detailContainer.hide(); // Hide details if already visible
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
        $('html, body').animate({ scrollTop: detailContainer.offset().top }, 'slow'); // Scroll to the detail container
    }

    // Handle finishing selected tasks
    $('#finish-tasks-btn').on('click', async function () {
        const selectedTasks = [];
        $('.task-select:checked').each(function () {
            selectedTasks.push($(this).data('id'));
        });

        if (selectedTasks.length === 0) {
            alert('No tasks selected.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8081/api/finish_tasks', {
                method: 'POST',
                credentials: 'include', // Automatically include cookies
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ taskIds: selectedTasks }) // Send task IDs
            });

            if (response.ok) {
                alert('Tasks marked as finished.');
                $('#plant-tasks').DataTable().ajax.reload(); // Reload table data
            } else {
                alert('Failed to finish tasks. Please try again.');
            }
        } catch (error) {
            console.error('Error finishing tasks:', error);
        }
    });
});
