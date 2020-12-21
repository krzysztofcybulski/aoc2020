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

    private fun ingredientsAllergens(): Map<String, String> {
        val possible = allergensToIngredients
            .flatMap { (allergen, ingredients) -> ingredients.map { it to allergen } }
            .groupBy { it.first }
            .mapValues { (_, v) -> v.map { it.second }.toMutableList() }
        while (possible.filter { it.value.size > 1 }.isNotEmpty()) {
            possible
                .filter { it.value.size == 1 }
                .forEach { (ing, all) ->
                    possible.filter { it.value.contains(all[0]) && it.key != ing }
                        .forEach { (a, b) -> b.remove(all[0]) }
                }
        }
        return possible.mapValues { (k, v) -> v[0] }
    }

    class Food(
        val ingredients: Set<String>,
        val allergens: Set<String>
    )

}

fun main() {
    val lines = lines("21AllergenAssessment")
        .map { parse(it) }

    AllergenAssessment(lines)
        .ingredientsAllergens
        .toList()
        .sortedBy { it.second }
        .joinToString(",") { it.first }
        .run { println(this) }
}

private fun parse(line: String): AllergenAssessment.Food {
    val (ingredients, allergens) = line.dropLast(1).split(" (contains ")
    return AllergenAssessment.Food(
        ingredients.split(" ").toSet(),
        allergens.split(", ").toSet()
    )
}
