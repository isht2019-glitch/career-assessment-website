// API Configuration for Custom Backend
// Switch between local development and production

const API_CONFIG = {
    // Change this to your production server URL when deploying
    BASE_URL: window.location.hostname === 'localhost' 
        ? 'http://localhost:3001/api' 
        : 'https://admin.theapp.work/api', // Update with your domain
    
    // API Endpoints
    ENDPOINTS: {
        // User endpoints
        USER_REGISTER: '/users/register',
        USER_LOGIN: '/users/login',
        
        // Payment endpoints
        PAYMENT_SUBMIT: '/payments/submit',
        PAYMENT_STATUS: '/payments/status',
        
        // Results endpoints
        RESULTS_SAVE: '/results/save',
        RESULTS_GET: '/results',
        
        // Admin endpoints (for admin panel)
        ADMIN_LOGIN: '/admin/login',
        ADMIN_STATS: '/admin/stats',
        ADMIN_USERS: '/admin/users',
        ADMIN_PAYMENTS_PENDING: '/admin/payments/pending',
        ADMIN_PAYMENTS_APPROVE: '/admin/payments/approve',
        ADMIN_PAYMENTS_REJECT: '/admin/payments/reject',
        ADMIN_RESULTS: '/admin/results',
        ADMIN_CHANGE_PASSWORD: '/admin/change-password'
    }
};

// Helper function to make API calls
async function apiCall(endpoint, method = 'GET', data = null, requiresAuth = false) {
    const url = API_CONFIG.BASE_URL + endpoint;
    
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        }
    };
    
    // Add auth token if required
    if (requiresAuth) {
        const token = localStorage.getItem('userToken') || localStorage.getItem('adminToken');
        if (token) {
            options.headers['Authorization'] = `Bearer ${token}`;
        }
    }
    
    // Add body for POST requests
    if (data && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(data);
    }
    
    try {
        const response = await fetch(url, options);
        const result = await response.json();
        return result;
    } catch (error) {
        console.error('API Error:', error);
        return { success: false, error: error.message };
    }
}

// Export for use in other files
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { API_CONFIG, apiCall };
}
