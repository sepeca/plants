function logout() {
    document.cookie = 'authToken=; max-age=0; path=/'; // Clear auth token
    document.cookie = 'username=; max-age=0; path=/'; // Clear username
    document.cookie = 'role=; max-age=0; path=/'; // Clear role
    window.location.href = '/pages/login.html';
}

document.getElementById('add-plant-form').addEventListener('submit', async function(event) {
    event.preventDefault();
    const plantName = document.getElementById('plant-name').value;
    const species = document.getElementById('species').value;
    const room = document.getElementById('room').value;
    const task = document.getElementById('task').value;
    const date = document.getElementById('date').value;
    const time = document.getElementById('time').value;
    const images = document.getElementById('images').files;

    const formData = new FormData();
    formData.append('plantName', plantName);
    formData.append('species', species);
    formData.append('room', room);
    formData.append('task', task);
    formData.append('date', date);
    formData.append('time', time);

    for (let i = 0; i < images.length; i++) {
        formData.append('images', images[i]);
    }

    try {
        const response = await fetch('/api/add_plant', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            alert('Plant added successfully.');
            document.getElementById('add-plant-form').reset();
            $('#plant-table').DataTable().ajax.reload(); // Reload table data
        } else {
            alert('Failed to add plant. Please try again.');
        }
    } catch (error) {
        console.error('Error adding plant:', error);
        alert('An error occurred. Please try again later.');
    }
});

$(document).ready(function () {
    $('#plant-table').DataTable({
        ajax: {
            url: '/api/get_plants',
            dataSrc: ''
        },
        columns: [
            { data: 'plantName' },
            { data: 'species' },
            { data: 'room' },
            { data: 'task' },
            {
                data: null,
                render: function (data, type, row) {
                    return `<button class="delete-btn" data-id="${row.id}">Delete</button>`;
                }
            }
        ],
        pageLength: 10,
        scrollY: '400px',
        scrollCollapse: true,
        scroller: true
    });

    $('#plant-table').on('click', '.delete-btn', async function () {
        const plantId = $(this).data('id');
        if (!confirm('Are you sure you want to delete this plant?')) {
            return;
        }

        try {
            const response = await fetch('/api/delete_plant', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ plantId })
            });

            if (response.ok) {
                alert('Plant deleted successfully.');
                $('#plant-table').DataTable().ajax.reload(); // Reload table data
            } else {
                alert('Failed to delete plant. Please try again.');
            }
        } catch (error) {
            console.error('Error deleting plant:', error);
            alert('An error occurred. Please try again later.');
        }
    });
});
