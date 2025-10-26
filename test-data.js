// PHASE-BASED RIASEC Questions Data (20 Questions Total - 10 per phase)

// PHASE 1: Initial Personality Discovery (10 Questions)
const phase1Questions = [
    {
        question: "When planning a group trip, what role do you take?",
        options: [
            { text: "Organize everything", type: "E" },
            { text: "Share ideas but let others decide", type: "S" },
            { text: "Follow the group's plan", type: "C" },
            { text: "Question decisions if flawed", type: "I" }
        ]
    },
    {
        question: "In an argument with someone unreasonable, what do you do?",
        options: [
            { text: "Calmly explain my point", type: "S" },
            { text: "Leave the conversation", type: "C" },
            { text: "Debate strongly to win", type: "E" },
            { text: "Use humor to diffuse tension", type: "A" }
        ]
    },
    {
        question: "A team project deadline is near. How do you act?",
        options: [
            { text: "Take charge and assign roles", type: "E" },
            { text: "Help the team collaborate", type: "S" },
            { text: "Follow instructions carefully", type: "C" },
            { text: "Find innovative shortcuts", type: "A" }
        ]
    },
    {
        question: "How do you prefer working?",
        options: [
            { text: "Leading a team", type: "E" },
            { text: "Working with others equally", type: "S" },
            { text: "Working alone", type: "I" },
            { text: "Taking flexible roles", type: "A" }
        ]
    },
    {
        question: "Your friend is stressed about exams. What do you do?",
        options: [
            { text: "Motivate with a pep talk", type: "S" },
            { text: "Help them study", type: "C" },
            { text: "Leave them to handle it", type: "I" },
            { text: "Make them laugh", type: "A" }
        ]
    },
    {
        question: "You are assigned a boring task. How do you handle it?",
        options: [
            { text: "Try to make it interesting", type: "A" },
            { text: "Ask for help or collaborate", type: "S" },
            { text: "Complete it systematically", type: "C" },
            { text: "Find ways to improve it", type: "I" }
        ]
    },
    {
        question: "How do you make decisions?",
        options: [
            { text: "Based on facts & data", type: "I" },
            { text: "Based on what feels right", type: "A" },
            { text: "Based on team consensus", type: "S" },
            { text: "Based on established rules", type: "C" }
        ]
    },
    {
        question: "When facing a tough challenge, your first thought is:",
        options: [
            { text: "I'll find a new way", type: "A" },
            { text: "I'll break it into steps", type: "C" },
            { text: "I'll research solutions", type: "I" },
            { text: "I'll get help from others", type: "S" }
        ]
    },
    {
        question: "At a party, you are likely to:",
        options: [
            { text: "Be the one telling stories", type: "A" },
            { text: "Talk to a small group", type: "S" },
            { text: "Observe and listen", type: "I" },
            { text: "Help organize activities", type: "C" }
        ]
    },
    {
        question: "You are given a creative assignment. You:",
        options: [
            { text: "Come up with unique ideas", type: "A" },
            { text: "Research deeply before starting", type: "I" },
            { text: "Collaborate with others", type: "S" },
            { text: "Follow a structured approach", type: "C" }
        ]
    },
    {
        question: "How do you handle last-minute changes?",
        options: [
            { text: "Stay flexible and adapt", type: "A" },
            { text: "Try to minimize disruptions", type: "C" },
            { text: "Analyze the impact first", type: "I" },
            { text: "Discuss with the team", type: "S" }
        ]
    },
    {
        question: "When learning something new, you prefer:",
        options: [
            { text: "Experimenting hands-on", type: "R" },
            { text: "Reading and analyzing deeply", type: "I" },
            { text: "Learning with others", type: "S" },
            { text: "Following step-by-step guides", type: "C" }
        ]
    },
    {
        question: "Your ideal work environment is:",
        options: [
            { text: "Creative and free-flowing", type: "A" },
            { text: "Collaborative and social", type: "S" },
            { text: "Quiet and focused", type: "I" },
            { text: "Organized and structured", type: "C" }
        ]
    },
    {
        question: "How do you react to failure?",
        options: [
            { text: "Look for creative alternatives", type: "A" },
            { text: "Analyze what went wrong", type: "I" },
            { text: "Seek support from others", type: "S" },
            { text: "Review and follow procedures", type: "C" }
        ]
    },
    {
        question: "You get a sudden leadership opportunity. You:",
        options: [
            { text: "Take charge confidently", type: "E" },
            { text: "Support the leader", type: "S" },
            { text: "Plan carefully before acting", type: "I" },
            { text: "Follow established protocols", type: "C" }
        ]
    },
    {
        question: "When solving problems, your approach is:",
        options: [
            { text: "Analytical and step-by-step", type: "I" },
            { text: "Creative and outside-the-box", type: "A" },
            { text: "Collaborative with others", type: "S" },
            { text: "Systematic and methodical", type: "C" }
        ]
    },
    {
        question: "You are best at:",
        options: [
            { text: "Coming up with ideas", type: "A" },
            { text: "Following plans and rules", type: "C" },
            { text: "Understanding complex concepts", type: "I" },
            { text: "Working with people", type: "S" }
        ]
    },
    {
        question: "How do you handle conflicts?",
        options: [
            { text: "Find a compromise", type: "S" },
            { text: "Use logic to resolve", type: "I" },
            { text: "Think creatively for solutions", type: "A" },
            { text: "Follow proper procedures", type: "C" }
        ]
    },
    {
        question: "You prefer to spend free time:",
        options: [
            { text: "Exploring hobbies", type: "A" },
            { text: "Reading/learning", type: "I" },
            { text: "Socializing with friends", type: "S" },
            { text: "Organizing personal tasks", type: "C" }
        ]
    },
    {
        question: "Your dream job involves:",
        options: [
            { text: "Creating something new", type: "A" },
            { text: "Leading a team", type: "E" },
            { text: "Researching and discovering", type: "I" },
            { text: "Helping others", type: "S" }
        ]
    },
    {
        question: "When deadlines approach, you:",
        options: [
            { text: "Innovate to save time", type: "A" },
            { text: "Follow the process carefully", type: "C" },
            { text: "Analyze priorities", type: "I" },
            { text: "Coordinate with the team", type: "S" }
        ]
    },
    {
        question: "Your preferred role in a team is:",
        options: [
            { text: "Idea generator", type: "A" },
            { text: "Organizer", type: "C" },
            { text: "Researcher", type: "I" },
            { text: "Facilitator", type: "S" }
        ]
    },
    {
        question: "When someone disagrees with you, you:",
        options: [
            { text: "Try to convince them", type: "E" },
            { text: "Consider their viewpoint", type: "S" },
            { text: "Analyze both perspectives", type: "I" },
            { text: "Refer to guidelines", type: "C" }
        ]
    },
    {
        question: "You learn best by:",
        options: [
            { text: "Doing and experimenting", type: "R" },
            { text: "Discussing with others", type: "S" },
            { text: "Reading and reflecting", type: "I" },
            { text: "Following structured lessons", type: "C" }
        ]
    },
    {
        question: "You are most satisfied when:",
        options: [
            { text: "Creating something", type: "A" },
            { text: "Helping others", type: "S" },
            { text: "Solving complex problems", type: "I" },
            { text: "Completing tasks efficiently", type: "C" }
        ]
    },
    {
        question: "If you fail at something, you:",
        options: [
            { text: "Try a new method", type: "A" },
            { text: "Reassess and analyze", type: "I" },
            { text: "Seek advice from others", type: "S" },
            { text: "Review the process", type: "C" }
        ]
    },
    {
        question: "You value most at work:",
        options: [
            { text: "Creativity", type: "A" },
            { text: "Stability", type: "C" },
            { text: "Learning", type: "I" },
            { text: "Relationships", type: "S" }
        ]
    },
    {
        question: "When things go wrong, you:",
        options: [
            { text: "Find new solutions", type: "A" },
            { text: "Stick to the plan", type: "C" },
            { text: "Analyze the situation", type: "I" },
            { text: "Rally the team", type: "S" }
        ]
    },
    {
        question: "Your biggest motivator is:",
        options: [
            { text: "Recognition", type: "E" },
            { text: "Learning", type: "I" },
            { text: "Helping others", type: "S" },
            { text: "Achievement", type: "C" }
        ]
    },
    {
        question: "You feel successful when:",
        options: [
            { text: "People appreciate you", type: "S" },
            { text: "You solve tough problems", type: "I" },
            { text: "You create something unique", type: "A" },
            { text: "You meet all goals", type: "C" }
        ]
    },
    {
        question: "You like roles that are:",
        options: [
            { text: "Flexible", type: "A" },
            { text: "Structured", type: "C" },
            { text: "Analytical", type: "I" },
            { text: "People-focused", type: "S" }
        ]
    },
    {
        question: "When under pressure, you:",
        options: [
            { text: "Innovate", type: "A" },
            { text: "Follow the rules", type: "C" },
            { text: "Think logically", type: "I" },
            { text: "Seek team support", type: "S" }
        ]
    },
    {
        question: "You prefer tasks that are:",
        options: [
            { text: "Creative", type: "A" },
            { text: "Logical", type: "I" },
            { text: "Social", type: "S" },
            { text: "Systematic", type: "C" }
        ]
    },
    {
        question: "You're most comfortable when:",
        options: [
            { text: "Expressing yourself", type: "A" },
            { text: "Working with data", type: "I" },
            { text: "Helping others", type: "S" },
            { text: "Following procedures", type: "C" }
        ]
    },
    {
        question: "Your natural strength is:",
        options: [
            { text: "Innovation", type: "A" },
            { text: "Analysis", type: "I" },
            { text: "Communication", type: "S" },
            { text: "Organization", type: "C" }
        ]
    },
    {
        question: "You enjoy work that involves:",
        options: [
            { text: "Building things", type: "R" },
            { text: "Researching", type: "I" },
            { text: "Creating art", type: "A" },
            { text: "Teaching others", type: "S" }
        ]
    },
    {
        question: "When making plans, you focus on:",
        options: [
            { text: "Possibilities", type: "A" },
            { text: "Details", type: "C" },
            { text: "Research", type: "I" },
            { text: "People involved", type: "S" }
        ]
    },
    {
        question: "Your approach to new situations is:",
        options: [
            { text: "Experimental", type: "A" },
            { text: "Cautious", type: "C" },
            { text: "Analytical", type: "I" },
            { text: "Collaborative", type: "S" }
        ]
    },
    {
        question: "You work best when:",
        options: [
            { text: "Free to be creative", type: "A" },
            { text: "Given clear structure", type: "C" },
            { text: "Allowed to think deeply", type: "I" },
            { text: "Working with others", type: "S" }
        ]
    },
    {
        question: "Your ideal day includes:",
        options: [
            { text: "Creating something new", type: "A" },
            { text: "Solving problems", type: "I" },
            { text: "Connecting with people", type: "S" },
            { text: "Completing organized tasks", type: "C" }
        ]
    },
    {
        question: "When choosing a career, you prioritize:",
        options: [
            { text: "Creative expression", type: "A" },
            { text: "Intellectual challenge", type: "I" },
            { text: "Helping society", type: "S" },
            { text: "Job security", type: "C" }
        ]
    },
    {
        question: "You're energized by:",
        options: [
            { text: "New ideas", type: "A" },
            { text: "Complex problems", type: "I" },
            { text: "Social interaction", type: "S" },
            { text: "Organized systems", type: "C" }
        ]
    },
    {
        question: "Your communication style is:",
        options: [
            { text: "Expressive and creative", type: "A" },
            { text: "Logical and precise", type: "I" },
            { text: "Warm and supportive", type: "S" },
            { text: "Clear and structured", type: "C" }
        ]
    },
    {
        question: "You handle stress by:",
        options: [
            { text: "Finding creative outlets", type: "A" },
            { text: "Analyzing the situation", type: "I" },
            { text: "Talking to friends", type: "S" },
            { text: "Making organized plans", type: "C" }
        ]
    },
    {
        question: "Your leadership style is:",
        options: [
            { text: "Inspirational", type: "A" },
            { text: "Thoughtful", type: "I" },
            { text: "Supportive", type: "S" },
            { text: "Directive", type: "E" }
        ]
    },
    {
        question: "You prefer feedback that is:",
        options: [
            { text: "Encouraging creativity", type: "A" },
            { text: "Detailed and specific", type: "I" },
            { text: "Supportive and personal", type: "S" },
            { text: "Clear and actionable", type: "C" }
        ]
    },
    {
        question: "When working on projects, you focus on:",
        options: [
            { text: "Innovation", type: "A" },
            { text: "Accuracy", type: "I" },
            { text: "Team success", type: "S" },
            { text: "Efficiency", type: "C" }
        ]
    },
    {
        question: "Your decision-making process involves:",
        options: [
            { text: "Brainstorming options", type: "A" },
            { text: "Analyzing data", type: "I" },
            { text: "Consulting others", type: "S" },
            { text: "Following procedures", type: "C" }
        ]
    },
    {
        question: "You're motivated by work that offers:",
        options: [
            { text: "Creative freedom", type: "A" },
            { text: "Intellectual stimulation", type: "I" },
            { text: "Social impact", type: "S" },
            { text: "Clear expectations", type: "C" }
        ]
    },
    {
        question: "Your ideal career involves:",
        options: [
            { text: "Creativity and freedom", type: "A" },
            { text: "Data and analysis", type: "I" },
            { text: "Helping and connecting", type: "S" },
            { text: "Managing and leading", type: "E" }
        ]
    }
];

