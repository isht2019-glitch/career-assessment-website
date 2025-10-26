// Test Application Logic
let currentQuestion = 0;
let riasecScores = { R: 0, I: 0, A: 0, S: 0, E: 0, C: 0 };
let aptitudeAnswers = [];
let timeRemaining = 75 * 60; // 75 minutes in seconds
let timerInterval;
let allQuestions = [];
let answers = [];

// Initialize the test
function initializeTest() {
    // Combine RIASEC and Aptitude questions
    allQuestions = [
        ...riasecQuestions.map((q, index) => ({
            ...q,
            section: 'riasec',
            originalIndex: index
        })),
        ...aptitudeQuestions.map((q, index) => ({
            ...q,
            section: 'aptitude',
            originalIndex: index
        }))
    ];
    
    console.log('Total questions loaded:', allQuestions.length);
    console.log('RIASEC questions:', riasecQuestions.length);
    console.log('Aptitude questions:', aptitudeQuestions.length);
    console.log('Combined total should be:', riasecQuestions.length + aptitudeQuestions.length);
    
    // Initialize answers array
    answers = new Array(allQuestions.length).fill(null);
    
    // Start timer
    startTimer();
    
    // Display first question
    displayQuestion();
    
    // Update progress
    updateProgress();
}

// Timer functionality
function startTimer() {
    timerInterval = setInterval(() => {
        timeRemaining--;
        updateTimerDisplay();
        
        if (timeRemaining <= 0) {
            clearInterval(timerInterval);
            submitTest();
        }
    }, 1000);
}

function updateTimerDisplay() {
    const minutes = Math.floor(timeRemaining / 60);
    const seconds = timeRemaining % 60;
    const timerElement = document.getElementById('timer');
    
    timerElement.textContent = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    
    // Change color when time is running low
    if (timeRemaining <= 300) { // 5 minutes
        timerElement.style.background = '#ff4757';
    } else if (timeRemaining <= 900) { // 15 minutes
        timerElement.style.background = '#ffa502';
    }
}

// Display current question
function displayQuestion() {
    const container = document.getElementById('questionContainer');
    const question = allQuestions[currentQuestion];
    
    console.log(`Displaying question ${currentQuestion + 1}/${allQuestions.length}:`, question);
    
    // Update section headers
    updateSectionHeaders();
    
    // Create question HTML
    const questionHTML = `
        <div class="question-card active">
            <div class="question-number">Question ${currentQuestion + 1} of ${allQuestions.length}</div>
            <div class="question-text">${question.question}</div>
            <div class="options">
                ${question.options.map((option, index) => `
                    <div class="option" onclick="selectOption(${index})" data-index="${index}">
                        ${typeof option === 'string' ? option : option.text}
                    </div>
                `).join('')}
            </div>
        </div>
    `;
    
    container.innerHTML = questionHTML;
    
    // Restore previous answer if exists
    if (answers[currentQuestion] !== null) {
        const selectedOption = container.querySelector(`[data-index="${answers[currentQuestion]}"]`);
        if (selectedOption) {
            selectedOption.classList.add('selected');
        }
    }
    
    // Update navigation
    updateNavigation();
}

// Update section headers visibility
function updateSectionHeaders() {
    const section1Header = document.getElementById('section1Header');
    const section2Header = document.getElementById('section2Header');
    
    if (currentQuestion < riasecQuestions.length) {
        section1Header.style.display = 'block';
        section2Header.style.display = 'none';
    } else {
        section1Header.style.display = 'none';
        section2Header.style.display = 'block';
    }
}

// Handle option selection
function selectOption(optionIndex) {
    // Remove previous selection
    document.querySelectorAll('.option').forEach(opt => {
        opt.classList.remove('selected');
    });
    
    // Add selection to clicked option
    const selectedOption = document.querySelector(`[data-index="${optionIndex}"]`);
    selectedOption.classList.add('selected');
    
    // Store answer
    answers[currentQuestion] = optionIndex;
    
    // Update progress
    updateProgress();
}

// Navigation functions
function nextQuestion() {
    console.log(`Next question called. Current: ${currentQuestion}, Total: ${allQuestions.length}`);
    if (currentQuestion < allQuestions.length - 1) {
        currentQuestion++;
        displayQuestion();
        updateNavigation();
    } else {
        console.log('Submitting test...');
        submitTest();
    }
}

