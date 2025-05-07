import { SERVER_ADDRESS } from './config.js';
import { checkAuthAndRedirect } from './auth.js';

$(document).ready(async function () {
    const token = checkAuthAndRedirect();
    if (!token) return;

    const userTableBody = document.querySelector('#user-table tbody');

    if (!userTableBody) {
        console.error('User table not found.');
        return;
    }

    async function fetchUsers() {
        try {
            const response = await fetch(`${SERVER_ADDRESS}/api/get_users`, {
                headers: { Authorization: `Bearer ${token}` }
            });

            if (!response.ok) {
                alert('You do not have permission to access this page.');
                window.location.href = './calendar.html';
                return;
            }

            const users = await response.json();
            users.forEach(user => {
                const row = `
                    <tr>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>${user.admin ? 'Admin' : 'User'}</td>
                        <td>
                            <button class="toggle-admin-btn" data-id="${user.userId}" data-is-admin="${user.admin}">
                                ${user.admin ? 'Revoke Admin' : 'Make Admin'}
                            </button>
                            <button class="delete-btn" data-id="${user.userId}">Delete</button>
                        </td>
                    </tr>
                `;
                userTableBody.insertAdjacentHTML('beforeend', row);
            });

            // Initialize DataTable after rows are inserted
            if ($.fn.DataTable.isDataTable('#user-table')) {
                $('#user-table').DataTable().destroy();
            }
            $('#user-table').DataTable({
                autoWidth: false,
                columns: [
                    { width: '20%' }, // Username
                    { width: '40%' }, // Email
                    { width: '20%' }, // Role
                    { width: '20%' }  // Actions
                ],
                scrollY: '400px',
                scrollCollapse: true,
                scroller: true,
                pageLength: 15
            });
        } catch (error) {
            console.error('Error fetching users:', error.message);
        }
    }

    async function toggleAdmin(userId, isAdmin) {
        try {
            const response = await fetch(`${SERVER_ADDRESS}/api/toggle_admin`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({ userId, isAdmin })
            });

            if (response.ok) {
                alert('User role updated successfully!');
                location.reload(); // Reload the page to update the table
            } else {
                const errorData = await response.json();
                alert(`Failed to update user role: ${errorData.message || 'Unknown error'}`);
            }
        } catch (error) {
            console.error('Error toggling admin role:', error.message);
            alert('An error occurred. Please try again later.');
        }
    }

    async function deleteUser(userIds) {
        try {
            const response = await fetch(`${SERVER_ADDRESS}/api/delete_users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({ userIds })
            });

            if (response.ok) {
                alert('User(s) deleted successfully!');
                location.reload(); // Reload the page to update the table
            } else {
                const errorData = await response.json();
                alert(`Failed to delete user(s): ${errorData.message || 'Unknown error'}`);
            }
        } catch (error) {
            console.error('Error deleting user(s):', error.message);
            alert('An error occurred. Please try again later.');
        }
    }

    // Fetch users from the API
    fetchUsers();

    // Event listener for "Toggle Admin" button
    $(document).on('click', '.toggle-admin-btn', function () {
        const userId = $(this).data('id');
        const isAdmin = !$(this).data('is-admin'); // Toggle the admin status
        const action = isAdmin ? 'Make Admin' : 'Revoke Admin';

        if (confirm(`Do you want to proceed with action: ${action} for this user?`)) {
            if (confirm('Last chance. Are you sure?')) {
                toggleAdmin(userId, isAdmin);
            }
        }
    });

    // Event listener for "Delete" button
    $(document).on('click', '.delete-btn', function () {
        const userId = $(this).data('id');

        if (confirm(`Do you want to proceed with action: Removing this user?`)) {
            if (confirm('Last chance. Are you sure?')) {
                deleteUser([userId]);
            }
        }
    });

    // Handle form submission for adding a new user
    document.querySelector('#add-user-form').addEventListener('submit', async function (event) {
        event.preventDefault();
        const username = document.querySelector('#username').value.trim();
        const email = document.querySelector('#email').value.trim();
        const password = document.querySelector('#password').value.trim();
        const isAdmin = document.querySelector('#is-admin').checked;

        if (!username || !email || !password) {
            alert('All fields are required.');
            return;
        }

        try {
            const response = await fetch(`${SERVER_ADDRESS}/api/add_user`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({ username, email, password, isAdmin })
            });

            if (response.ok) {
                alert('User added successfully!');
                location.reload(); // Reload the page to update the table
            } else {
                const errorData = await response.json();
                alert(`Failed to add user: ${errorData.message || 'Unknown error'}`);
            }
        } catch (error) {
            console.error('Error adding user:', error.message);
            alert('An error occurred. Please try again later.');
        }
    });
});