// Aptitude Questions Data (50 Questions)
const aptitudeQuestions = [
    // Numerical Ability & Quantitative Reasoning (Questions 1-10)
    {
        question: "If 8 workers build a wall in 12 days, how many workers are needed to build it in 6 days?",
        options: ["12", "16", "18", "20"],
        correct: 1
    },
    {
        question: "A train travels 240 km in 3 hours. What is its speed in km/h?",
        options: ["70", "75", "80", "85"],
        correct: 2
    },
    {
        question: "If 25% of a number is 60, what is 40% of that number?",
        options: ["90", "96", "100", "104"],
        correct: 1
    },
    {
        question: "The ratio of boys to girls in a class is 3:2. If there are 15 boys, how many girls are there?",
        options: ["8", "10", "12", "15"],
        correct: 1
    },
    {
        question: "A shopkeeper sells an item for ₹450 at a 10% profit. What was the cost price?",
        options: ["₹400", "₹405", "₹409", "₹415"],
        correct: 2
    },
    {
        question: "If x + 5 = 12, what is the value of 2x + 3?",
        options: ["17", "19", "21", "23"],
        correct: 0
    },
    {
        question: "The average of 5 numbers is 20. If one number is 30, what is the average of the remaining 4?",
        options: ["17.5", "18", "18.5", "19"],
        correct: 0
    },
    {
        question: "A rectangle has length 12 cm and width 8 cm. What is its area?",
        options: ["88 sq cm", "92 sq cm", "96 sq cm", "100 sq cm"],
        correct: 2
    },
    {
        question: "If 3^x = 81, what is the value of x?",
        options: ["3", "4", "5", "6"],
        correct: 1
    },
    {
        question: "A car covers 150 km in 2.5 hours. How much distance will it cover in 4 hours at the same speed?",
        options: ["220 km", "230 km", "240 km", "250 km"],
        correct: 2
    },

    // Series & Pattern Recognition (Questions 11-20)
    {
        question: "Find the next number in the series: 2, 6, 12, 20, 30, ?",
        options: ["40", "42", "44", "46"],
        correct: 1
    },
    {
        question: "Complete the series: A, D, G, J, ?",
        options: ["K", "L", "M", "N"],
        correct: 2
    },
    {
        question: "Find the missing number: 5, 11, 23, 47, ?",
        options: ["91", "93", "95", "97"],
        correct: 2
    },
    {
        question: "What comes next: 1, 4, 9, 16, 25, ?",
        options: ["30", "32", "36", "40"],
        correct: 2
    },
    {
        question: "Complete: 3, 7, 15, 31, ?",
        options: ["59", "61", "63", "65"],
        correct: 2
    },
    {
        question: "Find the pattern: Z, Y, X, W, ?",
        options: ["T", "U", "V", "S"],
        correct: 2
    },
    {
        question: "Next in series: 2, 5, 11, 23, ?",
        options: ["45", "47", "49", "51"],
        correct: 1
    },
    {
        question: "Complete: 1, 1, 2, 3, 5, 8, ?",
        options: ["11", "12", "13", "14"],
        correct: 2
    },
    {
        question: "Find next: 100, 81, 64, 49, ?",
        options: ["30", "32", "36", "40"],
        correct: 2
    },
    {
        question: "Pattern: AZ, BY, CX, ?",
        options: ["DW", "DV", "EW", "EV"],
        correct: 0
    },

    // Logical Reasoning (Questions 21-30)
    {
        question: "All roses are flowers. Some flowers are red. Therefore:",
        options: ["All roses are red", "Some roses are red", "No roses are red", "Cannot be determined"],
        correct: 3
    },
    {
        question: "If BOOK is coded as CPPL, how is WORD coded?",
        options: ["XPSE", "XQSE", "YPSE", "YQSE"],
        correct: 0
    },
    {
        question: "In a certain code, RAIN is written as 1234. How is GAIN written?",
        options: ["5234", "6234", "7234", "8234"],
        correct: 0
    },
    {
        question: "If Monday is the 1st, what day is the 15th?",
        options: ["Sunday", "Monday", "Tuesday", "Wednesday"],
        correct: 1
    },
    {
        question: "A is taller than B. C is shorter than B. Who is the shortest?",
        options: ["A", "B", "C", "Cannot determine"],
        correct: 2
    },
    {
        question: "If all cats are animals and some animals are pets, then:",
        options: ["All cats are pets", "Some cats are pets", "No cats are pets", "Cannot determine"],
        correct: 3
    },
    {
        question: "DELHI : INDIA :: PARIS : ?",
        options: ["EUROPE", "FRANCE", "CITY", "COUNTRY"],
        correct: 1
    },
    {
        question: "If FRIEND is coded as GSJFOE, how is MOTHER coded?",
        options: ["NPUIFS", "NPUIFS", "OPUIFS", "NQUIFS"],
        correct: 0
    },
    {
        question: "In a row of 20 people, A is 7th from left. What is A's position from right?",
        options: ["13th", "14th", "15th", "16th"],
        correct: 1
    },
    {
        question: "If yesterday was Wednesday, what will be the day after tomorrow?",
        options: ["Friday", "Saturday", "Sunday", "Monday"],
        correct: 1
    },

    // Verbal Ability (Questions 31-40)
    {
        question: "Choose the synonym of 'Abundant':",
        options: ["Scarce", "Plentiful", "Limited", "Rare"],
        correct: 1
    },
    {
        question: "What is the antonym of 'Optimistic'?",
        options: ["Hopeful", "Positive", "Pessimistic", "Confident"],
        correct: 2
    },
    {
        question: "Complete the analogy: Book : Author :: Painting : ?",
        options: ["Canvas", "Brush", "Artist", "Color"],
        correct: 2
    },
    {
        question: "Choose the correctly spelled word:",
        options: ["Recieve", "Receive", "Receve", "Receiv"],
        correct: 1
    },
    {
        question: "What does 'Procrastinate' mean?",
        options: ["To hurry", "To delay", "To complete", "To organize"],
        correct: 1
    },
    {
        question: "Find the odd one out:",
        options: ["Happy", "Joyful", "Elated", "Sad"],
        correct: 3
    },
    {
        question: "Choose the correct sentence:",
        options: ["He don't like coffee", "He doesn't likes coffee", "He doesn't like coffee", "He not like coffee"],
        correct: 2
    },
    {
        question: "What is a group of lions called?",
        options: ["Herd", "Flock", "Pride", "Pack"],
        correct: 2
    },
    {
        question: "Choose the synonym of 'Meticulous':",
        options: ["Careless", "Careful", "Quick", "Slow"],
        correct: 1
    },
    {
        question: "Complete: 'A stitch in time saves ____'",
        options: ["seven", "eight", "nine", "ten"],
        correct: 2
    },

    // Mixed Reasoning & General Aptitude (Questions 41-50)
    {
        question: "If it takes 5 machines 5 minutes to make 5 widgets, how long does it take 100 machines to make 100 widgets?",
        options: ["5 minutes", "20 minutes", "100 minutes", "500 minutes"],
        correct: 0
    },
    {
        question: "A clock shows 3:15. What is the angle between the hour and minute hands?",
        options: ["0°", "7.5°", "15°", "22.5°"],
        correct: 1
    },
    {
        question: "If you rearrange the letters 'CIFAIPC', you get the name of a:",
        options: ["Country", "Ocean", "Animal", "City"],
        correct: 1
    },
    {
        question: "What comes next in the sequence: J, F, M, A, M, J, ?",
        options: ["J", "A", "S", "O"],
        correct: 0
    },
    {
        question: "A father is 3 times as old as his son. In 12 years, he will be twice as old. How old is the son now?",
        options: ["10", "12", "14", "16"],
        correct: 1
    },
    {
        question: "Which number should replace the question mark: 2, 6, 12, 20, 30, ?",
        options: ["40", "42", "44", "46"],
        correct: 1
    },
    {
        question: "If EARTH is coded as 51238, how is HEART coded?",
        options: ["85123", "85132", "81523", "81532"],
        correct: 0
    },
    {
        question: "A man walks 3 km north, then 4 km east. How far is he from his starting point?",
        options: ["5 km", "6 km", "7 km", "8 km"],
        correct: 0
    },
    {
        question: "Find the odd one: Asia, Africa, India, Europe",
        options: ["Asia", "Africa", "India", "Europe"],
        correct: 2
    },
    {
        question: "Which is a synonym for Frugal?",
        options: ["Wasteful", "Generous", "Economical", "Rich"],
        correct: 2
    }
];
