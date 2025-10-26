// Career recommendation function based on personality code and aptitude score
function getCareerRecommendations(personalityCode, aptitudeScore) {
    // Determine aptitude band
    let aptitudeBand;
    if (aptitudeScore <= 10) aptitudeBand = '0-10';
    else if (aptitudeScore <= 20) aptitudeBand = '11-20';
    else if (aptitudeScore <= 30) aptitudeBand = '21-30';
    else if (aptitudeScore <= 40) aptitudeBand = '31-40';
    else aptitudeBand = '41-50';

    // Career mappings based on your specifications
    const careerMappings = {
        // Single RIASEC types
        'R': {
            '0-10': ['Laborer', 'Helper', 'Basic Technician', 'Agricultural Technicians', 'Biological Technicians', 'Bioinformatics Technicians'],
            '11-20': ['Technician Assistant', 'Machine Operator', 'Aerospace Engineering Technologists', 'Architectural and Civil Drafters', 'Automotive Engineering Technicians', 'Avionics Technicians', 'Aviation Inspectors'],
            '21-30': ['Skilled Technician', 'Electrician', 'Field Worker', 'Aircraft Mechanics', 'Automotive Service Technicians', 'Mechanical Supervisor', 'Geodetic Surveyors', 'Surveying and Mapping Technicians'],
            '31-40': ['Mechanical Engineer', 'Industrial Supervisor', 'Robotics Technician', 'Aerospace Engineers', 'Agricultural Engineers', 'Automotive Engineers', 'Bioengineers', 'Chemical Engineers', 'Civil Engineers', 'Computer Hardware Engineers', 'Electrical Engineers', 'Electronics Engineers', 'Industrial Engineers'],
            '41-50': ['Advanced Engineer', 'Technical Consultant', 'Operations Head', 'Surveyor', 'Landscape Architect', 'Water/Wastewater Engineers', 'Transportation Inspectors', 'Construction and Building Inspectors']
        },
        'I': {
            '0-10': ['Lab Assistant', 'Junior Support Staff', 'Basic Research Support', 'Data Entry in R&D', 'Social Science Research Assistants', 'Food Scientists'],
            '11-20': ['Remote Sensing Technicians', 'Geodetic Surveyors', 'Survey Researchers', 'Clinical Data Managers', 'Digital Forensics Analysts'],
            '21-30': ['Research Assistant', 'Data Analyst (junior)', 'Technician', 'Microbiologist', 'Environmental Engineering Technologists', 'Geological Technicians', 'Epidemiologists', 'Medical Scientists', 'Statisticians'],
            '31-40': ['Data Scientist', 'Software Developer', 'Research Analyst', 'Biochemists', 'Bioinformatics Scientists', 'Biologists', 'Computer and Information Research Scientists', 'Conservation Scientists', 'Environmental Scientists', 'Forensic Science Technicians', 'Hydrologists', 'Physicists'],
            '41-50': ['Senior Scientist', 'AI Researcher', 'Policy Analyst', 'Cybersecurity Head', 'Animal Scientists', 'Anthropologists', 'Astronomers', 'Economists', 'Geneticists', 'Geoscientists', 'Mathematicians', 'Political Scientists', 'Sociologists', 'Urban and Regional Planners', 'Zoologists']
        },
        'A': {
            '0-10': ['Helper in Arts/Design', 'Basic Craftsperson', 'Audio and Video Technicians', 'Floral Designers'],
            '11-20': ['Junior Designer', 'Assistant Content Creator', 'Set and Exhibit Designers', 'Photographers', 'Film and Video Editors', 'Media Programming Directors', 'Musicians and Singers', 'Dancers'],
            '21-30': ['Graphic Designer', 'Animator', 'Social Media Content Creator', 'Art Directors', 'Commercial and Industrial Designers', 'Interior Designers', 'Special Effects Artists', 'Music Directors', 'Web Developers'],
            '31-40': ['Creative Director', 'UX/UI Designer', 'Game Designer', 'Broadcast Announcers', 'Editors', 'Interpreters and Translators', 'News Analysts', 'Public Relations Specialists', 'Technical Writers', 'Writers and Authors'],
            '41-50': ['Creative Strategist', 'Art Entrepreneur', 'Media Innovator', 'Poets and Creative Writers', 'Librarians', 'Archivists', 'Curators']
        },
        'S': {
            '0-10': ['Community Helper', 'Support Staff', 'Receptionist', 'Basic Caregiver', 'Social and Human Service Assistants', 'Dental Assistants', 'Medical Assistants', 'Phlebotomists', 'Animal Trainers', 'Athletic Trainers'],
            '11-20': ['Adult Education Instructors', 'Child and Family Social Workers', 'Community Health Workers', 'Licensed Practical Nurses', 'Home Health Aides'],
            '21-30': ['Teacher (primary)', 'Social Worker', 'HR Associate', 'Recreation Workers', 'Health Education Specialists', 'Kindergarten Teachers', 'Middle School Teachers', 'Elementary School Teachers', 'Special Education Teachers', 'Physical Therapist Assistants'],
            '31-40': ['HR Manager', 'Counselor', 'Healthcare Administrator', 'Registered Nurses', 'Medical and Health Services Managers', 'Educational Counselors', 'Mental Health Counselors', 'Rehabilitation Counselors', 'Substance Abuse Counselors', 'Teachers', 'Marriage and Family Therapists'],
            '41-50': ['Organizational Leader', 'Senior Educator', 'Social Policy Maker', 'Clergy', 'Physicians', 'Surgeons', 'Psychologists', 'Physical Therapists', 'Speech-Language Pathologists', 'Veterinarians', 'Dentists', 'Optometrists', 'Pharmacists', 'Chiropractors', 'Podiatrists']
        },
        'E': {
            '0-10': ['Sales Assistant', 'Entry-level Support', 'Advertising Sales Agents', 'Insurance Sales Agents', 'Real Estate Sales Agents'],
            '11-20': ['Junior Sales Executive', 'Assistant Manager', 'Appraisers and Assessors', 'Financial Clerks', 'Medical Records Technicians'],
            '21-30': ['Business Development Associate', 'Team Lead', 'First-Line Supervisors', 'Fundraisers', 'Loan Officers', 'Marketing Specialists', 'Public Relations Specialists', 'Real Estate Brokers', 'Sales Engineers', 'Human Resources Specialists'],
            '31-40': ['Manager', 'Entrepreneur', 'Corporate Strategist', 'Advertising and Promotions Managers', 'Marketing Managers', 'Sales Managers', 'Financial Managers', 'Human Resources Managers', 'General and Operations Managers', 'Management Analysts', 'Legislators', 'Real Estate Brokers'],
            '41-50': ['CEO', 'Politician', 'Venture Capitalist', 'Senior Executive', 'Chief Executives', 'Investment Fund Managers', 'Lawyers', 'Judges', 'Legislators']
        },
        'C': {
            '0-10': ['Data Entry Clerk', 'Filing Assistant', 'Office Support Staff', 'Basic Accountant', 'Proofreaders', 'Statistical Assistants', 'Office Clerks'],
            '11-20': ['Bookkeeper', 'Accountants and Auditors', 'Budget Analysts', 'Compliance Officers'],
            '21-30': ['Admin Officer', 'Payroll Associate', 'Junior Auditor', 'Credit Analysts', 'Database Administrators', 'Purchasing Managers', 'Financial Clerks', 'Human Resources Specialists'],
            '31-40': ['Financial Analyst', 'Operations Planner', 'Senior Compliance Officer', 'Financial and Investment Analysts', 'Financial Risk Specialists', 'Human Resources Specialists', 'Logisticians', 'Purchasing Agents', 'Quality Control Systems Managers'],
            '41-50': ['CFO', 'Senior Compliance Manager', 'Risk Analyst', 'Actuaries', 'Financial Quantitative Analysts', 'Operations Research Analysts', 'Treasurers and Controllers']
        },

        // Dual RIASEC combinations
        'R+I': {
            '0-10': ['Lab Assistant', 'Junior Technician'],
            '11-20': ['Architectural and Civil Drafters'],
            '21-30': ['Electrical and Electronics Engineering Technicians', 'Environmental Engineering Technologists', 'Geological Technicians', 'Remote Sensing Technicians'],
            '31-40': ['Aerospace Engineers', 'Agricultural Engineers', 'Automotive Engineers', 'Bioengineers', 'Chemical Engineers', 'Civil Engineers', 'Computer Hardware Engineers', 'Electrical Engineers', 'Electronics Engineers', 'Industrial Engineers', 'Marine Engineers', 'Materials Engineers', 'Petroleum Engineers', 'Transportation Engineers'],
            '41-50': ['Advanced Engineer', 'Technical Consultant', 'Operations Head', 'Physicists', 'Mathematicians']
        },
        'R+A': {
            '0-10': ['Basic Craftsperson', 'Audio and Video Technicians'],
            '11-20': ['Set and Exhibit Designers'],
            '21-30': ['Landscape Architects', 'Floral Designers', 'Photographers', 'Commercial and Industrial Designers'],
            '31-40': ['Industrial Designer', 'Fashion Designer', 'Interior Designers', 'Special Effects Artists'],
            '41-50': ['Animator', 'Advanced Industrial Designer']
        },
        'R+S': {
            '0-10': ['Dental Assistants', 'Medical Assistants', 'Phlebotomists'],
            '11-20': ['Licensed Practical Nurses'],
            '21-30': ['Physical Therapist Assistants', 'Occupational Therapy Assistants', 'Health Education Specialists', 'Recreational Therapists'],
            '31-40': ['Physical Therapists', 'Occupational Therapists', 'Registered Nurses', 'Athletic Trainers'],
            '41-50': ['Medical and Health Services Managers', 'Optometrists', 'Pharmacists', 'Veterinarians', 'Surgeons']
        },
        'R+E': {
            '0-10': ['Sales Engineers'],
            '11-20': ['Automotive Service Managers', 'First-Line Supervisors of Mechanics'],
            '21-30': ['Construction Managers'],
            '31-40': ['Architectural and Engineering Managers', 'Facilities Managers'],
            '41-50': ['General and Operations Managers', 'Chief Engineers']
        },
        'R+C': {
            '0-10': ['Quality Control Inspectors'],
            '11-20': ['Surveying and Mapping Technicians'],
            '21-30': ['Architectural and Civil Drafters'],
            '31-40': ['Construction and Building Inspectors', 'Logistics Analysts', 'Industrial Engineering Technicians'],
            '41-50': ['Operations Research Analysts', 'Urban and Regional Planners']
        },
        'I+A': {
            '0-10': ['Market Research Analysts'],
            '11-20': ['UX/UI Designer'],
            '21-30': ['Graphic Designer', 'Editor'],
            '31-40': ['Bioinformatics Scientist', 'Web Developer', 'Art Director'],
            '41-50': ['Art Entrepreneur', 'Media Innovator']
        },
        'I+S': {
            '0-10': ['Social Science Research Assistants'],
            '11-20': ['Community Health Workers'],
            '21-30': ['Epidemiologists', 'Mental Health Counselors'],
            '31-40': ['Psychologists', 'Genetic Counselors', 'Medical Scientists'],
            '41-50': ['Public Health Educators', 'Forensic Psychologists']
        },
        'I+E': {
            '0-10': ['Marketing Research Analysts'],
            '11-20': ['Financial Analyst'],
            '21-30': ['Data Scientist'],
            '31-40': ['Management Analysts', 'Operations Research Analysts'],
            '41-50': ['Chief Information Officers', 'Chief Technology Officers']
        },
        'I+C': {
            '0-10': ['Data Entry', 'Bookkeeping'],
            '11-20': ['Database Administrators'],
            '21-30': ['Financial Clerks'],
            '31-40': ['Statistical Assistants', 'Data Analysts', 'Actuaries'],
            '41-50': ['Financial Risk Specialists', 'Budget Analysts']
        },
        'A+S': {
            '0-10': ['Public Relations Specialists', 'Social Media Managers'],
            '11-20': ['Interpreters and Translators'],
            '21-30': ['Art Therapists', 'Music Therapists'],
            '31-40': ['Technical Writers', 'Journalists', 'High School Teachers'],
            '41-50': ['Speech-Language Pathologists', 'Poets and Creative Writers']
        },
        'A+E': {
            '0-10': ['Creative Sales Agents', 'Marketing Assistants'],
            '11-20': ['Public Relations Specialists'],
            '21-30': ['Brand Managers'],
            '31-40': ['Advertising and Promotions Managers', 'Marketing Managers'],
            '41-50': ['Creative Director', 'Arts Entrepreneur']
        },
        'A+C': {
            '0-10': ['Editorial Assistants'],
            '11-20': ['Desktop Publishers'],
            '21-30': ['Web Developers', 'Librarians'],
            '31-40': ['Technical Writers', 'Broadcast Announcers', 'Editors'],
            '41-50': ['Archivists', 'Curators']
        },
        'S+E': {
            '0-10': ['Customer Service Representatives', 'Human Resources Assistants'],
            '11-20': ['Training and Development Specialists'],
            '21-30': ['Recruiters'],
            '31-40': ['Human Resources Managers', 'School Administrators'],
            '41-50': ['Chief Executives', 'Social and Community Service Managers']
        },
        'S+C': {
            '0-10': ['Office Assistants', 'Front Desk Clerks'],
            '11-20': ['Human Resources Specialists'],
            '21-30': ['Medical Records and Health Information Technicians'],
            '31-40': ['Compliance Officers', 'Accountants'],
            '41-50': ['Financial Managers', 'Operations Managers']
        },
        'E+C': {
            '0-10': ['Office Managers'],
            '11-20': ['Credit Analysts'],
            '21-30': ['Purchasing Managers'],
            '31-40': ['Financial Managers', 'Budget Analysts'],
            '41-50': ['Chief Financial Officers', 'Actuaries']
        }
    };

    // Get recommendations for the personality code
    const recommendations = careerMappings[personalityCode];
    if (recommendations && recommendations[aptitudeBand]) {
        return recommendations[aptitudeBand].slice(0, 8); // Return top 8 recommendations
    }

    // Fallback for single types if dual combination not found
    if (personalityCode.includes('+')) {
        const primaryType = personalityCode.split('+')[0];
        const fallbackRecommendations = careerMappings[primaryType];
        if (fallbackRecommendations && fallbackRecommendations[aptitudeBand]) {
            return fallbackRecommendations[aptitudeBand].slice(0, 8);
        }
    }

    // Default fallback
    return ['Software Developer', 'Teacher', 'Manager', 'Designer', 'Analyst', 'Consultant', 'Specialist', 'Coordinator'];
}
