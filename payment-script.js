// Payment Management System
class PaymentManager {
    constructor() {
        this.stripe = null;
        this.cardElements = {};
        this.currentPlan = null;
        this.isYearly = false;
        
        this.initializeStripe();
        this.initializeEventListeners();
        this.updatePricing();
    }

    initializeStripe() {
        // Initialize Stripe (replace with your publishable key)
        if (window.Stripe) {
            this.stripe = Stripe('pk_test_your_stripe_publishable_key_here');
            this.setupStripeElements();
        }
    }

    setupStripeElements() {
        if (!this.stripe) return;

        const elements = this.stripe.elements();
        const style = {
            base: {
                fontSize: '16px',
                color: '#424770',
                '::placeholder': {
                    color: '#aab7c4',
                },
            },
        };

        // Create card elements
        this.cardElements.number = elements.create('cardNumber', { style });
        this.cardElements.expiry = elements.create('cardExpiry', { style });
        this.cardElements.cvc = elements.create('cardCvc', { style });

        // Mount elements
        this.cardElements.number.mount('#card-number');
        this.cardElements.expiry.mount('#card-expiry');
        this.cardElements.cvc.mount('#card-cvc');
    }

    initializeEventListeners() {
        // Billing toggle
        document.getElementById('billingToggle').addEventListener('change', (e) => {
            this.isYearly = e.target.checked;
            this.updatePricing();
        });

        // Plan selection buttons
        document.querySelectorAll('.btn-plan').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const plan = e.target.dataset.plan;
                if (plan !== 'free') {
                    this.selectPlan(plan);
                }
            });
        });

        // Payment method selection
        document.querySelectorAll('input[name="paymentMethod"]').forEach(radio => {
            radio.addEventListener('change', (e) => {
                this.switchPaymentMethod(e.target.value);
            });
        });

        // Payment method options
        document.querySelectorAll('.payment-option').forEach(option => {
            option.addEventListener('click', () => {
                const method = option.dataset.method;
                const radio = option.querySelector('input[type="radio"]');
                radio.checked = true;
                this.switchPaymentMethod(method);
            });
        });

        // Modal controls
        document.querySelector('#paymentModal .close').addEventListener('click', () => {
            this.closePaymentModal();
        });

        document.getElementById('payButton').addEventListener('click', () => {
            this.processPayment();
        });

        document.getElementById('continueToApp').addEventListener('click', () => {
            console.log('Continue to App button clicked');
            this.redirectToApp();
        });

        // Logout
        document.getElementById('logout').addEventListener('click', () => {
            this.logout();
        });

        // Crypto buttons
        document.querySelectorAll('.crypto-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                this.handleCryptoPayment(e.target.textContent.trim());
            });
        });
    }

    updatePricing() {
        document.querySelectorAll('.amount').forEach(amount => {
            const monthly = parseFloat(amount.dataset.monthly);
            const yearly = parseFloat(amount.dataset.yearly);
            const price = this.isYearly ? yearly : monthly;
            amount.textContent = price;
        });

        document.querySelectorAll('.period').forEach(period => {
            period.textContent = this.isYearly ? '/year' : '/month';
        });
    }

    selectPlan(planName) {
        this.currentPlan = {
            name: planName,
            displayName: planName.charAt(0).toUpperCase() + planName.slice(1) + ' Plan',
            monthly: this.getPlanPrice(planName, false),
            yearly: this.getPlanPrice(planName, true)
        };

        this.showPaymentModal();
    }

    getPlanPrice(plan, yearly) {
        const prices = {
            pro: { monthly: 1, yearly: 1 },
            enterprise: { monthly: 1, yearly: 1 }
        };
        
        return yearly ? prices[plan].yearly : prices[plan].monthly;
    }

    showPaymentModal() {
        const modal = document.getElementById('paymentModal');
        const plan = this.currentPlan;
        const price = this.isYearly ? plan.yearly : plan.monthly;
        const period = this.isYearly ? '/year' : '/month';

        // Update order summary
        document.getElementById('selectedPlan').textContent = plan.displayName;
        document.getElementById('planPrice').textContent = `$${price}${period}`;
        
        if (this.isYearly) {
            const monthlyPrice = plan.monthly * 12;
            const yearlyPrice = plan.yearly * 12;
            const discount = monthlyPrice - yearlyPrice;
            
            document.getElementById('discountRow').style.display = 'flex';
            document.getElementById('discountAmount').textContent = `-$${discount}`;
            document.getElementById('totalPrice').textContent = `$${yearlyPrice}/year`;
        } else {
            document.getElementById('discountRow').style.display = 'none';
            document.getElementById('totalPrice').textContent = `$${price}${period}`;
        }

        modal.style.display = 'block';
    }

    closePaymentModal() {
        document.getElementById('paymentModal').style.display = 'none';
    }

    switchPaymentMethod(method) {
        // Update active payment option
        document.querySelectorAll('.payment-option').forEach(option => {
            option.classList.remove('active');
        });
        document.querySelector(`[data-method="${method}"]`).classList.add('active');

        // Show corresponding payment form
        document.querySelectorAll('.payment-form').forEach(form => {
            form.classList.remove('active');
        });
        document.getElementById(`${method}Payment`).classList.add('active');

        // Update pay button text
        const payButton = document.getElementById('payButton');
        const buttonTexts = {
            card: 'Complete Payment',
            paypal: 'Pay with PayPal',
            razorpay: 'Pay with Razorpay',
            crypto: 'Pay with Crypto'
        };
        payButton.textContent = buttonTexts[method];
    }

    async processPayment() {
        const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
        const payButton = document.getElementById('payButton');
        
        payButton.disabled = true;
        payButton.textContent = 'Processing...';

        try {
            switch (paymentMethod) {
                case 'card':
                    await this.processCardPayment();
                    break;
                case 'paypal':
                    await this.processPayPalPayment();
                    break;
                case 'razorpay':
                    await this.processRazorpayPayment();
                    break;
                case 'crypto':
                    await this.processCryptoPayment();
                    break;
            }
        } catch (error) {
            this.showError('Payment failed. Please try again.');
            payButton.disabled = false;
            payButton.textContent = 'Complete Payment';
        }
    }

    async processCardPayment() {
        console.log('processCardPayment called');
        if (!this.stripe) {
            console.log('Stripe not initialized, using simulation');
        }

        // Simulate card payment processing
        console.log('Starting payment simulation');
        await this.simulatePayment();
        console.log('Payment simulation completed');
        this.showPaymentSuccess();
    }

    async processPayPalPayment() {
        // Simulate PayPal redirect
        await this.simulatePayment();
        this.showPaymentSuccess();
    }

    async processRazorpayPayment() {
        const plan = this.currentPlan;
        const amount = this.isYearly ? plan.yearly * 100 : plan.monthly * 100; // Convert to paise

        const options = {
            key: 'rzp_test_your_key_here', // Replace with your Razorpay key
            amount: amount,
            currency: 'INR',
            name: 'TheApp',
            description: `${plan.displayName} Subscription`,
            handler: (response) => {
                this.showPaymentSuccess();
            },
            prefill: {
                name: 'User Name',
                email: 'user@example.com',
                contact: '+919999999999'
            },
            theme: {
                color: '#667eea'
            }
        };

        if (window.Razorpay) {
            const rzp = new Razorpay(options);
            rzp.open();
        } else {
            await this.simulatePayment();
            this.showPaymentSuccess();
        }
    }

    async processCryptoPayment() {
        // Simulate crypto payment
        await this.simulatePayment();
        this.showPaymentSuccess();
    }

    handleCryptoPayment(cryptoType) {
        this.showInfo(`Redirecting to ${cryptoType} payment gateway...`);
        setTimeout(() => {
            this.processCryptoPayment();
        }, 2000);
    }

    async simulatePayment() {
        return new Promise((resolve) => {
            setTimeout(resolve, 2000 + Math.random() * 2000);
        });
    }

    showPaymentSuccess() {
        console.log('showPaymentSuccess called');
        const plan = this.currentPlan;
        console.log('Current plan:', plan);
        
        if (!plan) {
            console.error('No current plan found');
            return;
        }
        
        const price = this.isYearly ? plan.yearly : plan.monthly;
        const period = this.isYearly ? '/year' : '/month';
        
        // Close payment modal
        this.closePaymentModal();
        
        // Update success modal
        document.getElementById('successPlan').textContent = plan.displayName;
        document.getElementById('successAmount').textContent = `₹${price}${period}`;
        
        // Calculate next billing date
        const nextBilling = new Date();
        nextBilling.setMonth(nextBilling.getMonth() + (this.isYearly ? 12 : 1));
        document.getElementById('nextBilling').textContent = nextBilling.toLocaleDateString();
        
        // Show success modal
        console.log('Showing success modal');
        const successModal = document.getElementById('successModal');
        if (successModal) {
            successModal.style.display = 'block';
            console.log('Success modal displayed');
        } else {
            console.error('Success modal element not found');
        }
    }

    redirectToApp() {
        // Redirect to career assessment test
        console.log('redirectToApp called');
        this.showInfo('Redirecting to your career assessment test...');
        setTimeout(() => {
            console.log('Redirecting to test.html');
            window.location.href = 'test.html';
        }, 1500);
    }

    logout() {
        if (confirm('Are you sure you want to logout?')) {
            window.location.href = 'index.html';
        }
    }

    // Utility methods
    showError(message) {
        this.showMessage(message, 'error');
    }

    showSuccess(message) {
        this.showMessage(message, 'success');
    }

    showInfo(message) {
        this.showMessage(message, 'info');
    }

    showMessage(message, type) {
        // Remove existing messages
        document.querySelectorAll('.toast-message').forEach(msg => msg.remove());

        const toast = document.createElement('div');
        toast.className = `toast-message ${type}`;
        toast.textContent = message;
        
        document.body.appendChild(toast);
        
        setTimeout(() => {
            toast.classList.add('show');
        }, 100);
        
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => toast.remove(), 300);
        }, 3000);
    }
}

// Initialize payment manager when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new PaymentManager();
});

// Add toast message styles
const toastStyles = `
.toast-message {
    position: fixed;
    top: 20px;
    right: 20px;
    padding: 15px 20px;
    border-radius: 8px;
    color: white;
    font-weight: 500;
    z-index: 10000;
    transform: translateX(100%);
    transition: transform 0.3s ease;
    max-width: 300px;
}

.toast-message.show {
    transform: translateX(0);
}

.toast-message.success {
    background: #28a745;
}

.toast-message.error {
    background: #dc3545;
}

.toast-message.info {
    background: #17a2b8;
}
`;

// Inject toast styles
const styleSheet = document.createElement('style');
styleSheet.textContent = toastStyles;
document.head.appendChild(styleSheet);
