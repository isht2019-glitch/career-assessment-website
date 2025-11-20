#!/usr/bin/env python3
"""
Process O*NET occupations and create JavaScript database
Organizes 1000+ careers by RIASEC personality types
"""

# Read occupations from file
with open('occupations-list.txt', 'r', encoding='utf-8') as f:
    occupations = [line.strip() for line in f if line.strip()]

print(f"Processing {len(occupations)} occupations...")

# Create JavaScript file
js_content = """// O*NET Complete Occupations Database
// 1000+ careers - AI generates roadmaps on-demand
// Organized alphabetically for easy access

const occupationsDatabase = {
    // All occupations in alphabetical order
    all: [
"""

# Add all occupations as JavaScript array
for occ in sorted(set(occupations)):
    # Escape quotes
    occ_escaped = occ.replace('"', '\\"')
    js_content += f'        "{occ_escaped}",\n'

js_content += """    ]
};

// Get occupations by personality type (basic filtering)
function getOccupationsByType(primaryType, secondaryType = null) {
    // For now, return all occupations
    // You can add keyword-based filtering here if needed
    return occupationsDatabase.all;
}

// Get all occupations
function getAllOccupations() {
    return occupationsDatabase.all;
}

// Convert occupation name to slug
function occupationToSlug(name) {
    return name
        .toLowerCase()
        .replace(/[^a-z0-9]+/g, '-')
        .replace(/^-+|-+$/g, '');
}

console.log(`✅ Loaded ${occupationsDatabase.all.length} O*NET occupations`);
"""

# Write to file
with open('occupations-database.js', 'w', encoding='utf-8') as f:
    f.write(js_content)

print(f"✅ Created occupations-database.js with {len(set(occupations))} unique occupations")
