package com.justin.id_34279075.nutritrack.data.foodIntake

/**
 * This data class is implemented as an extra feature for storing food category information for user reference.
 */
data class FoodCategoryInfo(
    val categoryName: String,
    val energyPerServe: String,
    val maxScore: List<Int>,
    val maxScoreCriteria: List<String>,
    val zeroScoreCriteria: List<String>,
    val details: String,
    val examples: List<String>
)

/**
 * Here, hard-coded values are stored, representing each food categories along with their details such as maxScore and examples, obtained from Dietary Guidelines files on Ed.
 */
object FoodCategories {
    val discretionary = FoodCategoryInfo(
        categoryName = "Discretionary",
        energyPerServe = "600kJ",
        maxScore = listOf(10),
        maxScoreCriteria = listOf("Males: < 3 serves, Females: < 2.5 serves"),
        zeroScoreCriteria = listOf("Males: ≥ 6 serves", "Females: ≥ 5.5 serves"),
        details = "Foods that are high in saturated fat, added sugar, salt, and/or alcohol that are not necessary for a healthy diet.",
        examples = listOf("Chips", "Sweets", "Soft drinks")
    )

    val vegetables = FoodCategoryInfo(
        categoryName = "Vegetables",
        energyPerServe = "75g",
        maxScore = listOf(5, 5),
        maxScoreCriteria = listOf("Males: >= 6 serves, Females: >= 5 serves", "Variety of vegetables consumed"),
        zeroScoreCriteria = listOf("No vegetables", "Variety score: 0"),
        details = "Vegetable variety: Consuming different types of vegetables (leafy, cruciferous, root, etc.)",
        examples = listOf("~1/2 cup cooked vegetables", "1 cup leafy salad")
    )

    val fruits = FoodCategoryInfo(
        categoryName = "Fruits",
        energyPerServe = "350kJ",
        maxScore = listOf(5, 5),
        maxScoreCriteria = listOf("≥ 2 serves", "≥ 2 varieties of fruit consumed"),
        zeroScoreCriteria = listOf("No fruit", "Variety score: 0"),
        details = "Consuming different types of fruits across categories (e.g., berries, citrus, stone fruits).",
        examples = listOf("1 medium fruit", "2 small fruits")
    )

    val grains = FoodCategoryInfo(
        categoryName = "Grains and cereals",
        energyPerServe = "500kJ",
        maxScore = listOf(5, 5),
        maxScoreCriteria = listOf("≥ 6 serves", "≥ 50% wholegrains or ≥ 3 serves"),
        zeroScoreCriteria = listOf("No grains and/or cereals", "No wholegrains"),
        details = "Wholegrains retain all parts of the grain. Refined grains have had bran and germ removed.",
        examples = listOf("Brown rice", "Wholegrain bread")
    )

    val meat = FoodCategoryInfo(
        categoryName = "Meat and alternatives",
        energyPerServe = "550kJ",
        maxScore = listOf(10),
        maxScoreCriteria = listOf("Males: ≥ 3 serves, Females: ≥ 2.5 serves"),
        zeroScoreCriteria = listOf("Males: ≤ 0.5 serves", "Females: 0 serves"),
        details = "Includes eggs, nuts, seeds, legumes, tofu.",
        examples = listOf("65-100g cooked meat", "2 eggs", "170g tofu", "30g nuts/seeds")
    )

    val dairy = FoodCategoryInfo(
        categoryName = "Dairy and alternatives",
        energyPerServe = "550kJ",
        maxScore = listOf(10),
        maxScoreCriteria = listOf("≥ 2.5 serves"),
        zeroScoreCriteria = listOf("No dairy and/or alternatives"),
        details = "Includes plant-based milk/products fortified with calcium.",
        examples = listOf("250ml milk", "200g yogurt", "40g cheese")
    )

    val water = FoodCategoryInfo(
        categoryName = "Water",
        energyPerServe = "N/A",
        maxScore = listOf(5),
        maxScoreCriteria = listOf("≥ 50% water consumed relative to total beverages"),
        zeroScoreCriteria = listOf("Did not meet 1.5L of non-alcoholic beverages"),
        details = "Total beverages include all fluids consumed. Recommended: 8-10 cups/day.",
        examples = listOf("Water")
    )

    val fats = FoodCategoryInfo(
        categoryName = "Fats",
        energyPerServe = "10g",
        maxScore = listOf(5, 5),
        maxScoreCriteria = listOf("Saturated fat ≤ 10% of total energy", "MUFA & PUFA Males: 4 serves, MUFA & PUFA Females: 2 serves"),
        zeroScoreCriteria = listOf("Saturated fat ≥ 12%", "MUFA & PUFA Males: < 1 serve, MUFA & PUFA Females: < 0.5 serves"),
        details = "Saturated fat comes from animal products and some oils. MUFA(Monounsaturated Fatty Acids): olive oil, avocados. PUFA(Polyunsaturated Fatty Acids) : fish, nuts, seeds.",
        examples = listOf("10g fat or 2 teaspoons")
    )

    val sodium = FoodCategoryInfo(
        categoryName = "Sodium",
        energyPerServe = "N/A",
        maxScore = listOf(10),
        maxScoreCriteria = listOf("≤ 70 mmol (920 mg)"),
        zeroScoreCriteria = listOf("> 100 mmol (3200 mg)"),
        details = "Main component of salt (NaCl).",
        examples = listOf("Recommended < 2000mg/day")
    )

    val addedSugars = FoodCategoryInfo(
        categoryName = "Added sugars",
        energyPerServe = "N/A",
        maxScore = listOf(10),
        maxScoreCriteria = listOf("< 15% of total energy intake"),
        zeroScoreCriteria = listOf("> 20% of total energy intake"),
        details = "Sugars added during processing/preparation.",
        examples = listOf("<10% energy from added sugars.")
    )

    val alcohol = FoodCategoryInfo(
        categoryName = "Alcohol",
        energyPerServe = "10g = 1 std",
        maxScore = listOf(5),
        maxScoreCriteria = listOf("≤ 1.4 standard drinks/day"),
        zeroScoreCriteria = listOf("> 1.4 standard drinks/day"),
        details = "Standard drink contains 10g pure alcohol. For health reasons, consuming no alcohol is safest.",
        examples = listOf("100ml wine (13% alcohol)", "285ml beer (4.9% alcohol)")
    )
}

