// Authentication and form handling
class AuthManager {
    constructor() {
        this.initializeEventListeners();
        this.initializeGoogleAuth();
    }

    initializeEventListeners() {
        // Tab switching
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.addEventListener('click', (e) => this.switchTab(e.target.dataset.tab));
        });

        // Form submissions
        document.getElementById('signupForm').addEventListener('submit', (e) => this.handleSignup(e));
        document.getElementById('signinForm').addEventListener('submit', (e) => this.handleSignin(e));

        // Social auth buttons
        document.getElementById('googleSignIn').addEventListener('click', () => this.handleGoogleAuth());
        document.getElementById('googleSignInLogin').addEventListener('click', () => this.handleGoogleAuth());
        document.getElementById('phoneAuth').addEventListener('click', () => this.handlePhoneAuth());
        document.getElementById('phoneAuthLogin').addEventListener('click', () => this.handlePhoneAuth());

        // OTP modal
        document.querySelector('.close').addEventListener('click', () => this.closeOtpModal());
        document.getElementById('verifyOtp').addEventListener('click', () => this.verifyOtp());
        document.getElementById('resendOtp').addEventListener('click', () => this.resendOtp());

        // OTP input handling
        this.setupOtpInputs();

        // Forgot password
        document.getElementById('forgotPassword').addEventListener('click', (e) => {
            e.preventDefault();
            this.handleForgotPassword();
        });
    }

    switchTab(tab) {
        // Update tab buttons
        document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
        document.querySelector(`[data-tab="${tab}"]`).classList.add('active');

        // Update forms
        document.querySelectorAll('.auth-form').forEach(form => form.classList.remove('active'));
        document.getElementById(tab).classList.add('active');
    }

    async handleSignup(e) {
        e.preventDefault();
        
        const formData = {
            fullName: document.getElementById('fullName').value,
            email: document.getElementById('email').value,
            phone: document.getElementById('phone').value,
            password: document.getElementById('password').value,
            confirmPassword: document.getElementById('confirmPassword').value
        };

        // Validate passwords match
        if (formData.password !== formData.confirmPassword) {
            this.showError('Passwords do not match');
            return;
        }

        // Validate password strength
        if (formData.password.length < 8) {
            this.showError('Password must be at least 8 characters long');
            return;
        }

        try {
            this.showLoading('Creating your account...');
            
            // Simulate API call
            await this.simulateApiCall();
            
            this.showSuccess('Account created successfully!');
            setTimeout(() => {
                window.location.href = 'test.html';
            }, 2000);
            
        } catch (error) {
            this.showError('Failed to create account. Please try again.');
        }
    }

    async handleSignin(e) {
        e.preventDefault();
        
        const formData = {
            email: document.getElementById('signinEmail').value,
            password: document.getElementById('signinPassword').value
        };

        try {
            this.showLoading('Signing you in...');
            
            // Simulate API call
            await this.simulateApiCall();
            
            this.showSuccess('Welcome back!');
            setTimeout(() => {
                window.location.href = 'test.html';
            }, 2000);
            
        } catch (error) {
            this.showError('Invalid credentials. Please try again.');
        }
    }

    initializeGoogleAuth() {
        // Initialize Google Sign-In
        window.gapi?.load('auth2', () => {
            gapi.auth2.init({
                client_id: 'YOUR_GOOGLE_CLIENT_ID.apps.googleusercontent.com'
            });
        });
    }

    async handleGoogleAuth() {
        try {
            this.showLoading('Connecting to Google...');
            
            // Simulate Google auth
            await this.simulateApiCall();
            
            // Simulate successful authentication
            setTimeout(() => {
                alert('Authentication successful! Redirecting to career assessment...');
                window.location.href = 'test.html';
            }, 1000);
            
        } catch (error) {
            this.showError('Google authentication failed. Please try again.');
        }
    }

    handlePhoneAuth() {
        const phoneInput = document.getElementById('phone') || document.getElementById('signinEmail');
        let phoneNumber = phoneInput.value;

        if (!phoneNumber) {
            phoneNumber = prompt('Please enter your phone number:');
            if (!phoneNumber) return;
        }

        // Validate phone number format
        const phoneRegex = /^[\+]?[1-9][\d]{0,15}$/;
        if (!phoneRegex.test(phoneNumber.replace(/\s/g, ''))) {
            this.showError('Please enter a valid phone number');
            return;
        }

        this.currentPhone = phoneNumber;
        this.showOtpModal(phoneNumber);
        this.sendOtp(phoneNumber);
    }

    showOtpModal(phoneNumber) {
        document.getElementById('phoneNumber').textContent = phoneNumber;
        document.getElementById('otpModal').style.display = 'block';
        
        // Focus first OTP input
        document.querySelector('.otp-input').focus();
    }

    closeOtpModal() {
        document.getElementById('otpModal').style.display = 'none';
        this.clearOtpInputs();
    }

    setupOtpInputs() {
        const inputs = document.querySelectorAll('.otp-input');
        
        inputs.forEach((input, index) => {
            input.addEventListener('input', (e) => {
                if (e.target.value.length === 1 && index < inputs.length - 1) {
                    inputs[index + 1].focus();
                }
            });

            input.addEventListener('keydown', (e) => {
                if (e.key === 'Backspace' && e.target.value === '' && index > 0) {
                    inputs[index - 1].focus();
                }
            });

            input.addEventListener('paste', (e) => {
                e.preventDefault();
                const paste = e.clipboardData.getData('text');
                const digits = paste.replace(/\D/g, '').slice(0, 6);
                
                digits.split('').forEach((digit, i) => {
                    if (inputs[i]) {
                        inputs[i].value = digit;
                    }
                });
                
                if (digits.length === 6) {
                    this.verifyOtp();
                }
            });
        });
    }

    clearOtpInputs() {
        document.querySelectorAll('.otp-input').forEach(input => {
            input.value = '';
        });
    }

    async sendOtp(phoneNumber) {
        try {
            this.showLoading('Sending OTP...');
            
            // Simulate OTP sending
            await this.simulateApiCall();
            
            this.showSuccess('OTP sent successfully!');
            
        } catch (error) {
            this.showError('Failed to send OTP. Please try again.');
        }
    }

    async verifyOtp() {
        const otpInputs = document.querySelectorAll('.otp-input');
        const otp = Array.from(otpInputs).map(input => input.value).join('');

        if (otp.length !== 6) {
            this.showError('Please enter the complete 6-digit code');
            return;
        }

        try {
            this.showLoading('Verifying OTP...');
            
            // Simulate OTP verification
            await this.simulateApiCall();
            
            this.showSuccess('Phone number verified successfully!');
            this.closeOtpModal();
            
            setTimeout(() => {
                window.location.href = 'test.html';
            }, 2000);
            
        } catch (error) {
            this.showError('Invalid OTP. Please try again.');
            this.clearOtpInputs();
        }
    }

    async resendOtp() {
        try {
            await this.sendOtp(this.currentPhone);
        } catch (error) {
            this.showError('Failed to resend OTP. Please try again.');
        }
    }

    handleForgotPassword() {
        const email = prompt('Please enter your email address:');
        if (!email) return;

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            this.showError('Please enter a valid email address');
            return;
        }

        this.showSuccess('Password reset link sent to your email!');
    }

    // Utility methods
    async simulateApiCall() {
        return new Promise((resolve) => {
            setTimeout(resolve, 1500 + Math.random() * 1000);
        });
    }

    showLoading(message) {
        this.removeMessages();
        const loading = document.createElement('div');
        loading.className = 'message loading';
        loading.innerHTML = `
            <div class="spinner"></div>
            <span>${message}</span>
        `;
        document.querySelector('.auth-card').appendChild(loading);
    }

    showSuccess(message) {
        this.removeMessages();
        const success = document.createElement('div');
        success.className = 'message success';
        success.textContent = message;
        document.querySelector('.auth-card').appendChild(success);
    }

    showError(message) {
        this.removeMessages();
        const error = document.createElement('div');
        error.className = 'message error';
        error.textContent = message;
        document.querySelector('.auth-card').appendChild(error);
    }

    removeMessages() {
        document.querySelectorAll('.message').forEach(msg => msg.remove());
    }
}

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new AuthManager();
});

// Add message styles
const messageStyles = `
.message {
    padding: 12px 16px;
    border-radius: 8px;
    margin: 15px 0;
    display: flex;
    align-items: center;
    gap: 10px;
    font-size: 14px;
    font-weight: 500;
}

.message.success {
    background: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
}

.message.error {
    background: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
}

.message.loading {
    background: #e2e3e5;
    color: #383d41;
    border: 1px solid #d6d8db;
}

.spinner {
    width: 16px;
    height: 16px;
    border: 2px solid #f3f3f3;
    border-top: 2px solid #007bff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
}
`;

// Inject styles
const styleSheet = document.createElement('style');
styleSheet.textContent = messageStyles;
document.head.appendChild(styleSheet);
