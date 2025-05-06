checkLoginStatus();

$(document).ready(function () {
    const table = $('#user-table').DataTable({
        ajax: {
            url: 'http://localhost:8081/api/get_users', // Use full URL with port 8081
            dataSrc: ''
        },
        columns: [
            {
                data: null,
                render: function (data, type, row) {
                    return `<input type="checkbox" class="user-select" data-id="${row.id}">`;
                },
                orderable: false
            },
            { data: 'username' },
            { data: 'email' },
            {
                data: 'isAdmin',
                render: function (data) {
                    return data ? 'Admin' : 'User';
                }
            },
            {
                data: null,
                render: function (data, type, row) {
                    const toggleAdminButton = `
                        <button class="toggle-admin-btn" data-id="${row.id}" data-is-admin="${row.isAdmin}">
                            ${row.isAdmin ? 'Revoke Admin' : 'Make Admin'}
                        </button>`;
                    const deleteButton = row.isAdmin
                        ? '' // Do not show delete button for admins
                        : `<button class="delete-btn" data-id="${row.id}">Delete</button>`;
                    return `${toggleAdminButton} ${deleteButton}`;
                }
            }
        ],
        pageLength: 10,
        scrollY: '400px',
        scrollCollapse: true,
        scroller: true
    });

    $('#add-user-form').on('submit', async function (event) {
        event.preventDefault();
        const username = $('#username').val();
        const email = $('#email').val();
        let password = $('#password').val();
        const isAdmin = $('#is-admin').is(':checked');

        if (!password) {
            password = generatePassword();
        }

        try {
            const response = await fetch('http://localhost:8081/api/add_user', { // Use full URL with port 8081
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, email, password, isAdmin })
            });

            if (response.ok) {
                alert('User added successfully.');
                $('#add-user-form')[0].reset();
                table.ajax.reload(); // Reload table data
            } else {
                alert('Failed to add user. Please try again.');
            }
        } catch (error) {
            console.error('Error adding user:', error);
            alert('An error occurred. Please try again later.');
        }
    });

    $('#delete-selected-users-btn').on('click', async function () {
        const selectedUsers = [];
        $('.user-select:checked').each(function () {
            selectedUsers.push($(this).data('id'));
        });

        if (selectedUsers.length === 0) {
            alert('No users selected.');
            return;
        }

        if (!confirm('Are you sure you want to delete the selected users? This action cannot be undone.')) {
            return;
        }

        try {
            const response = await fetch('http://localhost:8081/api/delete_users', { // Use full URL with port 8081
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userIds: selectedUsers })
            });

            if (response.ok) {
                alert('Selected users deleted successfully.');
                table.ajax.reload(); // Reload table data
            } else {
                alert('Failed to delete users. Please try again.');
            }
        } catch (error) {
            console.error('Error deleting users:', error);
            alert('An error occurred. Please try again later.');
        }
    });

    $('#user-table').on('click', '.toggle-admin-btn', async function () {
        const userId = $(this).data('id');
        const isAdmin = $(this).data('is-admin');
        try {
            const response = await fetch('http://localhost:8081/api/toggle_admin', { // Use full URL with port 8081
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userId, isAdmin: !isAdmin })
            });

            if (response.ok) {
                alert('User role updated successfully.');
                table.ajax.reload(); // Reload table data
            } else {
                alert('Failed to update user role. Please try again.');
            }
        } catch (error) {
            console.error('Error updating user role:', error);
            alert('An error occurred. Please try again later.');
        }
    });

    function generatePassword() {
        return Math.random().toString(36).slice(-8);
    }
});