function previousQuestion() {
    if (currentQuestion > 0) {
        currentQuestion--;
        displayQuestion();
    }
}

// Update navigation buttons and counter
function updateNavigation() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const counter = document.getElementById('questionCounter');
    
    prevBtn.disabled = currentQuestion === 0;
    
    if (currentQuestion === allQuestions.length - 1) {
        nextBtn.textContent = 'Submit Test';
    } else {
        nextBtn.textContent = 'Next';
    }
    
    counter.textContent = `Question ${currentQuestion + 1} of ${allQuestions.length}`;
}

// Update progress bar
function updateProgress() {
    const answeredQuestions = answers.filter(answer => answer !== null).length;
    const progressPercentage = (answeredQuestions / allQuestions.length) * 100;
    
    document.getElementById('progressFill').style.width = `${progressPercentage}%`;
}

// Submit test and calculate results
function submitTest() {
    clearInterval(timerInterval);
    
    // Calculate RIASEC scores
    const riasecScores = { R: 0, I: 0, A: 0, S: 0, E: 0, C: 0 };
    
    // Process RIASEC answers
    for (let i = 0; i < riasecQuestions.length; i++) {
        if (answers[i] !== null) {
            const selectedOption = riasecQuestions[i].options[answers[i]];
            if (selectedOption && selectedOption.type) {
                riasecScores[selectedOption.type]++;
            }
        }
    }
    
    // Calculate aptitude score
    let aptitudeCorrect = 0;
    for (let i = riasecQuestions.length; i < allQuestions.length; i++) {
        const aptitudeIndex = i - riasecQuestions.length;
        if (answers[i] === aptitudeQuestions[aptitudeIndex].correct) {
            aptitudeCorrect++;
        }
    }
    
    const aptitudePercentage = Math.round((aptitudeCorrect / aptitudeQuestions.length) * 100);
    
    // Display results
    displayResults(riasecScores, aptitudePercentage);
}

// Display test results with dual combinations
function displayResults(riasecScores, aptitudeScore) {
    console.log('Displaying results:', riasecScores, aptitudeScore);
    
    // Hide test container and show results
    document.getElementById('questionContainer').style.display = 'none';
    document.querySelector('.navigation').style.display = 'none';
    document.querySelector('.header').style.display = 'none';
    document.querySelectorAll('.section-header').forEach(el => el.style.display = 'none');
    
    // Find dominant and secondary RIASEC types
    const sortedTypes = Object.keys(riasecScores).sort((a, b) => riasecScores[b] - riasecScores[a]);
    const dominantType = sortedTypes[0];
    const secondaryType = sortedTypes[1];
    
    // Determine if it's a dual combination (secondary score within 2 points of dominant)
    const isDualType = riasecScores[dominantType] - riasecScores[secondaryType] <= 2;
    const personalityCode = isDualType ? `${dominantType}+${secondaryType}` : dominantType;
    
    // Get career recommendations based on personality code and aptitude score
    const careerRecommendations = getCareerRecommendations(personalityCode, aptitudeScore);
    
    // Create results HTML
    const riasecResultsHTML = `
        <div style="background: #f8f9fa; border-radius: 15px; padding: 25px; margin: 20px 0;">
            <h3>Your Personality Profile: ${personalityCode}</h3>
            <p style="margin: 15px 0; font-size: 16px; line-height: 1.6;">
                ${isDualType ? `You have a balanced combination of ${dominantType} and ${secondaryType} traits.` : `You are primarily a ${dominantType} type.`}
            </p>
            <div style="margin-top: 20px;">
                <h4>Career Recommendations (Aptitude Score: ${aptitudeScore}%):</h4>
                ${careerRecommendations.map(career => `
                    <span style="background: #667eea; color: white; padding: 8px 16px; border-radius: 20px; font-size: 14px; margin: 5px; display: inline-block;">
                        ${career}
                    </span>
                `).join('')}
            </div>
        </div>
    `;
    
    // Update results card
    document.getElementById('riasecResults').innerHTML = riasecResultsHTML;
    document.getElementById('aptitudeScore').innerHTML = `
        <div style="background: #f8f9fa; border-radius: 15px; padding: 25px; margin: 20px 0;">
            <h3>Aptitude Score</h3>
            <div style="font-size: 36px; font-weight: bold; color: #667eea; margin: 15px 0;">
                ${aptitudeScore}%
            </div>
            <p>You answered ${Math.round((aptitudeScore / 100) * aptitudeQuestions.length)} out of ${aptitudeQuestions.length} questions correctly</p>
        </div>
    `;
    
    // Show results card
    const resultsCard = document.getElementById('resultsCard');
    if (resultsCard) {
        resultsCard.classList.add('active');
        resultsCard.style.display = 'block';
    } else {
        console.error('Results card not found!');
    }
    
    // Add buttons for career exploration
    const buttonsContainer = document.createElement('div');
    buttonsContainer.style.cssText = 'margin-top: 30px; text-align: center;';
    
    const exploreBtn = document.createElement('button');
    exploreBtn.textContent = '🚀 Explore Career Roadmaps';
    exploreBtn.className = 'restart-btn';
    exploreBtn.style.cssText = 'background: #667eea; color: white; border: none; padding: 15px 30px; border-radius: 25px; font-size: 16px; cursor: pointer; margin: 10px; transition: all 0.3s ease;';
    exploreBtn.onclick = () => {
        console.log('Explore button clicked!');
        console.log('RIASEC Scores:', riasecScores);
        console.log('Aptitude Score:', aptitudeScore);
        console.log('Personality Code:', personalityCode);
        
        // Save results to localStorage and redirect
        localStorage.setItem('riasecScores', JSON.stringify(riasecScores));
        localStorage.setItem('aptitudeScore', aptitudeScore.toString());
        localStorage.setItem('personalityCode', personalityCode);
        localStorage.setItem('careerRecommendations', JSON.stringify(careerRecommendations));
        console.log('Data saved to localStorage');
        
        // Direct redirect with URL parameters as backup
        const params = new URLSearchParams({
            riasec: JSON.stringify(riasecScores),
            aptitude: aptitudeScore.toString(),
            personality: personalityCode
        });
        
        console.log('Redirecting to career-roadmap.html with params:', params.toString());
        window.location.href = `career-roadmap.html?${params.toString()}`;
    };
    
    const restartBtn = document.createElement('button');
    restartBtn.textContent = '🔄 Take Test Again';
    restartBtn.className = 'restart-btn';
    restartBtn.style.cssText = 'background: #6c757d; color: white; border: none; padding: 15px 30px; border-radius: 25px; font-size: 16px; cursor: pointer; margin: 10px; transition: all 0.3s ease;';
    restartBtn.onclick = () => location.reload();
    
    buttonsContainer.appendChild(exploreBtn);
    buttonsContainer.appendChild(restartBtn);
    document.getElementById('resultsCard').appendChild(buttonsContainer);
}

