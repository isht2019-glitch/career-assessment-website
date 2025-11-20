#!/usr/bin/env python3
"""Generate Android OccupationsDatabase.kt from occupations list"""

# Read occupations
with open('../occupations-list.txt', 'r', encoding='utf-8') as f:
    occupations = [line.strip() for line in f if line.strip()]

print(f"Generating Android database with {len(occupations)} occupations...")

# Create Kotlin file
kotlin_code = '''package com.theapp

/**
 * Complete O*NET Occupations Database
 * 637 careers with smart RIASEC filtering
 */
object OccupationsDatabase {
    
    // All occupations alphabetically
    val allOccupations = listOf(
'''

# Add all occupations
for occ in sorted(set(occupations)):
    occ_escaped = occ.replace('"', '\\"')
    kotlin_code += f'        "{occ_escaped}",\n'

kotlin_code += '''    )
    
    // Keywords for RIASEC personality types
    private val typeKeywords = mapOf(
        "R" to listOf("engineer", "mechanic", "technician", "pilot", "construction", "electrician", "plumber", "welder", "machinist", "installer", "repairer", "operator", "driver", "equipment", "maintenance", "industrial", "manufacturing", "automotive", "aircraft", "diesel", "hvac", "boiler", "crane", "heavy", "truck", "mining", "oil", "gas", "power", "plant", "solar", "wind", "energy", "surveyor", "drafter", "inspector", "firefighter", "police", "military", "crew", "officer"),
        "I" to listOf("scientist", "researcher", "analyst", "data", "software", "computer", "information", "database", "network", "systems", "programmer", "developer", "web", "security", "physician", "doctor", "medical", "health", "clinical", "laboratory", "technologist", "biologist", "chemist", "physicist", "mathematician", "statistician", "economist", "psychologist", "sociologist", "anthropologist", "geographer", "historian", "political", "urban", "planner", "environmental", "conservation", "research", "epidemiologist", "geneticist", "microbiologist", "biochemist"),
        "A" to listOf("designer", "artist", "writer", "author", "editor", "musician", "singer", "composer", "dancer", "choreographer", "actor", "director", "producer", "photographer", "camera", "film", "video", "broadcast", "announcer", "reporter", "journalist", "news", "media", "graphic", "interior", "fashion", "commercial", "industrial", "floral", "craft", "fine", "multimedia", "animator", "illustrator", "exhibit", "set", "makeup", "costume", "creative", "poet", "lyricist", "technical writer", "interpreter", "translator"),
        "S" to listOf("teacher", "instructor", "educator", "professor", "counselor", "therapist", "social worker", "nurse", "nursing", "healthcare", "medical assistant", "dental", "physical therapist", "occupational therapist", "speech", "respiratory", "radiation", "dietitian", "nutritionist", "pharmacist", "physician assistant", "midwife", "chiropractor", "optometrist", "podiatrist", "veterinarian", "clergy", "religious", "recreation", "fitness", "coach", "trainer", "childcare", "preschool", "kindergarten", "elementary", "middle school", "secondary", "special education", "guidance", "rehabilitation", "substance abuse", "mental health", "marriage", "family"),
        "E" to listOf("manager", "director", "executive", "administrator", "chief", "supervisor", "sales", "marketing", "business", "operations", "general manager", "financial manager", "human resources", "purchasing", "training", "development", "compensation", "benefits", "compliance", "regulatory", "fundraising", "public relations", "advertising", "promotions", "real estate", "broker", "agent", "property", "lodging", "food service", "entertainment", "recreation", "meeting", "event", "planner", "lawyer", "judge", "legislator", "arbitrator", "mediator", "entrepreneur", "merchant", "buyer", "loan officer", "insurance", "securities", "investment"),
        "C" to listOf("accountant", "auditor", "bookkeeper", "payroll", "billing", "clerk", "administrative", "secretary", "receptionist", "office", "data entry", "word processor", "typist", "file", "mail", "postal", "shipping", "receiving", "inventory", "stock", "order", "procurement", "production planning", "expediting", "dispatcher", "scheduler", "customer service", "information clerk", "interviewer", "teller", "cashier", "counter", "reservation", "ticket", "court reporter", "legal assistant", "paralegal", "medical records", "health information", "library assistant", "budget analyst", "financial analyst", "credit analyst", "tax", "claims", "insurance", "brokerage", "compliance officer", "inspector", "examiner", "appraiser")
    )
    
    /**
     * Get occupations filtered by personality type
     */
    fun getOccupationsByType(primaryType: String, secondaryType: String? = null): List<String> {
        val primaryKeywords = typeKeywords[primaryType] ?: emptyList()
        val secondaryKeywords = if (secondaryType != null) typeKeywords[secondaryType] ?: emptyList() else emptyList()
        val allKeywords = primaryKeywords + secondaryKeywords
        
        val filtered = allOccupations.filter { occupation ->
            val lowerOccupation = occupation.lowercase()
            allKeywords.any { keyword -> lowerOccupation.contains(keyword) }
        }
        
        return if (filtered.isNotEmpty()) filtered else allOccupations
    }
    
    /**
     * Convert occupation name to slug
     */
    fun occupationToSlug(name: String): String {
        return name.lowercase()
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
    }
}
'''

# Write file
with open('app/src/main/java/com/theapp/OccupationsDatabase.kt', 'w', encoding='utf-8') as f:
    f.write(kotlin_code)

print(f"✅ Created OccupationsDatabase.kt with {len(set(occupations))} occupations")
