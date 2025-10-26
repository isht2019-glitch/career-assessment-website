# 💳 Payment Integration Guide - Razorpay & Stripe

## 🇮🇳 **Razorpay Setup (India)**

### **Step 1: Create Razorpay Account**
1. Go to https://razorpay.com
2. Click **"Sign Up"** → Business account
3. **Business Details**:
   - Business Name: `TheApp`
   - Business Type: `Technology/Software`
   - Website: `theapp.work`
4. **Complete KYC** (PAN, Bank details)
5. **Activate account** (24-48 hours)

### **Step 2: Get API Keys**
1. **Dashboard** → **Settings** → **API Keys**
2. **Generate Test Keys** (for development):
   - **Key ID**: `rzp_test_xxxxxxxxxx`
   - **Key Secret**: `xxxxxxxxxx` (keep secret)
3. **Generate Live Keys** (for production - after KYC)

### **Step 3: Configure Webhooks**
1. **Settings** → **Webhooks** → **Add New Webhook**
2. **URL**: `https://theapp.work/api/razorpay/webhook`
3. **Events**: Select all payment events
4. **Secret**: Generate and save securely

### **Step 4: Update Android Code**
Replace in `PaymentActivity.kt`:
```kotlin
checkout.setKeyID("rzp_test_your_actual_key_here")
```

### **Step 5: Update Web Code**
Replace in `payment-script.js`:
```javascript
key: 'rzp_test_your_actual_key_here',
```

---

## 🌍 **Stripe Setup (International)**

### **Step 1: Create Stripe Account**
1. Go to https://stripe.com
2. Click **"Start now"** → **Create account**
3. **Business Details**:
   - Business Name: `TheApp`
   - Industry: `Software`
   - Website: `theapp.work`
   - Country: `India` (or your country)
4. **Complete verification** (may take 1-7 days)

### **Step 2: Get API Keys**
1. **Dashboard** → **Developers** → **API Keys**
2. **Test Keys** (for development):
   - **Publishable Key**: `pk_test_xxxxxxxxxx`
   - **Secret Key**: `sk_test_xxxxxxxxxx` (keep secret)
3. **Live Keys** (for production - after verification)

### **Step 3: Configure Webhooks**
1. **Developers** → **Webhooks** → **Add endpoint**
2. **URL**: `https://theapp.work/api/stripe/webhook`
3. **Events**: Select payment-related events
4. **Signing Secret**: Copy and save securely

### **Step 4: Update Android Code**
Replace in `PaymentActivity.kt`:
```kotlin
PaymentConfiguration.init(
    applicationContext,
    "pk_test_your_actual_stripe_key_here"
)
```

### **Step 5: Update Web Code**
Replace in `payment-script.js`:
```javascript
this.stripe = Stripe('pk_test_your_actual_stripe_key_here');
```

---

## 🔧 **Backend Requirements**

### **Create Payment Intent Endpoints**

#### **For Stripe** (`/api/stripe/create-payment-intent`):
```javascript
const stripe = require('stripe')('sk_test_your_secret_key');

app.post('/api/stripe/create-payment-intent', async (req, res) => {
  const { amount, currency = 'usd' } = req.body;
  
  const paymentIntent = await stripe.paymentIntents.create({
    amount: amount * 100, // Convert to cents
    currency: currency,
    metadata: {
      app: 'TheApp',
      plan: req.body.plan
    }
  });
  
  res.send({
    clientSecret: paymentIntent.client_secret
  });
});
```

#### **For Razorpay** (`/api/razorpay/create-order`):
```javascript
const Razorpay = require('razorpay');

const razorpay = new Razorpay({
  key_id: 'rzp_test_your_key_id',
  key_secret: 'your_key_secret'
});

app.post('/api/razorpay/create-order', async (req, res) => {
  const { amount, currency = 'INR' } = req.body;
  
  const order = await razorpay.orders.create({
    amount: amount * 100, // Convert to paise
    currency: currency,
    receipt: `receipt_${Date.now()}`,
    notes: {
      app: 'TheApp',
      plan: req.body.plan
    }
  });
  
  res.send(order);
});
```

---

## 📋 **Testing**

### **Razorpay Test Cards**
- **Success**: `4111 1111 1111 1111`
- **Failure**: `4000 0000 0000 0002`
- **CVV**: Any 3 digits
- **Expiry**: Any future date

### **Stripe Test Cards**
- **Success**: `4242 4242 4242 4242`
- **Declined**: `4000 0000 0000 0002`
- **CVV**: Any 3 digits
- **Expiry**: Any future date

### **Test UPI (Razorpay)**
- **Success**: `success@razorpay`
- **Failure**: `failure@razorpay`

---

## 🚀 **Going Live**

### **Razorpay Live Checklist**
- ✅ Complete KYC verification
- ✅ Add bank account details
- ✅ Replace test keys with live keys
- ✅ Set up proper webhook handling
- ✅ Test with small amounts

### **Stripe Live Checklist**
- ✅ Complete account verification
- ✅ Add business details and tax info
- ✅ Replace test keys with live keys
- ✅ Set up webhook endpoints
- ✅ Test with real payments

---

## 💡 **Pro Tips**

1. **Always use HTTPS** for payment pages
2. **Never expose secret keys** in frontend code
3. **Implement proper error handling**
4. **Log all payment events** for debugging
5. **Set up monitoring** for failed payments
6. **Use webhooks** for reliable payment confirmation
7. **Implement retry logic** for failed API calls

---

## 📞 **Support**
- **Razorpay**: support@razorpay.com
- **Stripe**: support@stripe.com

**Estimated setup time: 2-3 hours for both providers**