// Keyboard navigation
document.addEventListener('keydown', (e) => {
    if (e.key === 'ArrowLeft' && currentQuestion > 0) {
        previousQuestion();
    } else if (e.key === 'ArrowRight' && currentQuestion < allQuestions.length - 1) {
        nextQuestion();
    } else if (e.key >= '1' && e.key <= '4') {
        const optionIndex = parseInt(e.key) - 1;
        if (optionIndex < allQuestions[currentQuestion].options.length) {
            selectOption(optionIndex);
        }
    }
});

// Auto-save functionality
function autoSave() {
    localStorage.setItem('testProgress', JSON.stringify({
        currentQuestion,
        answers,
        timeRemaining
    }));
}

// Load saved progress
function loadProgress() {
    const saved = localStorage.getItem('testProgress');
    if (saved) {
        const progress = JSON.parse(saved);
        if (confirm('Resume previous test session?')) {
            currentQuestion = progress.currentQuestion;
            answers = progress.answers;
            timeRemaining = progress.timeRemaining;
            return true;
        }
    }
    return false;
}

// Initialize test when page loads
document.addEventListener('DOMContentLoaded', () => {
    if (!loadProgress()) {
        initializeTest();
    } else {
        startTimer();
        displayQuestion();
        updateProgress();
    }
    
    // Auto-save every 30 seconds
    setInterval(autoSave, 30000);
});

// Handle page visibility change (pause timer when tab is not active)
document.addEventListener('visibilitychange', () => {
    if (document.hidden) {
        clearInterval(timerInterval);
    } else {
        startTimer();
    }
});

// Prevent accidental page refresh
window.addEventListener('beforeunload', (e) => {
    if (currentQuestion > 0 && currentQuestion < allQuestions.length) {
        e.preventDefault();
        e.returnValue = 'Are you sure you want to leave? Your progress will be lost.';
        return e.returnValue;
    }
});
