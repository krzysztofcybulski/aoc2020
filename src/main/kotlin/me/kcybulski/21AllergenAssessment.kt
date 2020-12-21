package me.kcybulski

import me.kcybulski.utils.lines

class AllergenAssessment(val list: List<Food>) {

    val allergensToIngredients = allergensToIngredients()
    val ingredientsAllergens = ingredientsAllergens()

    private fun allergensToIngredients(): Map<String, Set<String>> {
        val allergensToIngredients = mutableMapOf<String, Set<String>>()
        list.forEach { food ->
            food.allergens.forEach { allergen ->
                allergensToIngredients.merge(allergen, food.ingredients) { a, b -> a.intersect(b) }
            }
        }
        return allergensToIngredients.toMap()
    }

    private fun ingredientsAllergens(): Map<String, String> = allergensToIngredients
        .flatMap { (allergen, ingredients) -> ingredients.map { it to allergen } }
        .toMap()

    class Food(
        val ingredients: Set<String>,
        val allergens: Set<String>
    )

}

fun main() {
    val lines = lines("21AllergenAssessment")
        .map { parse(it) }
    val allergenAssessment = AllergenAssessment(lines)
    allergenAssessment
        .list
        .flatMap { it.ingredients }
        .filter { it !in allergenAssessment.ingredientsAllergens.keys }
        .run { println(size) }
}

private fun parse(line: String): AllergenAssessment.Food {
    val (ingredients, allergens) = line.dropLast(1).split(" (contains ")
    return AllergenAssessment.Food(
        ingredients.split(" ").toSet(),
        allergens.split(", ").toSet()
    )
}
