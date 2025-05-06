import { SERVER_ADDRESS } from './config.js';

const token = localStorage.getItem('jwt');

if (!token) {
    console.error('Authentication token is missing. Please log in again.');
    window.location.href = './login.html';
}

// Endpoints:
// POST ${SERVER_ADDRESS}/api/create_task
// POST ${SERVER_ADDRESS}/api/create_plant
// GET ${SERVER_ADDRESS}/api/get_plants
