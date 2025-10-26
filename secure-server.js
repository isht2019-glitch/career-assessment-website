// Secure Node.js server with IP whitelist for Career Roadmap Demo
const http = require('http');
const fs = require('fs');
const path = require('path');
const url = require('url');

// Configuration
const PORT = 8080;
const HOST = '0.0.0.0'; // Listen on all interfaces

// IP Whitelist - Add your users' IP addresses here
const ALLOWED_IPS = [
    '127.0.0.1',           // Localhost
    '::1',                 // IPv6 localhost
    '192.168.1.4',         // Your local IP
    '192.168.1.0/24',      // Your local network range
    // Add specific user IPs below:
    // '203.0.113.1',      // Example: User from India
    // '198.51.100.1',     // Example: User from USA
    // '203.0.113.50',     // Example: User from another state
    // Add more IPs as needed
];

// MIME types for different file extensions
const MIME_TYPES = {
    '.html': 'text/html',
    '.js': 'text/javascript',
    '.css': 'text/css',
    '.json': 'application/json',
    '.png': 'image/png',
    '.jpg': 'image/jpeg',
    '.gif': 'image/gif',
    '.svg': 'image/svg+xml',
    '.ico': 'image/x-icon'
};

// Function to check if IP is in allowed list
function isIPAllowed(clientIP) {
    // Remove IPv6 prefix if present
    const cleanIP = clientIP.replace(/^::ffff:/, '');
    
    for (const allowedIP of ALLOWED_IPS) {
        if (allowedIP.includes('/')) {
            // Handle CIDR notation (e.g., 192.168.1.0/24)
            if (isIPInRange(cleanIP, allowedIP)) {
                return true;
            }
        } else if (cleanIP === allowedIP || clientIP === allowedIP) {
            return true;
        }
    }
    return false;
}

// Function to check if IP is in CIDR range
function isIPInRange(ip, cidr) {
    const [range, bits] = cidr.split('/');
    const mask = ~(2 ** (32 - bits) - 1);
    return (ip2int(ip) & mask) === (ip2int(range) & mask);
}

// Convert IP to integer
function ip2int(ip) {
    return ip.split('.').reduce((int, oct) => (int << 8) + parseInt(oct, 10), 0) >>> 0;
}

// Log access attempts
function logAccess(clientIP, allowed, path) {
    const timestamp = new Date().toISOString();
    const status = allowed ? 'ALLOWED' : 'BLOCKED';
    console.log(`[${timestamp}] ${status}: ${clientIP} -> ${path}`);
}

// Create HTTP server
const server = http.createServer((req, res) => {
    const clientIP = req.connection.remoteAddress || req.socket.remoteAddress;
    const parsedUrl = url.parse(req.url);
    let pathname = parsedUrl.pathname;
    
    // Check IP whitelist
    if (!isIPAllowed(clientIP)) {
        logAccess(clientIP, false, pathname);
        res.writeHead(403, { 'Content-Type': 'text/html' });
        res.end(`
            <!DOCTYPE html>
            <html>
            <head>
                <title>Access Denied</title>
                <style>
                    body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }
                    .error { color: #d32f2f; }
                </style>
            </head>
            <body>
                <h1 class="error">Access Denied</h1>
                <p>Your IP address (${clientIP}) is not authorized to access this demo.</p>
                <p>Please contact the administrator to request access.</p>
            </body>
            </html>
        `);
        return;
    }
    
    logAccess(clientIP, true, pathname);
    
    // Default to career-demo.html
    if (pathname === '/') {
        pathname = '/career-demo.html';
    }
    
    // Construct file path
    const filePath = path.join(__dirname, pathname);
    
    // Check if file exists
    fs.access(filePath, fs.constants.F_OK, (err) => {
        if (err) {
            res.writeHead(404, { 'Content-Type': 'text/html' });
            res.end(`
                <!DOCTYPE html>
                <html>
                <head>
                    <title>404 Not Found</title>
                    <style>
                        body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }
                    </style>
                </head>
                <body>
                    <h1>404 - File Not Found</h1>
                    <p>The requested file could not be found.</p>
                    <a href="/career-demo.html">Go to Career Demo</a>
                </body>
                </html>
            `);
            return;
        }
        
        // Read and serve file
        fs.readFile(filePath, (error, content) => {
            if (error) {
                res.writeHead(500, { 'Content-Type': 'text/html' });
                res.end('500 - Internal Server Error');
                return;
            }
            
            // Get content type
            const ext = path.extname(filePath).toLowerCase();
            const contentType = MIME_TYPES[ext] || 'application/octet-stream';
            
            // Set security headers
            res.writeHead(200, {
                'Content-Type': contentType,
                'X-Frame-Options': 'DENY',
                'X-XSS-Protection': '1; mode=block',
                'X-Content-Type-Options': 'nosniff',
                'Access-Control-Allow-Origin': '*',
                'Cache-Control': 'no-cache'
            });
            
            res.end(content);
        });
    });
});

// Start server
server.listen(PORT, HOST, () => {
    console.log(`🚀 Career Roadmap Demo Server Started`);
    console.log(`📍 Server running at http://${HOST}:${PORT}/`);
    console.log(`🔒 IP Whitelist enabled with ${ALLOWED_IPS.length} allowed addresses`);
    console.log(`🌐 Access URL: http://192.168.1.4:${PORT}/career-demo.html`);
    console.log(`\n📋 To add user IPs, edit the ALLOWED_IPS array in this file`);
    console.log(`\n🔍 Access logs will appear below:`);
});

// Handle server errors
server.on('error', (err) => {
    console.error('Server error:', err);
});

// Graceful shutdown
process.on('SIGINT', () => {
    console.log('\n🛑 Shutting down server...');
    server.close(() => {
        console.log('✅ Server closed');
        process.exit(0);
    });
});
