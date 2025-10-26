# IP-Based Access Configuration Guide

## 🔒 **Secure Server Setup**

Yes! I can create a secure server that allows only specific IP addresses to access your Career Roadmap Demo.

### **How to Add User IPs:**

1. **Get User IP Addresses:**
   - Ask users to visit: `https://whatismyipaddress.com/`
   - They'll see their public IP (e.g., `203.0.113.45`)

2. **Add IPs to Server:**
   - Edit `secure-server.js`
   - Add IPs to the `ALLOWED_IPS` array:

```javascript
const ALLOWED_IPS = [
    '127.0.0.1',           // Your localhost
    '192.168.1.4',         // Your local IP
    '203.0.113.1',         // User from India
    '198.51.100.25',       // User from USA  
    '185.199.108.153',     // User from Europe
    '103.21.244.0',        // User from Asia
    // Add more IPs here
];
```

### **Start Secure Server:**

```bash
node secure-server.js
```

### **Features:**
- ✅ **IP Whitelist Protection**
- ✅ **Access Logging** 
- ✅ **Security Headers**
- ✅ **Custom Error Pages**
- ✅ **CIDR Range Support**

### **User Access:**
- **Allowed users:** See full demo
- **Blocked users:** Get "Access Denied" message
- **All access attempts logged**

### **Example Usage:**

**For users in different countries:**
1. User in Mumbai: `203.0.113.45`
2. User in New York: `198.51.100.67` 
3. User in London: `185.199.108.89`

Add these IPs to `ALLOWED_IPS` and they can access:
`http://192.168.1.4:8080/career-demo.html`

**Ready to configure with your users' specific IP addresses!**
