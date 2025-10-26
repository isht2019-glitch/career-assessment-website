# TheApp

A modern web application with comprehensive authentication and payment features, designed for your domain **theapp.work**.

## Features

### 🔐 Authentication
- **Email/Password Registration & Login**
- **Google Sign-In Integration**
- **Mobile OTP Verification**
- **Forgot Password Recovery**
- **Secure Form Validation**

### 💳 Payment System
- **Multiple Payment Methods:**
  - Credit/Debit Cards (Stripe)
  - PayPal Integration
  - UPI/Razorpay (Indian payments)
  - Cryptocurrency Support
- **Flexible Pricing Plans:**
  - Starter (Free)
  - Pro ($19/month or $15/month yearly)
  - Enterprise ($49/month or $39/month yearly)
- **Yearly Billing Discount (20% off)**

### 🎨 Modern UI/UX
- **Responsive Design** - Works on all devices
- **Glass Morphism Effects** - Modern visual design
- **Smooth Animations** - Enhanced user experience
- **Accessible Interface** - WCAG compliant

## Quick Start

1. **Clone or download** this repository
2. **Open `index.html`** in your browser to view the sign-up page
3. **Navigate to `payment.html`** to see the pricing and payment options

## File Structure

```
theapp/
├── index.html              # Main sign-up/sign-in page
├── payment.html           # Pricing and payment page
├── styles.css             # Authentication page styles
├── payment-styles.css     # Payment page styles
├── script.js              # Authentication functionality
├── payment-script.js      # Payment processing logic
├── package.json           # Project configuration
└── README.md             # This file
```

## Configuration Required

### 1. Google Sign-In
Replace `YOUR_GOOGLE_CLIENT_ID` in `index.html` with your actual Google OAuth client ID:
```html
<meta name="google-signin-client_id" content="YOUR_ACTUAL_CLIENT_ID.apps.googleusercontent.com">
```

### 2. Stripe Payments
Replace the test key in `payment-script.js`:
```javascript
this.stripe = Stripe('pk_live_your_actual_stripe_key_here');
```

### 3. Razorpay Integration
Update the Razorpay key in `payment-script.js`:
```javascript
key: 'rzp_live_your_actual_key_here',
```

## Deployment

This app is ready to deploy to any static hosting service:

- **Netlify** (Recommended)
- **Vercel**
- **GitHub Pages**
- **AWS S3 + CloudFront**

### For theapp.work Domain

1. Point your domain DNS to your hosting provider
2. Configure SSL certificate
3. Update any hardcoded URLs to use your domain

## Security Notes

- Replace all placeholder API keys with real ones
- Enable HTTPS in production
- Implement proper backend validation
- Use environment variables for sensitive data
- Add rate limiting for API endpoints

## Browser Support

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## License

MIT License - Feel free to use this for your projects!

---

**Built with ❤️ for theapp.work**
