// Import checkLogin.js functionality by including it in the HTML
checkLoginStatus();

function logout() {
    document.cookie = 'authToken=; max-age=0; path=/'; // Clear auth token
    window.location.href = '/pages/login.html';
}

$(document).ready(function () {
    const table = $('#plant-tasks').DataTable({
        ajax: {
            url: '/api/get_tasks',
            dataSrc: '',
            beforeSend: function (xhr) {
                const token = document.cookie.split(';').find(cookie => cookie.trim().startsWith('authToken='))
                    ?.split('=')[1];
                if (token) {
                    xhr.setRequestHeader('Authorization', `Bearer ${token}`); // Add JWT token to the request
                }
            }
        },
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
    $('#finish-tasks-btn').on('click', function () {
        const selectedTasks = [];
        $('.task-select:checked').each(function () {
            selectedTasks.push($(this).data('id'));
        });

        if (selectedTasks.length === 0) {
            alert('No tasks selected.');
            return;
        }

        const token = document.cookie.split(';').find(cookie => cookie.trim().startsWith('authToken='))
            ?.split('=')[1];

        if (!token) {
            alert('Authentication token is missing. Please log in again.');
            window.location.href = '/pages/login.html';
            return;
        }

        fetch('/api/finish_tasks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` // Include JWT in the request
            },
            body: JSON.stringify({ taskIds: selectedTasks }) // Send task IDs
        })
            .then(response => {
                if (response.ok) {
                    alert('Tasks marked as finished.');
                    table.ajax.reload(); // Reload table data
                } else {
                    alert('Failed to finish tasks. Please try again.');
                }
            })
            .catch(error => console.error('Error finishing tasks:', error));
    });
});
