// Career Demo JavaScript
class CareerExplorer {
    constructor() {
        this.careers = [];
        this.filteredCareers = [];
        this.init();
    }

    async init() {
        await this.loadCareerData();
        this.setupEventListeners();
        this.displayFeaturedCareers();
        this.updateStats();
    }

    async loadCareerData() {
        // Sample career data - in production this would come from the complete-career-database.js
        this.careers = [
            {
                code: "15-1221.00",
                title: "Computer Systems Analysts",
                field: "technology",
                level: 4,
                description: "Analyze science, engineering, business, and other data processing problems to implement and improve computer systems.",
                overview: "Design and implement computer systems and networks for organizations"
            },
            {
                code: "29-1291.00",
                title: "Acupuncturists",
                field: "healthcare",
                level: 5,
                description: "Diagnose, treat, and prevent disorders by stimulating specific acupuncture points within the body using acupuncture needles.",
                overview: "Licensed healthcare professionals who treat patients using acupuncture techniques"
            },
            {
                code: "25-2059.01",
                title: "Adapted Physical Education Specialists",
                field: "education",
                level: 5,
                description: "Provide individualized physical education instruction or services to children, youth, or adults with exceptional physical needs.",
                overview: "Specialists who design and implement physical education programs for students with disabilities"
            },
            {
                code: "17-2041.00",
                title: "Chemical Engineers",
                field: "engineering",
                level: 4,
                description: "Design chemical plant equipment and devise processes for manufacturing chemicals and products.",
                overview: "Apply principles of chemistry, biology, physics, and math to solve problems involving production or use of chemicals"
            },
            {
                code: "11-3021.00",
                title: "Computer and Information Systems Managers",
                field: "technology",
                level: 5,
                description: "Plan, direct, or coordinate activities in such fields as electronic data processing, information systems, systems analysis, and computer programming.",
                overview: "Plan and coordinate computer-related activities in an organization"
            },
            {
                code: "29-1141.00",
                title: "Registered Nurses",
                field: "healthcare",
                level: 4,
                description: "Assess patient health problems and needs, develop and implement nursing care plans, and maintain medical records.",
                overview: "Provide and coordinate patient care and educate patients and the public about various health conditions"
            },
            {
                code: "13-2011.00",
                title: "Accountants and Auditors",
                field: "business",
                level: 4,
                description: "Examine, analyze, and interpret accounting records to prepare financial statements, give advice, or audit and evaluate statements.",
                overview: "Prepare and examine financial records, ensure accuracy, and ensure taxes are paid properly"
            },
            {
                code: "25-1022.00",
                title: "Mathematical Science Teachers, Postsecondary",
                field: "education",
                level: 5,
                description: "Teach courses pertaining to mathematical concepts, statistics, and actuarial science and to the application of original and standardized mathematical techniques.",
                overview: "Teach mathematics courses at colleges and universities"
            },
            {
                code: "17-2051.00",
                title: "Civil Engineers",
                field: "engineering",
                level: 4,
                description: "Perform engineering duties in planning, designing, and overseeing construction and maintenance of building structures and facilities.",
                overview: "Design, build, and supervise infrastructure projects and systems in the public and private sector"
            },
            {
                code: "15-1132.00",
                title: "Software Developers, Applications",
                field: "technology",
                level: 4,
                description: "Develop, create, and modify general computer applications software or specialized utility programs.",
                overview: "Design computer or mobile applications for consumers and businesses"
            }
        ];
        
        this.filteredCareers = [...this.careers];
    }

