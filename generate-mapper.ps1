# Generate OccupationMapper.kt from FINAL_COMPLETE_ALL_ROADMAPS.js

$jsContent = Get-Content "FINAL_COMPLETE_ALL_ROADMAPS.js" -Raw
$pattern = '^\s*"([a-z0-9-]+)":\s*`'
$matches = [regex]::Matches($jsContent, $pattern, [System.Text.RegularExpressions.RegexOptions]::Multiline)

$slugs = @()
foreach ($match in $matches) {
    $slugs += $match.Groups[1].Value
}

Write-Host "Found $($slugs.Count) occupation slugs"

# Generate Kotlin mappings
$mappings = @()
foreach ($slug in $slugs) {
    # Convert slug to display name: "software-developers" -> "Software Developers"
    $displayName = ($slug -split '-' | ForEach-Object { 
        $_.Substring(0,1).ToUpper() + $_.Substring(1) 
    }) -join ' '
    
    $mappings += "        `"$($displayName.ToLower())`" to `"$slug`""
}

$mappingsString = $mappings -join ",`n"

# Generate Kotlin file
$ktContent = @"
package com.theapp

/**
 * Maps occupation display names to database slugs
 * Auto-generated from FINAL_COMPLETE_ALL_ROADMAPS.js
 * Total: $($slugs.Count) occupations
 * Generated: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
 */
object OccupationMapper {
    
    private val occupationMap = mapOf(
$mappingsString
    )
    
    /**
     * Get database slug for occupation name
     * @param occupationName Display name of occupation
     * @return Database slug or null if not found
     */
    fun getSlug(occupationName: String): String? {
        val normalized = occupationName.lowercase().trim()
        return occupationMap[normalized]
    }
    
    /**
     * Check if occupation has a mapping
     */
    fun hasMapping(occupationName: String): Boolean {
        return getSlug(occupationName) != null
    }
    
    /**
     * Get all mapped occupation names
     */
    fun getAllMappedOccupations(): List<String> {
        return occupationMap.keys.toList()
    }
    
    /**
     * Get count of mapped occupations
     */
    fun getTotalCount(): Int {
        return occupationMap.size
    }
    
    /**
     * Search occupations by keyword
     */
    fun searchOccupations(keyword: String): List<String> {
        val lowerKeyword = keyword.lowercase()
        return occupationMap.keys.filter { it.contains(lowerKeyword) }
    }
}
"@

# Write to file
$outputPath = "android-app\app\src\main\java\com\theapp\OccupationMapper.kt"
$ktContent | Out-File -FilePath $outputPath -Encoding UTF8

Write-Host "✅ Generated $outputPath with $($slugs.Count) mappings"
