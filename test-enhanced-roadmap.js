// Test script to verify enhanced roadmap generation with detailed education levels
const { 
    COMPLETE_CAREER_DATABASE, 
    getCareerRoadmap 
} = require('./complete-career-database.js');

// Test a specific occupation to verify detailed education levels
function testEnhancedRoadmap() {
    console.log('=== TESTING ENHANCED ROADMAP GENERATION ===\n');
    
    // Get a technology occupation for testing
    const techOccupations = Object.keys(COMPLETE_CAREER_DATABASE).filter(code => {
        const occupation = COMPLETE_CAREER_DATABASE[code];
        return occupation.field === 'technology';
    });
    
    if (techOccupations.length === 0) {
        console.log('No technology occupations found. Testing with first available occupation...');
        const firstOccupation = Object.keys(COMPLETE_CAREER_DATABASE)[0];
        testOccupationRoadmap(firstOccupation);
    } else {
        console.log(`Found ${techOccupations.length} technology occupations. Testing first one...`);
        testOccupationRoadmap(techOccupations[0]);
    }
}

function testOccupationRoadmap(occupationCode) {
    console.log(`Testing occupation: ${occupationCode}`);
    
    const roadmap = getCareerRoadmap(occupationCode);
    
    if (!roadmap) {
        console.log('❌ Roadmap not generated');
        return;
    }
    
    console.log('✅ Roadmap generated successfully');
    console.log(`Title: ${roadmap.occupationTitle || roadmap.title}`);
    console.log(`Field: ${roadmap.field}`);
    console.log(`Level: ${roadmap.educationLevel || roadmap.level}`);
    
    // Test detailed education levels
    if (roadmap.detailedEducationLevels) {
        console.log('\n✅ Detailed Education Levels found:');
        
        if (roadmap.detailedEducationLevels.graduate) {
            console.log('  - Graduate level: ✅');
            console.log(`    Definition: ${roadmap.detailedEducationLevels.graduate.definition.substring(0, 80)}...`);
            console.log(`    Duration: ${roadmap.detailedEducationLevels.graduate.duration}`);
        }
        
        if (roadmap.detailedEducationLevels.postgraduate) {
            console.log('  - Postgraduate level: ✅');
            console.log(`    Definition: ${roadmap.detailedEducationLevels.postgraduate.definition.substring(0, 80)}...`);
        }
        
        if (roadmap.detailedEducationLevels.doctorate) {
            console.log('  - Doctorate level: ✅');
            console.log(`    Definition: ${roadmap.detailedEducationLevels.doctorate.definition.substring(0, 80)}...`);
        }
    } else {
        console.log('❌ Detailed Education Levels missing');
    }
    
    // Test education level distinctions
    if (roadmap.educationLevelDistinctions) {
        console.log('\n✅ Education Level Distinctions found:');
        console.log(`  Graduate: ${roadmap.educationLevelDistinctions.graduate}`);
        console.log(`  Postgraduate: ${roadmap.educationLevelDistinctions.postgraduate}`);
        console.log(`  Doctorate: ${roadmap.educationLevelDistinctions.doctorate}`);
    } else {
        console.log('❌ Education Level Distinctions missing');
    }
    
    // Test stream guidance
    if (roadmap.streamGuidance) {
        console.log('\n✅ Stream Guidance found:');
        console.log(`  Recommended: ${roadmap.streamGuidance.recommended}`);
        console.log(`  Flexibility: ${roadmap.streamGuidance.flexibility}`);
    } else {
        console.log('❌ Stream Guidance missing');
    }
    
    // Test timelines
    console.log('\n✅ Timeline Information:');
    console.log(`  India: ${roadmap.timeline}`);
    console.log(`  Abroad: ${roadmap.timelineAbroad}`);
    
    // Test latest trends
    if (roadmap.latestTrends && roadmap.latestTrends.length > 0) {
        console.log('\n✅ Latest Trends found:');
        console.log(`  ${roadmap.latestTrends.join(', ')}`);
    } else {
        console.log('❌ Latest Trends missing');
    }
    
    console.log('\n=== TEST COMPLETED ===');
}

// Run the test
testEnhancedRoadmap();