    setupEventListeners() {
        const searchBtn = document.getElementById('searchBtn');
        const searchInput = document.getElementById('careerSearch');
        const fieldFilter = document.getElementById('fieldFilter');
        const levelFilter = document.getElementById('levelFilter');
        const closeModal = document.getElementById('closeModal');

        searchBtn.addEventListener('click', () => this.performSearch());
        searchInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.performSearch();
        });
        
        fieldFilter.addEventListener('change', () => this.applyFilters());
        levelFilter.addEventListener('change', () => this.applyFilters());
        
        closeModal.addEventListener('click', () => this.closeModal());
        
        // Close modal when clicking outside
        window.addEventListener('click', (e) => {
            const modal = document.getElementById('careerModal');
            if (e.target === modal) {
                this.closeModal();
            }
        });
    }

    performSearch() {
        const searchTerm = document.getElementById('careerSearch').value.toLowerCase();
        const resultsSection = document.getElementById('resultsSection');
        
        if (searchTerm.trim() === '') {
            resultsSection.style.display = 'none';
            return;
        }

        this.filteredCareers = this.careers.filter(career => 
            career.title.toLowerCase().includes(searchTerm) ||
            career.description.toLowerCase().includes(searchTerm) ||
            career.field.toLowerCase().includes(searchTerm)
        );

        this.applyFilters();
        this.displaySearchResults();
        resultsSection.style.display = 'block';
        resultsSection.scrollIntoView({ behavior: 'smooth' });
    }

    applyFilters() {
        const fieldFilter = document.getElementById('fieldFilter').value;
        const levelFilter = document.getElementById('levelFilter').value;
        
        let filtered = [...this.filteredCareers];
        
        if (fieldFilter) {
            filtered = filtered.filter(career => career.field === fieldFilter);
        }
        
        if (levelFilter) {
            filtered = filtered.filter(career => career.level.toString() === levelFilter);
        }
        
        this.filteredCareers = filtered;
        
        // If we're showing search results, update them
        const resultsSection = document.getElementById('resultsSection');
        if (resultsSection.style.display !== 'none') {
            this.displaySearchResults();
        }
    }

    displayFeaturedCareers() {
        const container = document.getElementById('featuredCareers');
        const featured = this.careers.slice(0, 6); // Show first 6 as featured
        
        container.innerHTML = featured.map(career => this.createCareerCard(career)).join('');
    }

    displaySearchResults() {
        const container = document.getElementById('searchResults');
        
        if (this.filteredCareers.length === 0) {
            container.innerHTML = '<div class="no-results"><h3>No careers found</h3><p>Try adjusting your search terms or filters.</p></div>';
            return;
        }
        
        container.innerHTML = this.filteredCareers.map(career => this.createCareerCard(career)).join('');
    }

    createCareerCard(career) {
        return `
            <div class="career-card" onclick="careerExplorer.showCareerDetails('${career.code}')">
                <h3>${career.title}</h3>
                <div>
                    <span class="career-field">${this.capitalizeField(career.field)}</span>
                    <span class="career-level">Level ${career.level}</span>
                </div>
                <p class="career-description">${career.description}</p>
            </div>
        `;
    }

    generateDetailedEducationLevels(detailedLevels, distinctions) {
        return `
            <div class="detailed-education-levels">
                <h2>🎓 Detailed Education Level Descriptions</h2>
                
                ${distinctions ? `
                <div class="education-distinctions">
                    <h3>📋 Education Level Distinctions</h3>
                    <div class="distinction-grid">
                        <div class="distinction-item">
                            <h4>Graduate Level</h4>
                            <p>${distinctions.graduate}</p>
                        </div>
                        <div class="distinction-item">
                            <h4>Postgraduate Level</h4>
                            <p>${distinctions.postgraduate}</p>
                        </div>
                        <div class="distinction-item">
                            <h4>Doctorate Level</h4>
                            <p>${distinctions.doctorate}</p>
                        </div>
                    </div>
                </div>
                ` : ''}

                ${detailedLevels.graduate ? `
                <div class="education-level-detail">
                    <h3>🎓 Graduate Level (Master's Programs)</h3>
                    <p><strong>Definition:</strong> ${detailedLevels.graduate.definition}</p>
                    <p><strong>Duration:</strong> ${detailedLevels.graduate.duration}</p>
                    
                    <h4>Program Types:</h4>
                    <ul>
                        ${Object.entries(detailedLevels.graduate.types).map(([key, value]) => 
                            `<li><strong>${key.charAt(0).toUpperCase() + key.slice(1)}:</strong> ${value}</li>`
                        ).join('')}
                    </ul>
                    
                    <h4>Top Specializations:</h4>
                    <ul>
                        ${detailedLevels.graduate.specializations.slice(0, 5).map(spec => `<li>${spec}</li>`).join('')}
                    </ul>
                </div>
                ` : ''}

                ${detailedLevels.postgraduate ? `
                <div class="education-level-detail">
                    <h3>🏆 Postgraduate Level (Advanced Programs)</h3>
                    <p><strong>Definition:</strong> ${detailedLevels.postgraduate.definition}</p>
                    <p><strong>Duration:</strong> ${detailedLevels.postgraduate.duration}</p>
                    
                    <h4>Program Types:</h4>
                    <ul>
                        ${Object.entries(detailedLevels.postgraduate.types).map(([key, value]) => 
                            `<li><strong>${key.charAt(0).toUpperCase() + key.slice(1)}:</strong> ${value}</li>`
                        ).join('')}
                    </ul>
                </div>
                ` : ''}

                ${detailedLevels.doctorate ? `
                <div class="education-level-detail">
                    <h3>🔬 Doctorate Level (Ph.D. Programs)</h3>
                    <p><strong>Definition:</strong> ${detailedLevels.doctorate.definition}</p>
                    <p><strong>Duration:</strong> ${detailedLevels.doctorate.duration}</p>
                    
                    <h4>Research Areas:</h4>
                    <ul>
                        ${detailedLevels.doctorate.researchAreas.slice(0, 5).map(area => `<li>${area}</li>`).join('')}
                    </ul>
                    
                    ${detailedLevels.doctorate.fundingOptions ? `
                    <h4>Funding Options:</h4>
                    <ul>
                        ${detailedLevels.doctorate.fundingOptions.slice(0, 4).map(option => `<li>${option}</li>`).join('')}
                    </ul>
                    ` : ''}
                </div>
                ` : ''}
            </div>
        `;
    }

    generateIntegratedProgramsSection(integratedPrograms) {
        return `
            <h3>🔗 Dual Degree Options:</h3>
            <ul>
                ${integratedPrograms.dualDegreeOptions ? integratedPrograms.dualDegreeOptions.slice(0, 4).map(option => `<li>${option}</li>`).join('') : ''}
            </ul>
            
            <h3>🤝 Combined Programs:</h3>
            <ul>
                ${integratedPrograms.combinedPrograms ? integratedPrograms.combinedPrograms.slice(0, 3).map(program => `<li>${program}</li>`).join('') : ''}
            </ul>
            
            <h3>⚡ Accelerated Pathways:</h3>
            <ul>
                ${integratedPrograms.acceleratedPathways ? integratedPrograms.acceleratedPathways.slice(0, 3).map(pathway => `<li>${pathway}</li>`).join('') : ''}
            </ul>
            
            <h3>✅ Benefits:</h3>
            <ul>
                ${integratedPrograms.benefits ? integratedPrograms.benefits.slice(0, 4).map(benefit => `<li>${benefit}</li>`).join('') : `
                <li>Reduced total study duration by 1-2 years</li>
                <li>Cost-effective education pathway</li>
                <li>Better industry connections</li>
                <li>Higher placement prospects</li>`}
            </ul>
        `;
    }

    capitalizeField(field) {
        return field.charAt(0).toUpperCase() + field.slice(1);
    }

    async showCareerDetails(careerCode) {
        const career = this.careers.find(c => c.code === careerCode);
        if (!career) return;

        const modal = document.getElementById('careerModal');
        const detailsContainer = document.getElementById('careerDetails');
        
        // Show loading
        detailsContainer.innerHTML = '<div class="loading"></div><p>Loading career details...</p>';
        modal.style.display = 'block';
        
        // Simulate loading detailed data
        setTimeout(() => {
            detailsContainer.innerHTML = this.generateCareerDetails(career);
        }, 1000);
    }

    generateCareerDetails(career) {
        // Get detailed roadmap data from the career database
        let roadmapData = null;
        try {
            if (typeof getCareerRoadmap === 'function') {
                roadmapData = getCareerRoadmap(career.code);
            }
        } catch (error) {
            console.log('Could not load detailed roadmap data');
        }

        // Generate comprehensive career details with enhanced education levels
        const fieldTemplates = {
            technology: {
                highSchool: { stream: "Science (PCM)", percentage: "75%+", entrance: "JEE Main/Advanced" },
                bachelor: { duration: "4 years", courses: ["B.Tech Computer Science", "B.E. Information Technology", "BCA"] },
                masters: { duration: "2 years", courses: ["M.Tech", "MCA", "MS Computer Science"] },
                salary: { india: "₹3-25 LPA", global: "$60,000-150,000" }
            },
            healthcare: {
                highSchool: { stream: "Science (PCB)", percentage: "85%+", entrance: "NEET" },
                bachelor: { duration: "4.5-5.5 years", courses: ["MBBS", "BDS", "BAMS", "BHMS"] },
                masters: { duration: "2-3 years", courses: ["MD", "MS", "MDS"] },
                salary: { india: "₹4-30 LPA", global: "$70,000-200,000" }
            },
            engineering: {
                highSchool: { stream: "Science (PCM)", percentage: "75%+", entrance: "JEE Main/Advanced" },
                bachelor: { duration: "4 years", courses: ["B.Tech", "B.E."] },
                masters: { duration: "2 years", courses: ["M.Tech", "ME"] },
                salary: { india: "₹3-20 LPA", global: "$65,000-130,000" }
            },
            business: {
                highSchool: { stream: "Any (Commerce preferred)", percentage: "60%+", entrance: "CAT/MAT" },
                bachelor: { duration: "3 years", courses: ["B.Com", "BBA", "B.A. Economics"] },
                masters: { duration: "2 years", courses: ["MBA", "M.Com", "CA"] },
                salary: { india: "₹3-15 LPA", global: "$50,000-120,000" }
            },
            education: {
                highSchool: { stream: "Any", percentage: "60%+", entrance: "University specific" },
                bachelor: { duration: "3-4 years", courses: ["B.A.", "B.Sc.", "B.Ed"] },
                masters: { duration: "2 years", courses: ["M.A.", "M.Sc.", "M.Ed"] },
                salary: { india: "₹2-10 LPA", global: "$40,000-80,000" }
            }
        };

        const template = fieldTemplates[career.field] || fieldTemplates.technology;

        return `
            <div class="career-detail">
                <h1>${career.title}</h1>
                <p><strong>Field:</strong> ${this.capitalizeField(career.field)} | <strong>Level:</strong> ${career.level}</p>
                <p><strong>Overview:</strong> ${career.overview}</p>
                
                <h2>📚 Education Pathway</h2>
                
                <div class="pathway-section">
                    <h3>🏫 High School (Classes 11-12)</h3>
                    <ul>
                        <li><strong>Stream:</strong> ${template.highSchool.stream}</li>
                        <li><strong>Minimum Percentage:</strong> ${template.highSchool.percentage}</li>
                        <li><strong>Entrance Exams:</strong> ${template.highSchool.entrance}</li>
                    </ul>
                </div>

                <div class="pathway-section">
                    <h3>🎓 Bachelor's Degree (${template.bachelor.duration})</h3>
                    <ul>
                        ${template.bachelor.courses.map(course => `<li>${course}</li>`).join('')}
                    </ul>
                </div>

                <div class="pathway-section">
                    <h3>🎯 Master's Degree (${template.masters.duration})</h3>
                    <ul>
                        ${template.masters.courses.map(course => `<li>${course}</li>`).join('')}
                    </ul>
                </div>

                ${roadmapData && roadmapData.detailedEducationLevels ? this.generateDetailedEducationLevels(roadmapData.detailedEducationLevels, roadmapData.educationLevelDistinctions) : ''}

                <div class="integrated-programs">
                    <h2>⚡ Integrated Programs</h2>
                    ${roadmapData && roadmapData.integratedPrograms ? this.generateIntegratedProgramsSection(roadmapData.integratedPrograms) : `
                    <h3>Dual Degree Options:</h3>
                    <ul>
                        <li>Bachelor's + Master's (5 years) - Integrated ${career.title} program</li>
                        <li>Degree + MBA (5.5 years) - ${career.title} management focus</li>
                        <li>Technical + Research (5-6 years) - Advanced ${career.title} studies</li>
                    </ul>
                    
                    <h3>Benefits:</h3>
                    <ul>
                        <li>Reduced total study duration by 1-2 years</li>
                        <li>Cost-effective education pathway</li>
                        <li>Better industry connections</li>
                        <li>Higher placement prospects</li>
                    </ul>`}
                </div>

                <div class="salary-info">
                    <h2>💰 Salary Information</h2>
                    <p><strong>India:</strong> ${template.salary.india}</p>
                    <p><strong>Global:</strong> ${template.salary.global}</p>
                </div>

                <h2>🏆 Extracurricular Activities</h2>
                <ul>
                    <li>Industry-specific certifications</li>
                    <li>Internships and practical training</li>
                    <li>Professional workshops and seminars</li>
                    <li>Research projects and publications</li>
                    <li>Leadership roles in student organizations</li>
                </ul>

                <h2>📈 Latest Trends</h2>
                <ul>
                    <li>Digital transformation and automation</li>
                    <li>Sustainable and green practices</li>
                    <li>AI and machine learning integration</li>
                    <li>Remote work and digital collaboration</li>
                    <li>Continuous learning and upskilling</li>
                </ul>

                <h2>🎯 Career Progression</h2>
                <ul>
                    <li><strong>Entry Level (0-2 years):</strong> Junior ${career.title}</li>
                    <li><strong>Mid Level (2-5 years):</strong> ${career.title}</li>
                    <li><strong>Senior Level (5-10 years):</strong> Senior ${career.title}</li>
                    <li><strong>Expert Level (10+ years):</strong> Lead/Principal ${career.title}</li>
                </ul>
            </div>
        `;
    }

    closeModal() {
        document.getElementById('careerModal').style.display = 'none';
    }

    updateStats() {
        // Update stats with actual numbers
        document.getElementById('totalCareers').textContent = '1000+';
        document.getElementById('detailedRoadmaps').textContent = '123';
    }
}

// Scroll to search function
function scrollToSearch() {
    document.querySelector('.search-section').scrollIntoView({ behavior: 'smooth' });
}

// Initialize the career explorer when page loads
let careerExplorer;
document.addEventListener('DOMContentLoaded', () => {
    careerExplorer = new CareerExplorer();
});

// Add some interactive animations
document.addEventListener('DOMContentLoaded', () => {
    // Animate stats on scroll
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);

    // Observe all sections for animation
    document.querySelectorAll('section').forEach(section => {
        section.style.opacity = '0';
        section.style.transform = 'translateY(20px)';
        section.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(section);
    });

    // Add typing effect to tagline
    const tagline = document.querySelector('.tagline');
    if (tagline) {
        const text = tagline.textContent;
        tagline.textContent = '';
        let i = 0;
        const typeWriter = () => {
            if (i < text.length) {
                tagline.textContent += text.charAt(i);
                i++;
                setTimeout(typeWriter, 50);
            }
        };
        setTimeout(typeWriter, 1000);
    }
});
