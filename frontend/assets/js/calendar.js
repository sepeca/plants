// Import checkLogin.js functionality by including it in the HTML
checkLoginStatus();

function logout() {
    document.cookie = 'authToken=; max-age=0; path=/'; // Clear auth token
    document.cookie = 'username=; max-age=0; path=/'; // Clear username
    document.cookie = 'role=; max-age=0; path=/'; // Clear role
    window.location.href = '/pages/login.html';
}

function showAdminActions() {
    const cookies = document.cookie.split(';').reduce((acc, cookie) => {
        const [key, value] = cookie.trim().split('=');
        acc[key] = value;
        return acc;
    }, {});

    if (cookies.role === 'admin') {
        document.querySelector('.admin-actions').style.display = 'block'; // Show admin actions
    }
}

$(document).ready(function () {
    showAdminActions(); // Check and show admin actions
    const table = $('#plant-tasks').DataTable({
        ajax: {
            url: '/api/get_tasks',
            dataSrc: '',
            data: function (d) {
                d.role = getUserRole(); // Add user role to the request
                d.status = 'unfinished'; // Only fetch unfinished tasks
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
        fetch(`/api/get_task_details?id=${rowId}`)
            .then(response => response.json())
            .then(data => {
                showTaskDetails(data);
            })
            .catch(error => console.error('Error fetching task details:', error));
    });

    // Show task details in the detail container
    function showTaskDetails(data) {
        const detailContainer = $('.task-detail-container');
        const detailContent = $('#task-detail-content');

        detailContent.html(`
            <p><strong>ID:</strong> ${data.id}</p>
            <p><strong>Last Finished Event:</strong> ${data.lastEvent}</p>
            <p><strong>Caretaker:</strong> ${data.caretaker}</p>
            <p><strong>Description:</strong> ${data.description || 'No description available.'}</p>
            <div class="task-images">
                ${data.photos.map(photo => `<img src="${photo}" alt="Task Photo" class="task-photo">`).join('')}
            </div>
        `);

        detailContainer.show();
        $('html, body').animate({ scrollTop: detailContainer.offset().top }, 'slow'); // Scroll to the detail container
    }

    // Close modal
    $('.close-btn').on('click', function () {
        $('#task-details-modal').hide();
    });

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

        fetch('/api/finish_tasks', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ taskIds: selectedTasks })
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

    function getUserRole() {
        // Fetch user role from the server or cookie
        return document.cookie.includes('role=admin') ? 'admin' : 'user';
    }
});
