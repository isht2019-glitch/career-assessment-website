// Interactive Guide System - Velly Bandaar Character
class GuideSystem {
    constructor() {
        this.currentStep = 0;
        this.userName = '';
        this.isActive = false;
        this.init();
    }

    init() {
        this.createGuideElements();
        this.bindEvents();
        // Start guide after page loads only if not completed before
        setTimeout(() => {
            if (this.shouldShowGuide()) {
                this.startGuide();
            } else {
                // Show character even if guide is completed, for manual restart
                document.getElementById('guideCharacter').classList.remove('hidden');
            }
        }, 2000);
    }

    createGuideElements() {
        // Create overlay
        const overlay = document.createElement('div');
        overlay.className = 'guide-overlay';
        overlay.id = 'guideOverlay';
        document.body.appendChild(overlay);

        // Create character
        const character = document.createElement('div');
        character.className = 'guide-character hidden';
        character.id = 'guideCharacter';
        character.innerHTML = `
            <div style="width: 100%; height: 100%; border-radius: 50%; background: linear-gradient(45deg, #8B4513, #D2691E); display: flex; align-items: center; justify-content: center; position: relative; overflow: hidden;">
                <div style="font-size: 60px;">🐵</div>
                <div style="position: absolute; bottom: 15px; right: 15px; font-size: 25px; transform: rotate(-15deg);">🔫</div>
            </div>
        `;
        document.body.appendChild(character);

        // Create speech bubble
        const speechBubble = document.createElement('div');
        speechBubble.className = 'speech-bubble';
        speechBubble.id = 'speechBubble';
        speechBubble.innerHTML = `
            <button class="guide-close" id="guideClose">&times;</button>
            <div class="guide-content" id="guideContent"></div>
            <div class="typing-indicator" id="typingIndicator">
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
                <div class="typing-dot"></div>
            </div>
        `;
        document.body.appendChild(speechBubble);
    }

    bindEvents() {
        document.getElementById('guideCharacter').addEventListener('click', () => {
            if (!this.isActive) this.startGuide();
        });

        document.getElementById('guideClose').addEventListener('click', () => {
            this.hideGuide();
        });

        document.getElementById('guideOverlay').addEventListener('click', (e) => {
            if (e.target === e.currentTarget) this.hideGuide();
        });
    }

    startGuide() {
        this.isActive = true;
        this.currentStep = 0;
        this.showGuide();
        this.showStep(0);
    }

    showGuide() {
        document.getElementById('guideOverlay').classList.add('active');
        document.getElementById('guideCharacter').classList.remove('hidden');
        document.getElementById('speechBubble').classList.add('active');
    }

    hideGuide() {
        document.getElementById('guideOverlay').classList.remove('active');
        document.getElementById('speechBubble').classList.remove('active');
        this.isActive = false;
    }

    showTyping(duration = 2000) {
        const typingIndicator = document.getElementById('typingIndicator');
        const content = document.getElementById('guideContent');
        
        content.style.display = 'none';
        typingIndicator.classList.add('active');
        
        return new Promise(resolve => {
            setTimeout(() => {
                typingIndicator.classList.remove('active');
                content.style.display = 'block';
                resolve();
            }, duration);
        });
    }

    async showStep(step) {
        const content = document.getElementById('guideContent');
        
        await this.showTyping(1500);
        
        switch(step) {
            case 0:
                content.innerHTML = `
                    <div class="guide-text">
                        Hi! My name is <span class="guide-name">"Velly Bandaar"</span>! 🐵
                    </div>
                    <div class="guide-text">
                        I work for the gang called <span class="guide-gang">"Badmash Patandar Samajvadi Party"</span> 💀
                    </div>
                    <div class="guide-text">
                        This group helps in illumination of the world. ✨
                    </div>
                    <div class="guide-text">
                        What's your name? 🤔
                    </div>
                    <input type="text" class="guide-input" id="userNameInput" placeholder="Enter your name..." maxlength="30">
                    <button class="guide-btn continue" onclick="guideSystem.handleNameInput()">Tell Velly</button>
                `;
                document.getElementById('userNameInput').focus();
                break;

            case 1:
                await this.showTyping(2000);
                content.innerHTML = `
                    <div class="guide-text">
                        I don't care who you are. 😤
                    </div>
                    <div class="guide-text">
                        But you will be someone because you have come on <span class="guide-name">"TheApp"</span> - a byproduct of my gang! 🚀
                    </div>
                    <div class="guide-text">
                        First you need to pay a small amount. Then I will instruct you more. 💰
                    </div>
                    <div class="guide-buttons">
                        <button class="guide-btn yes" onclick="guideSystem.handleChoice('yes')">Yes</button>
                        <button class="guide-btn obey" onclick="guideSystem.handleChoice('obey')">I will obey</button>
                    </div>
                `;
                break;

            case 2:
                await this.showTyping(1500);
                const response = this.getResponseMessage();
                content.innerHTML = `
                    <div class="guide-text">
                        ${response} 💪
                    </div>
                    <div class="guide-text">
                        Now proceed with your registration and remember - Velly Bandaar is watching! 👁️
                    </div>
                    <button class="guide-btn continue" onclick="guideSystem.completeGuide()">Start Registration</button>
                `;
                break;
        }
    }

    handleNameInput() {
        const nameInput = document.getElementById('userNameInput');
        const name = nameInput.value.trim();
        
        if (name.length < 2) {
            nameInput.style.border = '2px solid #ff4444';
            nameInput.placeholder = 'Please enter a valid name!';
            return;
        }
        
        this.userName = name;
        this.currentStep = 1;
        this.showStep(1);
    }

    handleChoice(choice) {
        this.userChoice = choice;
        this.currentStep = 2;
        this.showStep(2);
    }

    getResponseMessage() {
        if (this.userChoice === 'yes') {
            return "You need to work hard! 🔥";
        } else if (this.userChoice === 'obey') {
            return "You are on the right path! ✅";
        }
        return "Good choice!";
    }

    completeGuide() {
        this.hideGuide();
        // Hide character after guide completion
        setTimeout(() => {
            document.getElementById('guideCharacter').classList.add('hidden');
        }, 500);
        
        // Store that user has seen the guide
        localStorage.setItem('vellyGuideCompleted', 'true');
        localStorage.setItem('userName', this.userName);
        
        // Focus on first input field
        const firstInput = document.querySelector('#signup input, #signin input');
        if (firstInput) firstInput.focus();
    }

    // Check if user has already seen the guide
    shouldShowGuide() {
        return !localStorage.getItem('vellyGuideCompleted');
    }
}

// Initialize guide system when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    window.guideSystem = new GuideSystem();
});

// Fallback initialization if DOMContentLoaded already fired
if (document.readyState === 'loading') {
    // DOM is still loading
} else {
    // DOM is already loaded
    window.guideSystem = new GuideSystem();
}

// Add keyboard support
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && window.guideSystem && window.guideSystem.isActive) {
        window.guideSystem.hideGuide();
    }
    
    // Enter key support for name input
    if (e.key === 'Enter' && document.getElementById('userNameInput')) {
        window.guideSystem.handleNameInput();
    }
});
