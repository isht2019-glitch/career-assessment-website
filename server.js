const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 8080;

const server = http.createServer((req, res) => {
    // Set CORS headers
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
    res.setHeader('Cache-Control', 'no-cache, no-store, must-revalidate');
    res.setHeader('Pragma', 'no-cache');
    res.setHeader('Expires', '0');

    // Handle OPTIONS requests
    if (req.method === 'OPTIONS') {
        res.writeHead(200);
        res.end();
        return;
    }

    let filePath = req.url === '/' ? '/index.html' : req.url;
    filePath = path.join(__dirname, filePath);

    // Security: prevent directory traversal
    const realPath = path.resolve(filePath);
    const baseDir = path.resolve(__dirname);
    if (!realPath.startsWith(baseDir)) {
        res.writeHead(403);
        res.end('Forbidden');
        return;
    }

    // Serve files
    fs.readFile(filePath, (err, content) => {
        if (err) {
            if (err.code === 'ENOENT') {
                res.writeHead(404, { 'Content-Type': 'text/html' });
                res.end('<h1>404 - File Not Found</h1>');
            } else {
                res.writeHead(500);
                res.end('Server Error');
            }
        } else {
            // Determine content type
            const ext = path.extname(filePath);
            let contentType = 'text/html';
            if (ext === '.js') contentType = 'application/javascript';
            if (ext === '.css') contentType = 'text/css';
            if (ext === '.json') contentType = 'application/json';
            if (ext === '.jpeg' || ext === '.jpg') contentType = 'image/jpeg';
            if (ext === '.png') contentType = 'image/png';
            if (ext === '.gif') contentType = 'image/gif';

            res.writeHead(200, { 'Content-Type': contentType });
            res.end(content);
        }
    });
});

server.listen(PORT, () => {
    console.log(`
╔════════════════════════════════════════════════════════════╗
║                                                            ║
║         🚀 TheApp - Career Assessment Server 🚀           ║
║                                                            ║
║  Server is running at: http://localhost:${PORT}           ║
║                                                            ║
║  📱 Open in browser: http://localhost:${PORT}             ║
║                                                            ║
║  ✅ All fixes are now LIVE:                               ║
║     - Payment after personality test                      ║
║     - Improved questions (distinct options)               ║
║     - Logout/Delete/Survey buttons visible                ║
║     - Mobile responsive                                   ║
║     - Load More button on roadmap                         ║
║                                                            ║
║  Press Ctrl+C to stop the server                          ║
║                                                            ║
╚════════════════════════════════════════════════════════════╝
    `);
});
